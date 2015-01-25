package edu.ucsd.query;

import org.junit.Before;
import org.junit.Test;

import edu.ucsd.grammar.ParseException;

public class QueryManagerTest {
	private QueryManager queryManager;
	
	@Before
	public void setUp() {
		queryManager = new QueryManager();
	}
	
	@Test
	public void testFindShortestTerm() throws ParseException {
		queryManager.executeQuery(Query.createQuery("for w:Word, p = shortest_term_starting_with(w) where w = 'Walt' return p").getParsedQuery());
	}
}
