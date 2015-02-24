package edu.ucsd.questionanswering;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.springframework.data.neo4j.support.Neo4jTemplate;

import edu.ucsd.xmlparser.entity.ApplicationRelationshipType;
import edu.ucsd.xmlparser.entity.NeTags;
import edu.ucsd.xmlparser.entity.Word;

public class NNSSentenceFormHandler implements SentenceFormHandler {
	@Inject
	private Neo4jTemplate template;
	
	@Override
	public Answer handleWord(Word word) {
		List<String> answers = new ArrayList<String>();
		Node node = template.getNode(word.getId());
		Iterator<Relationship> relationships = node.getRelationships(Direction.OUTGOING, ApplicationRelationshipType.WORD_DEPENDENCY).iterator();
		while(relationships.hasNext()) {
			Relationship rel = relationships.next();
			if("prep_of".equals(rel.getProperty("dependency"))) {
				Node endNode = rel.getEndNode();
				if(NeTags.isOrganizationOrPerson((String)endNode.getProperty("neTag"))) {
					answers.add((String)endNode.getProperty("text"));
				} 
				Iterator<Relationship> rels = endNode.getRelationships(Direction.OUTGOING, ApplicationRelationshipType.WORD_DEPENDENCY).iterator();
				while(rels.hasNext()) {
					Relationship singleRel = rels.next();
					if("conj_and".equals(singleRel.getProperty("dependency")) || "nn".equals(singleRel.getProperty("dependency"))) {
						Node endN = singleRel.getEndNode();
						if(NeTags.isOrganizationOrPerson((String)endN.getProperty("neTag"))) {
							answers.add((String)endN.getProperty("text"));
						}
					} 
				}
			} 
		}
		
		return new ListAnswer(answers);
	}

}
