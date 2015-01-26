package edu.ucsd.query.function;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.transaction.annotation.Transactional;

import edu.ucsd.grammar.ParsedQuery;
import edu.ucsd.grammar.VariableAssignment;
import edu.ucsd.query.dao.QueryFunctionDao;
import edu.ucsd.xmlparser.entity.ApplicationRelationshipType;
import edu.ucsd.xmlparser.entity.Word;

public class ShortestPhrase implements Function<VariableAssignment, ShortestPhrase.ShortestPhraseResult> {
	public final static String FUNCTION_NAME = "shortest_term_starting_with";
	
	private final static Logger logger = LoggerFactory.getLogger(ShortestPhrase.class);
	
	private final static int MIN_LENGTH = 2;
	
	@Inject
	private QueryFunctionDao queryFunctionDao;
	
	@Inject
	private Neo4jTemplate template;
	
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
	@Transactional
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
		int length = 0;
		
		
		for(Word word : words) {
			List<String> sb = new ArrayList<String>();
			String currentPosTag = word.getPosTag();
			Node node = template.getNode(word.getId());
			buildPhraseWithConsecutiveTag(sb, node, currentPosTag);
			
			if(sb.size() >= MIN_LENGTH) {
				if(length == 0 || sb.size() < length) {
					StringBuilder stringBuilder = new StringBuilder();
					for(String s : sb) {
						stringBuilder.append(s + " ");
					}
					shortestPhrase = stringBuilder.toString().trim();
					length = sb.size();
				}
			}
		}
		
		return shortestPhrase;
	}
	
	private void buildPhraseWithConsecutiveTag(List<String> sb, Node word, String posTag) {
		if(word.getProperty("posTag").equals(posTag)) {
			sb.add((String)word.getProperty("text"));
		} else {
			return;
		}
		
		Iterable<Relationship> rels = word.getRelationships(Direction.OUTGOING, ApplicationRelationshipType.NEXT_WORD);
		if(rels.iterator().hasNext()) {
			buildPhraseWithConsecutiveTag(sb, rels.iterator().next().getEndNode(), posTag);
		}
	}

}
