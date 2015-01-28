package edu.ucsd.xmlparser.entity;

import java.util.HashSet;
import java.util.Set;

import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedToVia;

@NodeEntity
public class Document {
	@GraphId 
	private Long id;
	
	private String title;
	private int year;
	private int documentNumber;
	
	@Fetch
	@RelatedToVia(type="HAS_SENTENCE", direction=Direction.OUTGOING)
	private Set<DocumentToSentence> sentences = new HashSet<DocumentToSentence>();

	@SuppressWarnings("unused")
	private Document() {
	}
	
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

	public void addSentence(Sentence sentence) {
		this.sentences.add(new DocumentToSentence(this, sentence));
	}
}
