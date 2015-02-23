package edu.ucsd.questionanswering;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.transaction.annotation.Transactional;

import edu.ucsd.xmlparser.entity.NeTags;
import edu.ucsd.xmlparser.repository.DocumentRepository;
import edu.ucsd.xmlparser.util.Neo4jUtils;

public class WhichQuestionHandler implements QuestionHandler {
	@Inject
	private DocumentRepository documentRepository;
	
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
			System.out.println(documentId);
		}
		
		return answer;
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
