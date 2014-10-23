package edu.ucsd.xmlparser;

import java.io.File;

import javax.xml.parsers.DocumentBuilder; 
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import edu.ucsd.xmlparser.util.NodeUtils;

public class FinancialXMLParser {	
	public FinancialXMLParser() {
	}
	
	/**
	 * Entry point to the whole parsing mechanism 
	 * - Utilizes a DOM Parser for now
	 * 
	 * @param file
	 * @throws Exception
	 */
	public void parse(File file) throws Exception {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder(); 
		Node documentNode = db.parse(file);
		NodeUtils.printNodeName(documentNode);
		NodeList children = documentNode.getChildNodes();
		for(int i = 0; i < children.getLength(); i++) {			
			Node node = children.item(i);
			visitChildNode(node, 1);
		}
	}
	
	/**
	 * A function that "visits" a node and recursively goes though the children of the node 
	 * 
	 * @param childNode		- a node to be visited
	 * @param parentNode	- the parent node
	 * @param level			- the level of the tree 
	 */
	private void visitChildNode(Node childNode, int level) {
		NodeUtils.printNodeName(childNode);
		NodeList childrenNodes = childNode.getChildNodes();
		for(int j = 0; j < childrenNodes.getLength(); j++) {
			visitChildNode(childrenNodes.item(j), level+1);
		}
	}
}
