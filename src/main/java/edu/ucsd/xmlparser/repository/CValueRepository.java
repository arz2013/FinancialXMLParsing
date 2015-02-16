package edu.ucsd.xmlparser.repository;

import java.util.List;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;

import edu.ucsd.xmlparser.entity.CValueDocumentNode;

public interface CValueRepository extends GraphRepository<CValueDocumentNode> {
	@Query("match (c:CValueDocumentNode) return c")
	List<CValueDocumentNode> getAllDocumentNodes();
}
