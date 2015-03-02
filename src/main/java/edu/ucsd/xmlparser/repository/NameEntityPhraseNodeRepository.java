package edu.ucsd.xmlparser.repository;

import java.util.Set;

import edu.ucsd.xmlparser.entity.NameEntityPhraseNode;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;

public interface NameEntityPhraseNodeRepository extends GraphRepository<NameEntityPhraseNode> {
	@Query("match (n:NameEntityPhraseNode) where n.documentId = {0} and n.phrase =~ '(?i).*{1}.*' return n.sentenceId")
	Set<Long> getSentenceIdsContainingNameEntity(Long documentId, String nameEntity);

	@Query("match (n:NameEntityPhraseNode) where n.phrase =~ '(?i).*{0}.*' return n.sentenceId")
	Set<Long> getSentenceIdsContainingNameEntity(String aboutParameter);
}
