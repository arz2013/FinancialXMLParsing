package edu.ucsd.xmlparser.entity;

public enum PhraseTypes {
	NNP;
	
	public static boolean isNNP(String posTag) {
		return PhraseTypes.NNP.name().equals(posTag);
	}
}
