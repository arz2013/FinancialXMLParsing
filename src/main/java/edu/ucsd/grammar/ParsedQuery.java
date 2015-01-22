package edu.ucsd.grammar;

public class ParsedQuery<F extends ForClauseType<F>, W extends WhereClauseType<W>> {
	private ForClause<F> forClause;
	private WhereClause<W> whereClause;
	private ReturnClause returnClause;
	
	public ParsedQuery(ForClause<F> forClause, WhereClause<W> whereClause, ReturnClause returnClause) {
		this.forClause = forClause;
		this.whereClause = whereClause;
		this.returnClause = returnClause;
	}

	public ForClause<F> getForClause() {
		return forClause;
	}

	public WhereClause<W> getWhereClause() {
		return whereClause;
	}

	public ReturnClause getReturnClause() {
		return returnClause;
	}
}
