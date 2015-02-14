package edu.ucsd.xmlparser.entity;

import java.util.HashSet;
import java.util.Set;

import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.support.index.IndexType;

@NodeEntity
public class CValueNode {
	@GraphId 
	private Long id;
	
	@Indexed(indexType=IndexType.FULLTEXT, indexName = "cvalue_text")
	private String text;
	
	private Double cValue;
	
	private Integer frequency;

	private String referenceType;
	
	private Set<Long> sectionIds = new HashSet<Long>();
	private Set<Long> sentenceIds = new HashSet<Long>();
	
	@SuppressWarnings("unused")
	private CValueNode() {
	}
	
	public CValueNode(String text, Integer frequency, Double cValue, ReferenceType type, Set<Long> sectionIds,
			Set<Long> sentenceIds) {
		this.text = text;
		this.frequency = frequency;
		this.cValue = cValue;
		this.referenceType = type.getFriendlyString();
		this.sectionIds.addAll(sectionIds);
		this.sentenceIds.addAll(sentenceIds);
	}

	public Long getId() {
		return id;
	}

	public String getText() {
		return text;
	}

	public Double getcValue() {
		return cValue;
	}

	public Integer getFrequency() {
		return frequency;
	}

	public String getReferenceType() {
		return referenceType;
	}

	public Set<Long> getSectionIds() {
		return sectionIds;
	}

	public Set<Long> getSentenceIds() {
		return sentenceIds;
	}
}
