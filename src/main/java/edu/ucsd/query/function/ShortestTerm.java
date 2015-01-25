package edu.ucsd.query.function;

import edu.ucsd.grammar.ParsedQuery;
import edu.ucsd.grammar.VariableAssignment;

public class ShortestTerm implements Function<VariableAssignment, ShortestTerm.ShortestTermResult> {
	public final static String FUNCTION_NAME = "shortest_term_starting_with";
	
	public class ShortestTermResult {
		private String text;
		
		public ShortestTermResult(String text) {
			this.text = text;
		}

		public String getText() {
			return text;
		}
	}
	
	@Override
	public ShortestTermResult evaluate(VariableAssignment variableAssignment, ParsedQuery query) {
		ShortestTermResult result = null;
		
		if(!variableAssignment.getFunctionName().equals(FUNCTION_NAME)) {
			throw new IllegalArgumentException("Wrong function argument"); 
		}
		
		String parameterValue = query.findParameterValue(variableAssignment.getArgument());
		
		return result;
	}

}
