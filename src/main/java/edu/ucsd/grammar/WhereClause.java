package edu.ucsd.grammar;

import java.util.HashSet;
import java.util.Set;

public class WhereClause<T extends WhereClauseType<T>> {
	private Set<T> clauseTypes = new HashSet<T>();
	
	public WhereClause() {
	}
	
	public void addClauseType(T clauseType) {
		clauseTypes.add(clauseType);
	}
}
