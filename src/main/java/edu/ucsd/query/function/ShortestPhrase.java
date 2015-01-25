package edu.ucsd.query.function;

import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.ucsd.grammar.ParsedQuery;
import edu.ucsd.grammar.VariableAssignment;
import edu.ucsd.query.dao.QueryFunctionDao;
import edu.ucsd.xmlparser.entity.Word;

public class ShortestPhrase implements Function<VariableAssignment, ShortestPhrase.ShortestPhraseResult> {
	public final static String FUNCTION_NAME = "shortest_term_starting_with";
	
	private final static Logger logger = LoggerFactory.getLogger(ShortestPhrase.class);
	
	@Inject
	private QueryFunctionDao queryFunctionDao;
	
	public class ShortestPhraseResult {
		private String text;
		
		public ShortestPhraseResult(String text) {
			this.text = text;
		}

		public String getText() {
			return text;
		}
	}
	
	@Override
	public ShortestPhraseResult evaluate(VariableAssignment variableAssignment, ParsedQuery query) {
		if(!variableAssignment.getFunctionName().equals(FUNCTION_NAME)) {
			throw new IllegalArgumentException("Wrong function argument"); 
		}
		
		String parameterValue = query.findParameterValue(variableAssignment.getArgument());
		List<Word> words = queryFunctionDao.getWord(parameterValue);
		return new ShortestPhraseResult(lookForShortestPhrase(words));
	}

	private String lookForShortestPhrase(List<Word> words) {
		String shortestPhrase = null;
		
		for(Word word : words) {
			
		}
		
		return shortestPhrase;
	}

}
