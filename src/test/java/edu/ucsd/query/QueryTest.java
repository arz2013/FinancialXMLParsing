package edu.ucsd.query;

import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import edu.ucsd.grammar.ParseException;
import edu.ucsd.grammar.ValidationException;

public class QueryTest {
	@Rule
	public ExpectedException undeclaredParameters = ExpectedException.none();
	
	@Rule
	public ExpectedException duplicateParameters = ExpectedException.none();

	@Test
	public void testValidQuery() throws ParseException {
		Query query = Query.createQuery("for w:Word , p = shortest_term_starting_with(w), s:Sentence where w = 'Walt' and s.contains(w) return s");
		assertTrue("Query is not null indicating successful parse.", query != null);
	}
	
	@Test
	public void testUndeclaredParametersInQuery() throws ParseException {
		undeclaredParameters.expect(ValidationException.class);
		undeclaredParameters.expectMessage("Undeclared variable used as parameter.");
		Query.createQuery("for w:Word , p = shortest_term_starting_with(q), s:Sentence where w = 'Walt' and s.contains(w) return s");
	}
	
	@Test
	public void testDuplicateParametersInQuery() throws ParseException {
		duplicateParameters.expect(ValidationException.class);
		duplicateParameters.expectMessage("Duplicate Parameters.");
		Query.createQuery("for w:Word , w:Sentence, p = shortest_term_starting_with(w), s:Sentence where w = 'Walt' and s.contains(w) return s");
	}
}
