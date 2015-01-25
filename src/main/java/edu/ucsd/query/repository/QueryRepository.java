package edu.ucsd.query.repository;

import java.util.List;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;

import edu.ucsd.xmlparser.entity.Word;

public interface QueryRepository extends GraphRepository {
	@Query("match (w:Word{text:{0}}) return w")
	public List<Word> getWordsByText(String text);
}
