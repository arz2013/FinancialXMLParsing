package edu.ucsd.questionanswering;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.transaction.annotation.Transactional;

import edu.ucsd.xmlparser.entity.ApplicationRelationshipType;
import edu.ucsd.xmlparser.entity.NeTags;
import edu.ucsd.xmlparser.entity.PhraseTypes;
import edu.ucsd.xmlparser.entity.Word;
import edu.ucsd.xmlparser.repository.DocumentRepository;
import edu.ucsd.xmlparser.repository.SentenceRepository;
import edu.ucsd.xmlparser.util.Neo4jUtils;

public class WhichQuestionHandler implements QuestionHandler {
	@Inject
	private DocumentRepository documentRepository;
	
	@Inject
	private SentenceRepository sentenceRepository;
	
	@Inject
	private Neo4jTemplate template;
	
	@Override
	@Transactional
	public Answer answerQuestion(List<ParsedWord> parsedQuestion) {
		Answer answer = new NoAnswer();
		
		// This is what we are looking for
		String answerNameEntityType = parsedQuestion.get(1).getLemma();
		if(!NeTags.isValid(answerNameEntityType))  {
			throw new IllegalArgumentException("Unsupported argument to question starting with which : " + answerNameEntityType);
		}
		
		// Look for Actor
		String actor = lookForActor(parsedQuestion, 2);
		Long documentId = lookForMostProbablyDocument(actor);
		
		if(documentId != null) {
			// Look for Verb and Noun Equivalents
			Set<String> verbAndNounEquivalents = lookForVerbAndNounEquivalents(parsedQuestion, 2);
			if(verbAndNounEquivalents.size() > 0) {
				List<Word> words = sentenceRepository.findWords(documentId, verbAndNounEquivalents); 
				for(Word word : words) {
					if(PhraseTypes.isNNS(word.getPosTag())) {
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
									if("conj_and".equals(singleRel.getProperty("dependency"))) {
										Node endN = singleRel.getEndNode();
										if(NeTags.isOrganizationOrPerson((String)endN.getProperty("neTag"))) {
											answers.add((String)endN.getProperty("text"));
										}
									}
								}
							}
						}
						
						answer = new ListAnswer(answers);
					}
				}
			}
		}
		
		return answer;
	}

	private Set<String> lookForVerbAndNounEquivalents(
			List<ParsedWord> parsedQuestion, int index) {
		Set<String> verbsAndNounEquivalents = new HashSet<String>();
		String verb = "";
		
		for(int i = index; i < parsedQuestion.size(); i++) {
			ParsedWord pw = parsedQuestion.get(i);
			if(PhraseTypes.isVB(pw.getPosTag())) {
				verb = pw.getWord();
				verbsAndNounEquivalents.add(pw.getWord());
				break;
			}
		}
		
		if("acquire".equals(verb)) {
			verbsAndNounEquivalents.add("acquired");
			verbsAndNounEquivalents.add("acquisition");
			verbsAndNounEquivalents.add("acquisitions");
		}
		
		return verbsAndNounEquivalents;
	}

	private Long lookForMostProbablyDocument(String actor) {
		Long docIdResult = null;
		Long count = null;
		
		Iterator<Map<String, Object>> documentIdToCount = documentRepository.getCount(Neo4jUtils.likeInput(actor)).iterator();
		while(documentIdToCount.hasNext()) {
			Map<String, Object> docIdAndCount = documentIdToCount.next();
			
			if(docIdResult == null || (Long.class.cast(docIdAndCount.get("count")) > count)) {
				docIdResult = Long.class.cast(docIdAndCount.get("documentId"));
				count = Long.class.cast(docIdAndCount.get("count"));
			} 
		}	
		
		return docIdResult;
	}

	private String lookForActor(List<ParsedWord> parsedQuestion, int start) {
		String actor = null;
		
		for(int i = start; i < parsedQuestion.size(); i++) {
			ParsedWord pw = parsedQuestion.get(i);
			if(NeTags.isOrganizationOrPerson(pw.getNeTag())) {
				if(actor == null) {
					actor = pw.getWord();
				} else {
					actor += " ";
					actor += pw.getWord();
				}
			}
		}
		
		if(actor == null) {
			for(int i = start; i < parsedQuestion.size(); i++) {
				ParsedWord pw = parsedQuestion.get(i);
				if("NNP".equals(pw.getPosTag())) {
					if(actor == null) {
						actor = pw.getWord();
					} else {
						actor += " ";
						actor += pw.getWord();
					}
				}
			}	
		} 
		
		return actor.trim();
	}
}
