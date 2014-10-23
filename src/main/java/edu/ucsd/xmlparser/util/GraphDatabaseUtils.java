package edu.ucsd.xmlparser.util;

import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.springframework.beans.factory.annotation.Autowired;
import org.w3c.dom.NamedNodeMap;

import edu.ucsd.xmlparser.entity.NodeAttributes;

public class GraphDatabaseUtils {
	@Autowired
	private GraphDatabaseService graphDatabaseService;
	
	/**
	 * Converts an DOM XML Node into a Neo4J Node
	 * - The XML Node's name will be converted into the Label of the Neo4J Node
	 * - The XML Node's attributes will be converted into properties of the Neo4J Node
	 * - And finally, the XML Node's value will also be converted into a property of the Neo4J Node
	 * 
	 * @param xmlNode - the XML Node's that's going to be transformed into a Neo4J Node
	 * @return a Neo4J representation of the XML Node
	 */
	public Node toGraphNode(org.w3c.dom.Node xmlNode) {
		Node graphNode = graphDatabaseService.createNode();
		// Associate a Label
		Label nodeNameLabel = DynamicLabel.label(xmlNode.getNodeName());
		graphNode.addLabel(nodeNameLabel);
		// Associate all the attributes
		NamedNodeMap xmlNodeAttributes = xmlNode.getAttributes();
		for(int i = 0; i < xmlNodeAttributes.getLength(); i++) {
			org.w3c.dom.Node attribute = xmlNodeAttributes.item(i);
			graphNode.setProperty(attribute.getNodeName(), attribute.getNodeValue());
		}
		// Associate a value even if it's null
		graphNode.setProperty(NodeAttributes.VALUE.getAttributeName(), xmlNode.getNodeValue());
		
		return graphNode;
	}
	
	public Relationship createRelationship(Node start, Node to, RelationshipType hasChild) {
		return start.createRelationshipTo(to, hasChild);
	}
}
