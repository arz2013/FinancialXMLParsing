package edu.ucsd.xmlparser.repository;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;

import edu.ucsd.xmlparser.entity.Document;

public interface DocumentRepository extends GraphRepository<Document> {
	@Query("start d = node:__types__(className=\"_Document\") where d.title = {0} and d.year = {1} and d.documentNumber = {2} return d")
	public Document getDocumentByTitleYearAndNumber(String title, int year, int documentNumber);
}
