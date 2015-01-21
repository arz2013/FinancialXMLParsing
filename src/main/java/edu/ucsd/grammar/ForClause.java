package edu.ucsd.grammar;

import java.util.HashSet;
import java.util.Set;

public class ForClause<T extends ForClauseType<T>> {
	private Set<ForClauseType<T>> clauses = new HashSet<ForClauseType<T>>(); 
	
	public ForClause() {
	}
	
	public void addClauseType(ForClauseType<T> clauseType) {
		clauses.add(clauseType);
	}
}
