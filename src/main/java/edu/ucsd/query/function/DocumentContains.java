package edu.ucsd.query.function;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.inject.Inject;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.springframework.data.neo4j.support.Neo4jTemplate;

import edu.ucsd.xmlparser.entity.ApplicationRelationshipType;
import edu.ucsd.xmlparser.entity.Document;
import edu.ucsd.xmlparser.entity.Sentence;

public class DocumentContains implements Contains {
	@Inject
	private Neo4jTemplate template;
	
	@Override
	public Set<String> contains(Set<Sentence> sentences) {
		Set<String> documents = new HashSet<String>();
		for(Sentence sentence : sentences) {
			documents.add(getContainingDocument(sentence).getTitle());
		}
		return documents;
	}

	private Document getContainingDocument(Sentence sentence) {
		Node sentenceNode = template.getNode(sentence.getId());
		Iterator<Relationship> rels = sentenceNode.getRelationships(Direction.INCOMING, ApplicationRelationshipType.HAS_SENTENCE).iterator();

		return this.template.findOne(rels.next().getOtherNode(sentenceNode).getId(), Document.class);
	}

}
