package edu.ucsd.grammar;

public enum VariableTypes {
	WORD("Word", true, false), 
	SENTENCE("Sentence", false, true), 
	DOCUMENT("Document", false, true),
	PHRASE("Phrase", false, false);
	
	private String friendlyString;
	private boolean acceptAssignment;
	private boolean acceptFunction;
	
	private VariableTypes(String friendlyString, boolean acceptAssignment, boolean acceptFunction) {
		this.friendlyString = friendlyString;
		this.acceptAssignment = acceptAssignment;
		this.acceptFunction = acceptFunction;
	}
	
	public final static VariableTypes fromString(String friendlyString) {
		VariableTypes returnValue = null;
		
		for(VariableTypes type : VariableTypes.values()) {
			if(type.friendlyString.equals(friendlyString)) {
				returnValue = type;
				break;
			}
		}
		
		if(returnValue == null) {
			throw new IllegalArgumentException(friendlyString + " has no valid VariableTypes.");
		}
		
		return returnValue;
	}
	
	public boolean isAcceptAssignment() {
		return this.acceptAssignment;
	}
	
	public boolean isAcceptFunction() {
		return this.acceptFunction;
	}
}
