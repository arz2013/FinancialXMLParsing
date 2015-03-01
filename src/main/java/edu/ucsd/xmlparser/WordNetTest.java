package edu.ucsd.xmlparser;

import edu.ucsd.wordnet.LexicalUtility;

public class WordNetTest {

	public static void main(String[] args) {
		System.out.println("Noun Form: " + LexicalUtility.getNounsIncludingPluralFormsForVerb("acquire"));
		System.out.println("Past Tense of Verb: " + LexicalUtility.getPastTense("acquire"));
	}
}
