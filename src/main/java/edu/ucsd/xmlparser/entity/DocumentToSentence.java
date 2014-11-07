package edu.ucsd.xmlparser.entity;

import org.springframework.data.neo4j.annotation.EndNode;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.RelationshipEntity;
import org.springframework.data.neo4j.annotation.StartNode;

@RelationshipEntity(type="HAS_SENTENCE")
public class DocumentToSentence {
	@GraphId
	private Long id; 
	
    @StartNode 
    private Document document;
    
    @EndNode
    private Sentence sentence;
    
    @SuppressWarnings("unused")
	private DocumentToSentence() {
    }

	public DocumentToSentence(Document document, Sentence sentence) {
		super();
		if(document == null) {
			throw new IllegalArgumentException("Document can not be null.");
		}
		if(sentence == null) {
			throw new IllegalArgumentException("Sentence can not be null.");
		}
		this.document = document;
		this.sentence = sentence;
	}

	public Document getDocument() {
		return document;
	}
	
	public Sentence getSentence() {
		return sentence;
	}
}
