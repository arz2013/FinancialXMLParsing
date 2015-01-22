package edu.ucsd.query;

import static org.junit.Assert.*;

import org.junit.Test;

import edu.ucsd.grammar.ParseException;
import edu.ucsd.grammar.ValidationException;

public class QueryTest {

	@Test
	public void testValidQuery() throws ParseException {
		Query query = Query.createQuery("for w:Word , p = shortest_term_starting_with(w), s:Sentence where w = 'Walt' and s.contains(w) return s");
		assertTrue("Query is not null indicating successful parse.", query != null);
	}
	
	@Test(expected=ValidationException.class)
	public void testUndeclaredParametersInQuery() throws ParseException {
		Query query = Query.createQuery("for w:Word , p = shortest_term_starting_with(q), s:Sentence where w = 'Walt' and s.contains(w) return s");
		
	}
}
