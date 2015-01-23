package edu.ucsd.query;

import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import edu.ucsd.grammar.ParseException;
import edu.ucsd.grammar.TokenMgrError;
import edu.ucsd.grammar.ValidationException;

public class QueryTest {
	@Rule
	public ExpectedException undeclaredParametersInForClause = ExpectedException.none();
	
	@Rule
	public ExpectedException duplicateParametersInForClause = ExpectedException.none();
	
	@Rule
	public ExpectedException undeclaredParametersInWhereClause = ExpectedException.none();
	
	@Rule
	public ExpectedException duplicateParametersInWhereClause = ExpectedException.none();
	
	@Rule
	public ExpectedException undeclaredVariableUsedAsParameterInWhereClause = ExpectedException.none();

	@Test
	public void testValidQuery() throws ParseException {
		Query query = Query.createQuery("for w:Word , p = shortest_term_starting_with(w), s:Sentence where w = 'Walt' and s.contains(w) return s");
		assertTrue("Query is not null indicating successful parse.", query != null);
	}
	
	@Test
	public void testUndeclaredParametersInForClause() throws ParseException {
		undeclaredParametersInForClause.expect(ValidationException.class);
		undeclaredParametersInForClause.expectMessage("Undeclared variable used as parameter in for clause.");
		Query.createQuery("for w:Word , p = shortest_term_starting_with(q), s:Sentence where w = 'Walt' and s.contains(w) return s");
	}
	
	@Test
	public void testDuplicateParametersInForClause() throws ParseException {
		duplicateParametersInForClause.expect(ValidationException.class);
		duplicateParametersInForClause.expectMessage("Duplicate Parameters in for clause.");
		Query.createQuery("for w:Word , w:Sentence, p = shortest_term_starting_with(w), s:Sentence where w = 'Walt' and s.contains(w) return s");
	}
	
	@Test
	public void testUndeclaredVariablesInWhereClause() throws ParseException {
		undeclaredParametersInWhereClause.expect(ValidationException.class);
		undeclaredParametersInWhereClause.expectMessage("Undeclared variable in where clause");
		Query.createQuery("for w:Word , p = shortest_term_starting_with(w), s:Sentence where w = 'Walt' and w1 = 'Disney' and s.contains(w) return s");
	}
	
	@Test
	public void testDuplicateVariablesInWhereClause() throws ParseException {
		duplicateParametersInWhereClause.expect(ValidationException.class);
		duplicateParametersInWhereClause.expectMessage("Duplicate Parameters in where clause.");
		Query.createQuery("for w:Word , p = shortest_term_starting_with(w), s:Sentence where w = 'Walt' and w = 'Disney' and s.contains(w) return s");
	}
	
	@Test
	public void testUndeclaredParametersInWhereClause() throws ParseException {
		undeclaredVariableUsedAsParameterInWhereClause.expect(ValidationException.class);
		undeclaredVariableUsedAsParameterInWhereClause.expectMessage("Undeclared variable used as parameter in where clause.");
		Query.createQuery("for w:Word , p = shortest_term_starting_with(w), s:Sentence where w = 'Walt' and s.contains(w1) return s");
	}
	
	@Test(expected=TokenMgrError.class)
	public void testParameterValueMustBeSingleString() throws ParseException {
		Query.createQuery("for w:Word , p = shortest_term_starting_with(w), s:Sentence where w = 'Walt Disney' and s.contains(w) return s");		
	}
}
