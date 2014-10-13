package edu.ucsd.xmlparser.entity;

import java.util.HashMap;
import java.util.Map;

public class GraphNode {	
	private Map<String, Object> propertyNameToValues = new HashMap<String, Object>();
	
	public void setProperty(String propertyName, Object propertyValue) {
		if(propertyName == null) {
			throw new IllegalArgumentException("Property Name for a Node can not be null.");
		}
		
		this.propertyNameToValues.put(propertyName, propertyValue);
	}
}
