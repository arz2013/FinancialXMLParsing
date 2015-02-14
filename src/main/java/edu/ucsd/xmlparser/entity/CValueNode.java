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
	
	private Set<Long> referenceIds = new HashSet<Long>();
	
	@SuppressWarnings("unused")
	private CValueNode() {
	}
	
	public CValueNode(String text, Integer frequency, Double cValue, ReferenceType type, Set<Long> referenceIds) {
		this.text = text;
		this.frequency = frequency;
		this.cValue = cValue;
		this.referenceType = type.getFriendlyString();
		this.referenceIds.addAll(referenceIds);
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
}
