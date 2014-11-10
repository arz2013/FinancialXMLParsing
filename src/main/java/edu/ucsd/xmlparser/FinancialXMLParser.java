package edu.ucsd.xmlparser;

import java.io.File;

import javax.inject.Inject;
import javax.xml.parsers.DocumentBuilder; 
import javax.xml.parsers.DocumentBuilderFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import edu.ucsd.nlpparser.StanfordParser;
import edu.ucsd.xmlparser.entity.ApplicationRelationshipType;
import edu.ucsd.xmlparser.entity.NodeName;
import edu.ucsd.xmlparser.entity.Sentence;
import edu.ucsd.xmlparser.util.GraphDatabaseUtils;

public class FinancialXMLParser {
	@Inject
	private GraphDatabaseUtils graphDatabaseUtils;
	
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
		visit(documentGraphNode, documentNode);
	}
	
	private void visit(org.neo4j.graphdb.Node graphNode, Node xmlNode) {
		NodeList children = xmlNode.getChildNodes();
		org.neo4j.graphdb.Node previousGraphChildNode = null;
		
		for(int i = 0; i < children.getLength(); i++) {			
			Node childNode = children.item(i);
			// Check for Paragraph Node for special handling
			boolean isParagraph = isParagraphTextNode(childNode);
			
			if(!isParagraph) {
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
				visit(graphChildNode, childNode);
			} else {
				Node hashText = childNode.getFirstChild();
				System.out.println("Sentence Number : " + this.sentenceNumber + " with value : " + hashText.getNodeValue());
				Sentence sentence = stanfordParser.parseAndLoad(hashText.getNodeValue(), this.sentenceNumber);
				graphDatabaseUtils.createRelationship(graphNode, graphDatabaseUtils.getNode(sentence), ApplicationRelationshipType.HAS_CHILD);
				this.sentenceNumber++;
			}
		}
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
}
