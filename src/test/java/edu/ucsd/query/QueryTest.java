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
	
	@Rule
	public ExpectedException unusedParameterInWhereClause = ExpectedException.none();
	
	@Rule
	public ExpectedException undeclaredVariableInReturnClause = ExpectedException.none();
	
	@Rule
	public ExpectedException invalidAssignment = ExpectedException.none();

	@Test
	public void testValidQuery() throws ParseException {
		Query query = Query.createQuery("for w:Word , p = shortest_term_starting_with(w), s:Sentence where w = 'Walt' and s.contains(w) return s");
		assertTrue("Query is not null indicating successful parse.", query != null);
		Query.createQuery("for w:Word where w = 'Walt' return w");
		
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
	public void testParameterValueMustBeSingleStringInWhereClause() throws ParseException {
		Query.createQuery("for w:Word , p = shortest_term_starting_with(w), s:Sentence where w = 'Walt Disney' and s.contains(w) return s");		
	}
	
	@Test
	public void testUnusedParameterInWhereClause1() throws ParseException {
		unusedParameterInWhereClause.expect(ValidationException.class);
		unusedParameterInWhereClause.expectMessage("Parameter is declared and set but does not contribute to the return statement.");
		Query.createQuery("for w:Word , w1:Word, p = shortest_term_starting_with(w), s:Sentence where w = 'Walt' and w1 = 'Disney' and s.contains(w) return s");			
	}
	
	@Test
	public void testUnusedParameterInWhereClause2() throws ParseException {
		unusedParameterInWhereClause.expect(ValidationException.class);
		unusedParameterInWhereClause.expectMessage("Parameter is declared but does not contribute to the where and return statements.");
		Query.createQuery("for w:Word , w1:Word, p = shortest_term_starting_with(w), s:Sentence where w = 'Walt' and s.contains(w) return w");			
	}
	
	@Test
	public void testInvalidAssigmentInWhereClause() throws ParseException {
		invalidAssignment.expect(ValidationException.class);
		invalidAssignment.expectMessage("Invalid assignment in where clause.");
		Query.createQuery("for s:Sentence where s = 'Walt' return s");
	}
	
	@Test
	public void testInvalidFunctionCallInWhereClause() throws ParseException {
		// Calling the contains function on a w 
	}
	
	@Test
	public void testInvalidContainsInWhereClause() throws ParseException {
		// Document can contain Sentence, Phrase and Word, but not the other way around
	}
	
	
	@Test
	public void testUndeclaredVariableInReturnClause() throws ParseException {
		this.undeclaredVariableInReturnClause.expect(ValidationException.class);
		this.undeclaredVariableInReturnClause.expectMessage("Undeclared variable in return clause.");
		Query.createQuery("for w:Word , p = shortest_term_starting_with(w), s:Sentence where w = 'Walt' and s.contains(w) return s1");
	}
}
