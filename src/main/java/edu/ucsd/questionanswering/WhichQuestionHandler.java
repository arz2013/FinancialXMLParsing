package edu.ucsd.questionanswering;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.transaction.annotation.Transactional;

import edu.ucsd.wordnet.LexicalUtility;
import edu.ucsd.xmlparser.entity.NeTags;
import edu.ucsd.xmlparser.entity.PhraseTypes;
import edu.ucsd.xmlparser.entity.Word;
import edu.ucsd.xmlparser.repository.DocumentRepository;
import edu.ucsd.xmlparser.repository.SentenceRepository;
import edu.ucsd.xmlparser.util.Neo4jUtils;

public class WhichQuestionHandler implements QuestionHandler, ApplicationContextAware {
	private static Map<String, String> sentenceFormTypeToHandler = new HashMap<String, String>();
	
	static {
		sentenceFormTypeToHandler.put("NNS", "nnsSentenceFormHandler");
		sentenceFormTypeToHandler.put("NN", "nnsSentenceFormHandler");
	}
	
	@Inject
	private DocumentRepository documentRepository;
	
	@Inject
	private SentenceRepository sentenceRepository;
	
	private ApplicationContext context;
	
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
		
		List<Answer> answers = new ArrayList<Answer>();
		
		if(documentId != null) {
			// Look for Verb and Noun Equivalents
			Set<String> verbAndNounEquivalents = lookForVerbAndNounEquivalents(parsedQuestion, 2);
			if(verbAndNounEquivalents.size() > 0) {
				List<Word> words = sentenceRepository.findWords(documentId, verbAndNounEquivalents); 
				for(Word word : words) {
					String handlerName = sentenceFormTypeToHandler.get(word.getPosTag());
					if(handlerName != null) {
						SentenceFormHandler sentenceHandler = SentenceFormHandler.class.cast(this.context.getBean(handlerName));
						answers.add(sentenceHandler.handleWord(word));
					} else {
						// System.out.println(word.getText() + ", " + word.getPosTag());
					}
				} 
			}
		}
		
		if(answers.size() > 0) {
			Set<String> answerRaws = new HashSet<String>();
			for(Answer ans : answers) {
				answerRaws.add(ans.asText());
			}
			answer = new SetAnswer(answerRaws);
		}
		
		return answer;
	}

	private Set<String> lookForVerbAndNounEquivalents(
			List<ParsedWord> parsedQuestion, int index) {
		Set<String> verbsAndNounEquivalents = new HashSet<String>();
		String verb = "";
		boolean likelyPresentTense = false;
		
		for(int i = index; i < parsedQuestion.size(); i++) {
			ParsedWord pw = parsedQuestion.get(i);
			if(PhraseTypes.isVB(pw.getPosTag())) {
				verb = pw.getWord();
				verbsAndNounEquivalents.add(pw.getWord());
				if(pw.isLikelyPresentTense()) {
					likelyPresentTense = true;
				}
				break;
			}
		}
		
		if(verb == null || "".equals(verb)) {
			throw new IllegalArgumentException("A Verb needs to specified for Which type questions");
		}
		
		verbsAndNounEquivalents.addAll(LexicalUtility.getNounsIncludingPluralFormsForVerb(verb));
		if(likelyPresentTense) {
			verbsAndNounEquivalents.add(LexicalUtility.getPastTense(verb));
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

	@Override
	public void setApplicationContext(ApplicationContext arg0)
			throws BeansException {
		this.context = arg0;
	}
}
