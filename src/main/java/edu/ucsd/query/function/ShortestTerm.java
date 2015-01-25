package edu.ucsd.query.function;

import edu.ucsd.grammar.ParsedQuery;
import edu.ucsd.grammar.VariableAssignment;

public class ShortestTerm implements Function<VariableAssignment, String> {
	public final static String FUNCTION_NAME = "shortest_term_starting_with";
	
	@Override
	public String evaluate(VariableAssignment variableAssignment, ParsedQuery query) {
		if(!variableAssignment.getFunctionName().equals(FUNCTION_NAME)) {
			throw new IllegalArgumentException("Wrong function argument"); 
		}
		
		String parameterValue = query.findParameterValue(variableAssignment.getArgument());
		
		return null;
	}

}
