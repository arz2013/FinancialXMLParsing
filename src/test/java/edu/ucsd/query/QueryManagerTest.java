package edu.ucsd.query;

import java.util.Set;

import org.junit.Assert;
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
		QueryResult<String> result = queryManager.executeQuery(Query.createQuery("for w:Word, p = shortest_term_starting_with(w) where w = 'Walt' return p").getParsedQuery(), String.class);
		Assert.assertEquals(result.getResult(), "Walt Disney");
	}
	
	@Test
	public void testSentenceShortestTerm() throws ParseException {
		QueryResult<Set> result = queryManager.executeQuery(Query.createQuery("for s:Sentence, w:Word, p = shortest_term_starting_with(w) where w = 'Walt' and s.contains(p) return s").getParsedQuery(), Set.class);
		Assert.assertEquals(6, result.getResult().size());
		for(String res : (Set<String>)result.getResult()) {
			System.out.println(res);
		}
	}
	
	@Test
	public void testReturnWord() throws ParseException {
		QueryResult<String> result = queryManager.executeQuery(Query.createQuery("for w:Word where w = 'Walt' return w").getParsedQuery(), String.class);
		Assert.assertEquals(result.getResult(), "Walt");
	}
}
