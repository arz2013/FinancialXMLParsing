package edu.ucsd.grammar;

public interface WhereClauseType<T extends WhereClauseType<T>> {
	String getVariableName();
	String getFunctionParameter();
	default String getFunctionName() {
		return null;
	}
	boolean usesVariableName(String variableName);
}
