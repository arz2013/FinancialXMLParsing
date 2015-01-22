package edu.ucsd.grammar;

import java.util.HashSet;
import java.util.Set;

public class ForClause<T extends ForClauseType<T>> {
	private Set<T> clauses = new HashSet<T>(); 
	
	public ForClause() {
	}
	
	public void addClauseType(T clauseType) {
		clauses.add(clauseType);
	}
}
