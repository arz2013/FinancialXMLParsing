package edu.ucsd.grammar;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ParsedQuery<F extends ForClauseType<F>, W extends WhereClauseType<W>> {
	private ForClause<F> forClause;
	private WhereClause<W> whereClause;
	private ReturnClause returnClause;
	
	public ParsedQuery(ForClause<F> forClause, WhereClause<W> whereClause, ReturnClause returnClause) {
		this.forClause = forClause;
		this.whereClause = whereClause;
		this.returnClause = returnClause;
	}

	public ForClause<F> getForClause() {
		return forClause;
	}

	public WhereClause<W> getWhereClause() {
		return whereClause;
	}

	public ReturnClause getReturnClause() {
		return returnClause;
	}

	public void validate() throws ValidationException {
		validateForClause();
		
	}

	private void validateForClause() {
		Set<F> clauses = this.getForClause().getClauses();
		List<String> variables = clauses.stream().map(f -> f.getVariableAsString()).collect(Collectors.toList());
		Set<String> variablesAsSet = clauses.stream().map(f -> f.getVariableAsString()).collect(Collectors.toCollection(HashSet::new));
		// Doing the above is actually quite inefficient since we are going through the Set twice
		// We are doing it here to explore the use of lambdas in Java
		if(variablesAsSet.size() < variables.size()) {
			throw new ValidationException("Duplicate Parameters");
		}
		Set<String> parameters = clauses.stream().map(f -> f.getParameterAsString()).collect(Collectors.toCollection(HashSet::new));
		for(String parameter : parameters) {
			if(parameter != null && !variables.contains(parameter)) {
				throw new ValidationException("Undeclared variable used as parameter.");
			}
		}
	}
}
