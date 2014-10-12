package edu.ucsd.xmlparser.filter;

import java.util.List;

import org.w3c.dom.Node;

public class NodeFilterList extends FilterList<Node, NodeFilter> {
	
	public NodeFilterList(List<NodeFilter> parameters) {
		super(parameters);
	}
	
	@Override
	public boolean exclude(Node node) {
		boolean result = false;
		for(NodeFilter nodeFilter : super.getFilters()) {
			result = result || nodeFilter.exclude(node);
		}
		
		return result;
	}

}
