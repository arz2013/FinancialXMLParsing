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
		Set<String> variables = validateForClause();
		validateWhereClause(variables);
		validateReturnClause(variables);
	}

	private void validateReturnClause(Set<String> variables) {
		Set<W> clauses = this.getWhereClause().getClauses();
		List<String> variableNames = clauses.stream().filter(w -> w.getVariableName() != null).map(w -> w.getVariableName()).collect(Collectors.toList());
		Set<String> variableNamesAsSet = variableNames.stream().collect(Collectors.toCollection(HashSet::new));
		if(variableNamesAsSet.size() < variableNames.size()) {
			throw new ValidationException("Duplicate Parameters in where clause.");
		}
		for(String variableName : variableNamesAsSet) {
			if(!variables.contains(variableName)) {
				throw new ValidationException("Undeclared variable in where clause.");
			}
		}
		Set<String> parameterNames = clauses.stream().filter(w -> w.getFunctionParameter() != null).map(w -> w.getFunctionParameter()).collect(Collectors.toCollection(HashSet::new));
		for(String parameterName : parameterNames) {
			if(!variables.contains(parameterName)) {
				throw new ValidationException("Undeclared variable used as parameter in where clause.");
			}
		}
	}

	private void validateWhereClause(Set<String> variables) {
		// TODO Auto-generated method stub
		
	}

	private Set<String> validateForClause() {
		// Validate For Clause
		Set<F> clauses = this.getForClause().getClauses();
		List<String> variables = clauses.stream().filter(f -> f.getVariableAsString() != null).map(f -> f.getVariableAsString()).collect(Collectors.toList());
		Set<String> variablesAsSet = clauses.stream().map(f -> f.getVariableAsString()).collect(Collectors.toCollection(HashSet::new));
		// Doing the above is actually quite inefficient since we are going through the Set twice
		// We are doing it here to explore the use of lambdas in Java
		if(variablesAsSet.size() < variables.size()) {
			throw new ValidationException("Duplicate Parameters in for clause.");
		}
		Set<String> parameters = clauses.stream().filter(f -> f.getParameterAsString() != null).map(f -> f.getParameterAsString()).collect(Collectors.toCollection(HashSet::new));
		for(String parameter : parameters) {
			if(!variables.contains(parameter)) {
				throw new ValidationException("Undeclared variable used as parameter in for clause.");
			}
		}
		
		return variablesAsSet;
	}
}
