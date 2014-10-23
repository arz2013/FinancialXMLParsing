package edu.ucsd.xmlparser;

import java.io.File;

import javax.xml.parsers.DocumentBuilder; 
import javax.xml.parsers.DocumentBuilderFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import edu.ucsd.xmlparser.entity.ApplicationRelationshipType;
import edu.ucsd.xmlparser.util.GraphDatabaseUtils;

public class FinancialXMLParser {
	@Autowired
	private GraphDatabaseUtils graphDatabaseUtils;
	
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
		org.neo4j.graphdb.Node documentGraphNode = graphDatabaseUtils.toGraphNode(documentNode);
		visit(documentGraphNode, documentNode);
	}
	
	private void visit(org.neo4j.graphdb.Node graphNode, Node xmlNode) {
		NodeList children = xmlNode.getChildNodes();
		org.neo4j.graphdb.Node previousGraphChildNode = null;
		
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
			visit(graphChildNode, childNode);
		}
	}
}
