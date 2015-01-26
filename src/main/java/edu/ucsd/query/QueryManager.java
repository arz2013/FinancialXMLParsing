package edu.ucsd.query;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import edu.ucsd.grammar.ParsedQuery;
import edu.ucsd.grammar.VariableAssignment;
import edu.ucsd.query.function.Function;
import edu.ucsd.query.function.ShortestPhrase;
import edu.ucsd.system.SystemApplicationContext;

public class QueryManager {
	public QueryResult executeQuery(ParsedQuery parsedQuery) {
		// This is where we are going to store the result of execution of various intermediate function
		Map<String, Object> varToResult = new HashMap<String, Object>();
		parsedQuery.fillInConstraint(varToResult);	
		// We execute each function from the for clause to the where clause
		// Once all functions have been executed, it must be the case that we have the final result
		Set<VariableAssignment> variableAssignments = parsedQuery.allForClauseFunctions();
		for(VariableAssignment va : variableAssignments) {
			if(va.getFunctionName().equals(ShortestPhrase.FUNCTION_NAME)) {
				Function<VariableAssignment, ShortestPhrase.ShortestPhraseResult> sp = (Function<VariableAssignment, ShortestPhrase.ShortestPhraseResult>)SystemApplicationContext.getApplicationContext().getBean("shortestPhraseFunction");
				ShortestPhrase.ShortestPhraseResult spr = sp.evaluate(va, parsedQuery);
				varToResult.put(va.getVariableName(), spr.getText());
			} else {
				throw new IllegalArgumentException("Unrecognized function name");
			}
		}
		
		return null;
	}
}
