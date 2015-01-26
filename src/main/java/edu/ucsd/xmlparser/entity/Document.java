package edu.ucsd.xmlparser.entity;

import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.NodeEntity;

@NodeEntity
public class Document {
	@GraphId 
	private Long id;
	
	private String title;
	private int year;
	private int documentNumber;
	
	public Document(String title, int year, int documentNumber) {
		if(title == null) {
			throw new IllegalArgumentException("Title can not be null");
		}
		
		this.title = title;
		this.year = year;
		this.documentNumber = documentNumber;
	}

	public String getTitle() {
		return title;
	}

	public int getYear() {
		return year;
	}

	public int getDocumentNumber() {
		return documentNumber;
	}

	public Long getId() {
		return id;
	}
}
