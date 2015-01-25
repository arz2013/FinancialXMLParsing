package edu.ucsd.grammar;

class WordConstraint implements WhereClauseType<WordConstraint> {
	private String variableName;
	private String variableValue;

	public WordConstraint(String variableName, String variableValue) {
		this.variableName = variableName;
		this.variableValue = variableValue;;
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
	
	@Override
	public boolean usesVariableName(String variableName) {
		return variableName.equals(this.variableName);
	}
}
