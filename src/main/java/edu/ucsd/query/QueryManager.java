package edu.ucsd.query;

import edu.ucsd.grammar.ParsedQuery;
import edu.ucsd.system.SystemApplicationContext;

public class QueryManager {
	public QueryResult executeQuery(ParsedQuery parsedQuery) {
		QueryExecutor queryExecutor = (QueryExecutor)SystemApplicationContext.getApplicationContext().getBean("queryExecutor");
		
		return null;
	}
}
