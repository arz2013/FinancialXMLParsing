package edu.ucsd.xmlparser;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.DocumentBuilder; 
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import edu.ucsd.xmlparser.filter.NodeFilter;
import edu.ucsd.xmlparser.filter.NodeFilterList;
import edu.ucsd.xmlparser.filter.NodeNameFilter;
import edu.ucsd.xmlparser.filter.WhitespaceNodeFilter;
import edu.ucsd.xmlparser.util.NodeUtils;

public class FinancialXMLParser {
	private static NodeFilterList nodeFilterList;
	
	static {
		NodeFilter whitespaceFilter = new WhitespaceNodeFilter();
		NodeFilter xpacketFilter = new NodeNameFilter("xpacket");
		NodeFilter xFilter = new NodeNameFilter("x:xmpmeta");
		NodeFilter figureFilter = new NodeNameFilter("Figure");
		
		nodeFilterList = new NodeFilterList(Arrays.asList(whitespaceFilter, xpacketFilter, xFilter, figureFilter));
	}
	
	public static void main(String[] args) throws Exception {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder(); 
		Document doc = db.parse(new File(FinancialXMLParser.class.getClassLoader().getResource("sample-financial.xml").getFile()));
		NodeList children = doc.getChildNodes();
		for(int i = 0; i < children.getLength(); i++) {
			Node node = children.item(i);
			if("TaggedPDF-doc".equals(node.getNodeName())) {
				NodeList childrenNodes = node.getChildNodes();
				if(!nodeFilterList.exclude(node)) {
					NodeUtils.printNodeName(node);
				}
				
				for(int j = 0; j < childrenNodes.getLength(); j++) {
					visitChildNode(childrenNodes.item(j), node, 1);
				}
			}
		}
	}
	
	/**
	 * A function that "visits" a node and recursively goes though the children of the node 
	 * 
	 * @param childNode		- a node to be visited
	 * @param parentNode	- the parent node
	 * @param level			- the level of the tree 
	 */
	private static void visitChildNode(Node childNode, Node parentNode, int level) {
		if(!nodeFilterList.exclude(childNode)) {
			NodeUtils.printNodeDetails(childNode, level);
		}
		
		NodeList childrenNodes = childNode.getChildNodes();
		for(int j = 0; j < childrenNodes.getLength(); j++) {
			visitChildNode(childrenNodes.item(j), childNode, level+1);
		}
	}
}
