package edu.ucsd.grammar;

public interface ForClauseType<T extends ForClauseType<T>> {
	T getType();	
}