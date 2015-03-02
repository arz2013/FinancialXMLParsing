package edu.ucsd.questionanswering;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

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
		Set<String> answers = new HashSet<String>();
		Node node = template.getNode(word.getId());
		Iterator<Relationship> relationships = node.getRelationships(Direction.OUTGOING, ApplicationRelationshipType.WORD_DEPENDENCY).iterator();
		while(relationships.hasNext()) {
			Relationship rel = relationships.next();
			if("prep_of".equals(rel.getProperty("dependency"))) {
				String phrase = "";
				StringBuilder sb = new StringBuilder();
				Node endNode = rel.getEndNode();
				if(NeTags.isOrganizationOrPerson((String)endNode.getProperty("neTag"))) {
					sb.append((String)endNode.getProperty("text"));
					phrase = QAUtils.getPhrase(endNode);
				} 
				
				if(phrase.equals("")) {
					Iterator<Relationship> rels = endNode.getRelationships(Direction.OUTGOING, ApplicationRelationshipType.WORD_DEPENDENCY).iterator();
					while(rels.hasNext()) {
						Relationship singleRel = rels.next();
						if("conj_and".equals(singleRel.getProperty("dependency")) || "nn".equals(singleRel.getProperty("dependency"))) {
							Node endN = singleRel.getEndNode();
							if(NeTags.isOrganizationOrPerson((String)endN.getProperty("neTag"))) {
								sb.append(" ");
								sb.append((String)endN.getProperty("text"));
								phrase = QAUtils.getPhrase(endNode);
							}
						} 
					}
				}
				
				/*
				String answer = sb.toString().trim();
				if(!"".equals(answer)) {
					answers.add(answer);
				}*/
				if(!"".equals(phrase)) {
					answers.add(phrase);
				}
			} 
		}
		
		if(answers.size() > 0) {
			return new SetAnswer(answers);
		}
		
		return new NoAnswer();
	}
}
