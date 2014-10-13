package edu.ucsd.xmlparser;

import java.io.File;
import java.util.Arrays;

import javax.xml.parsers.DocumentBuilder; 
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import edu.ucsd.xmlparser.filter.NodeFilter;
import edu.ucsd.xmlparser.filter.NodeFilterList;
import edu.ucsd.xmlparser.filter.NodeFilterListResult;
import edu.ucsd.xmlparser.filter.NodeNameFilter;
import edu.ucsd.xmlparser.filter.WhitespaceNodeFilter;
import edu.ucsd.xmlparser.util.NodeUtils;

public class FinancialXMLParser {
	private NodeFilterList nodeFilterList;
	
	public FinancialXMLParser() {
		NodeFilter whitespaceFilter = new WhitespaceNodeFilter();
		NodeFilter xpacketFilter = new NodeNameFilter("xpacket");
		NodeFilter xFilter = new NodeNameFilter("x:xmpmeta");
		NodeFilter figureFilter = new NodeNameFilter("Figure");
		
		nodeFilterList = new NodeFilterList(Arrays.asList(whitespaceFilter, xpacketFilter, xFilter, figureFilter));
	}
	
	
	public void parse(File file) throws Exception {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder(); 
		Document doc = db.parse(file);
		
		NodeList children = doc.getChildNodes();
		for(int i = 0; i < children.getLength(); i++) {
			Node node = children.item(i);
			if("TaggedPDF-doc".equals(node.getNodeName())) {
				NodeFilterListResult filterResult = nodeFilterList.exclude(node);
				if(!filterResult.isExcludeNode()) {
					NodeUtils.printNodeName(node);
				}
				
				if(!filterResult.isSkipChildren()) {
					NodeList childrenNodes = node.getChildNodes();

					for(int j = 0; j < childrenNodes.getLength(); j++) {
						visitChildNode(childrenNodes.item(j), node, 1);
					}
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
	private void visitChildNode(Node childNode, Node parentNode, int level) {
		NodeFilterListResult filterResult = nodeFilterList.exclude(childNode);
		
		if(!filterResult.isExcludeNode()) {
			NodeUtils.printNodeDetails(childNode, level);
		}
		
		if(!filterResult.isSkipChildren()) {
			NodeList childrenNodes = childNode.getChildNodes();
			for(int j = 0; j < childrenNodes.getLength(); j++) {
				visitChildNode(childrenNodes.item(j), childNode, level+1);
			}
		}
	}
}
