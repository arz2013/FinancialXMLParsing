package edu.ucsd.wordnet;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.atteo.evo.inflector.English;
import org.yawni.wordnet.POS;
import org.yawni.wordnet.Relation;
import org.yawni.wordnet.WordNet;
import org.yawni.wordnet.WordSense;

public class LexicalUtility {
	private static WordNet wordNet = WordNet.getInstance();
	
	public static Set<String> getNounsIncludingPluralFormsForVerb(String verbWord) {
		List<WordSense> senses = wordNet.lookupWordSenses(verbWord, POS.VERB);
		Set<String> nounForms = new HashSet<String>();
		for(WordSense sense : senses) {
			for(Relation relation : sense.getRelations()) {
				if(relation.getTarget().getPOS().equals(POS.NOUN)) {
					String noun = relation.getTarget().getDescription();
					nounForms.add(noun);
					nounForms.add(English.plural(noun));
					break;
				}
			}
		}
		return nounForms;
	}
}
