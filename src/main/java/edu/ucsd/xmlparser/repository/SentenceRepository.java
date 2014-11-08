package edu.ucsd.xmlparser.repository;

import java.util.List;
import java.util.Map;

import org.neo4j.graphdb.Node;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;

import edu.ucsd.xmlparser.entity.NeTags;
import edu.ucsd.xmlparser.entity.Sentence;
import edu.ucsd.xmlparser.entity.Word;

public interface SentenceRepository extends GraphRepository<Sentence> {
	@Query("start s = node:__types__(className=\"_Sentence\") where s.text = {0} return s")
	public Sentence getSentenceByText(String text);

	@Query("start s = node:__types__(className=\"_Sentence\"), w = node:__types__(className=\"_Word\") match (s)-[:HAS_WORD]->(w) where s.text = {0} return w")
	public List<Word> getWordsBySentenceText(String text);
	
	@Query("start w = node:__types__(className=\"_Word\"), w1 = node:__types__(className=\"_Word\") match (w)-[h:WORD_DEPENDENCY]->(w1) where id(w) = {0} and id(w1) = {1} return h.dependency")
	public String getRelationShip(Long startWordId, Long endWordId);

	@Query("start d = node:__types__(className=\"_Document\"), s = node:__types__(className=\"_Sentence\") match (d)-[:HAS_SENTENCE]->(s) where id(d) = {0} return s order by s.sNum")
	public List<Sentence> getSentencesBasedOnDocument(Long documentId);
	
	@Query("start w = node:__types__(className=\"_Word\") where w.text <> \"ROOT\" and w.neTag = {0} return w")
	public List<Word> getWordsWithNeTag(String neTag);
	
	@Query("start s=node:__types__(className=\"_Sentence\"), w=node:__types__(className=\"_Word\") match (s)-[:HAS_WORD]->(w) where s.sNum = {0} and (w.position >= {1} and w.position < {2}) return w;")
	public List<Node> getWordsFromTo(int sentenceNumber, int wordPositionFrom, int wordPositionTo);

	@Query("start s = node:__types__(className=\"_Sentence\"), w = node:__types__(className=\"_Word\") match (s)-[:HAS_WORD]->(w) where w.text <> \"ROOT\" and w.neTag = {0} return s.sNum as sentenceNumber, collect(w) as words")
	public Iterable<Map<String, Object>> getWordsKeyedBySentenceNumberWithSpecificNeTag(NeTags neTag);

	@Query("start s=node:__types__(className=\"_Sentence\"), w=node:__types__(className=\"_Word\") match (s)-[:HAS_WORD]->(w) where s.sNum = {0} and w.position = {1} return w;")
	public Node getWord(int sentenceNumber, int startIndex);
}
