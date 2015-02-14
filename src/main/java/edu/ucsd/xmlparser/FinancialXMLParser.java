package edu.ucsd.xmlparser;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.xml.parsers.DocumentBuilder; 
import javax.xml.parsers.DocumentBuilderFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import edu.ucsd.cvalue.CValueCalculator;
import edu.ucsd.cvalue.CValueData;
import edu.ucsd.nlpparser.StanfordParser;
import edu.ucsd.xmlparser.entity.ApplicationRelationshipType;
import edu.ucsd.xmlparser.entity.Document;
import edu.ucsd.xmlparser.entity.NodeName;
import edu.ucsd.xmlparser.entity.Sentence;
import edu.ucsd.xmlparser.util.GraphDatabaseUtils;

public class FinancialXMLParser {
	@Inject
	private GraphDatabaseUtils graphDatabaseUtils;
	
	@Inject
	private Neo4jTemplate template;
	
	@Inject
	private StanfordParser stanfordParser;
	
	private int sentenceNumber = 0;
	
	private static Logger logger = LoggerFactory.getLogger(FinancialXMLParser.class);
	
	public FinancialXMLParser() {
	}
	
	/**
	 * Entry point to the whole parsing mechanism 
	 * - Utilizes a DOM Parser for now
	 * 
	 * @param file
	 * @throws Exception
	 */
	@Transactional
	public void parseAndLoad(File file) throws Exception {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder(); 
		Node documentNode = db.parse(file);
		org.neo4j.graphdb.Node documentGraphNode = graphDatabaseUtils.toDocumentGraphNode(documentNode);
		Document document = new Document(file.getName(), 2012, 1);
		template.save(document);
		Map<String, Integer> termAndFrequency = new HashMap<String, Integer>();
		visit(documentGraphNode, documentNode, template.getNode(document.getId()), termAndFrequency, null, null);
		computeCValue(termAndFrequency);
	}
	
	private void computeCValue(Map<String, Integer> termAndFrequency) {
		System.out.println("Number of terms raw: " + termAndFrequency.size());
		List<CValueData> cValueDatas = termAndFrequency.keySet().stream().filter(t -> (termAndFrequency.get(t) > 0)).map(t -> new CValueData(t, termAndFrequency.get(t), new StringTokenizer(t).countTokens())).collect(Collectors.toList());
		Collections.sort(cValueDatas, new Comparator<CValueData>() {
			@Override
			public int compare(CValueData o1, CValueData o2) {
				return new Integer(o2.getPhraseLength()).compareTo(new Integer(o1.getPhraseLength()));
			}
		});
		CValueCalculator.calculate(cValueDatas);
		System.out.println("Number of Terms : " + cValueDatas.size());
		
		File fout = new File("cvalue.txt");
		
		Collections.sort(cValueDatas, new Comparator<CValueData>() {
			@Override
			public int compare(CValueData o1, CValueData o2) {
				return o2.getCValue().compareTo(o1.getCValue());
			}
		});
		
		try(BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fout)))) {
			for(CValueData d : cValueDatas) {
				bw.write(d.getPhrase() + " " + d.getCValue());
				bw.newLine();
			}
		} catch(IOException ioe) {
			System.err.println(ioe.getMessage());
		}
	}

	/**
	 * 
	 * 
	 * @param graphNode
	 * @param xmlNode
	 * @param document
	 * @param documentTermAndFrequency
	 * @param sectionId -- corresponds to either the id of a <P> tag or a <Sect> tag
	 */
	private void visit(org.neo4j.graphdb.Node graphNode, Node xmlNode, org.neo4j.graphdb.Node document, Map<String, Integer> documentTermAndFrequency, Long sectionId, Map<String, Integer> sectionTermAndFrequency) {
		NodeList children = xmlNode.getChildNodes();
		org.neo4j.graphdb.Node previousGraphChildNode = null;
		boolean isSection = false;
		boolean isIndependentParagraphSection = false;
		
		for(int i = 0; i < children.getLength(); i++) {			
			Node childNode = children.item(i);
		
			org.neo4j.graphdb.Node graphChildNode = graphDatabaseUtils.toGraphNode(childNode);

			// Create Relationship(s) between Parent and Child
			graphDatabaseUtils.createRelationship(graphNode, graphChildNode, ApplicationRelationshipType.HAS_CHILD);
			// Create a special Relationship if this is the first child processed
			if(i == 0) {
				graphDatabaseUtils.createRelationship(graphNode, graphChildNode, ApplicationRelationshipType.FIRST_CHILD);
			}

			// Create Relationship between Siblings
			if(previousGraphChildNode != null) {
				graphDatabaseUtils.createRelationship(previousGraphChildNode, graphChildNode, ApplicationRelationshipType.NEXT);
			}

			previousGraphChildNode = graphChildNode;

			// Root node does not have a parent
			if(xmlNode.getParentNode() != null) {
				// Are we in a <P> within a <Sect>, if not then sectionId has to be null and we will see a P
				if(isSection(xmlNode)) {
					sectionId = graphNode.getId();
					isSection = true;
					sectionTermAndFrequency = new HashMap<String, Integer>();
				} else if(sectionId == null && NodeName.PARAGRAPH.getTextName().equals(xmlNode.getNodeName())) {
					sectionId = graphNode.getId();
					isIndependentParagraphSection = true;
					sectionTermAndFrequency = new HashMap<String, Integer>();
				}
			}
			
			visit(graphChildNode, childNode, document, documentTermAndFrequency, sectionId, sectionTermAndFrequency);
		} 
		
		// Parent Node is null for the top level of the document
		if(xmlNode.getParentNode() != null && isParentParagraphNode(xmlNode.getParentNode())) {
			Node hashText = xmlNode;
			logger.info("Sentence Number : " + this.sentenceNumber + " with value : " + hashText.getNodeValue());
			String rawSentence = hashText.getNodeValue();
			// IMPORTANT, this isn't a hard and fast rule, more like a hack for now
			if(!(rawSentence.startsWith("% Change") && rawSentence.length() > 30 )) {
				List<Sentence> sentences = stanfordParser.parseAndLoad(hashText.getNodeValue(), this.sentenceNumber, documentTermAndFrequency, sectionId, sectionTermAndFrequency);
				for(Sentence sentence : sentences) {
					org.neo4j.graphdb.Node sentenceNode = graphDatabaseUtils.getNode(sentence);
					graphDatabaseUtils.createRelationship(graphNode, sentenceNode, ApplicationRelationshipType.HAS_CHILD);
					graphDatabaseUtils.createRelationship(document, sentenceNode, ApplicationRelationshipType.HAS_SENTENCE);
					this.sentenceNumber = this.sentenceNumber + sentences.size();
				}
			}
		}
	}

	private boolean isSection(Node xmlNode) {
		return NodeName.SECTION.getTextName().equals(xmlNode.getNodeName());
	}

	/**
	 * In our PDF document, texts are usually encoded in the following way.
	 * <P>some text</P>
	 * 
	 * @param childNode
	 * @return
	 */
	private boolean isParagraphTextNode(Node childNode) {
		return NodeName.PARAGRAPH.getTextName().equals(childNode.getNodeName()) && childNode.getChildNodes().getLength() == 1 &&
				childNode.getFirstChild().getNodeName().equals(NodeName.HASH_TEXT.getTextName());
	}
	
	private boolean isParentParagraphNode(Node node) {
		return NodeName.PARAGRAPH.getTextName().equals(node.getNodeName()) && node.getChildNodes().getLength() == 1 &&
				node.getFirstChild().getNodeName().equals(NodeName.HASH_TEXT.getTextName());
	}
}
