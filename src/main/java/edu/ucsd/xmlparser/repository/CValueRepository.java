package edu.ucsd.xmlparser.repository;

import java.util.List;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;

import edu.ucsd.xmlparser.entity.CValueCollectionNode;
import edu.ucsd.xmlparser.entity.CValueDocumentNode;
import edu.ucsd.xmlparser.entity.CValueSectionNode;
import edu.ucsd.xmlparser.entity.NameEntityPhraseNode;

public interface CValueRepository extends GraphRepository<CValueDocumentNode> {
	@Query("match (c:CValueDocumentNode) return c")
	List<CValueDocumentNode> getAllDocumentNodes();
	
	@Query("match (c:CValueSectionNode) return c")
	List<CValueSectionNode> getAllSectionNodes();
	
	@Query("match (c:CValueCollectionNode) return c")
	List<CValueCollectionNode> getAllCValueCollectionNodes();
	
	@Query("match (n:NameEntityPhraseNode) return n")
	List<NameEntityPhraseNode> getAllNameEntityPhraseNodes();
}
