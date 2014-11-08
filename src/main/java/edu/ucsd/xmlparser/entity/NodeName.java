package edu.ucsd.xmlparser.entity;

public enum NodeName {
	PARAGRAPH("P"), HASH_TEXT("#text");
	
	String textName;
	
	NodeName(String textName) {
		this.textName = textName;
	}
	
	public String getTextName() {
		return this.textName;
	}
}
