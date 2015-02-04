package edu.ucsd.grammar;

class WordConstraint implements WhereClauseType<WordConstraint> {
	private String variableName;
	private String variableValue;
	private String variableContext;

	public WordConstraint(String variableName, String variableValue) {
		this(variableName, variableValue, null);
	}
	
	public WordConstraint(String variableName, String variableValue, String variableContext) {
		this.variableName = variableName;
		this.variableValue = variableValue.replace("'", "").trim();
		this.variableContext = variableContext;
	}

	@Override
	public String getVariableName() {
		return this.variableName;
	}
	
	@Override
	public String getFunctionParameter() {
		return null;
	}

	@Override
	public String getVariableValue() {
		return this.variableValue;
	}
	
	public String getVariableContext() {
		return this.variableContext;
	}
	
	@Override
	public boolean usesVariableName(String variableName) {
		return variableName.equals(this.variableName);
	}
}
