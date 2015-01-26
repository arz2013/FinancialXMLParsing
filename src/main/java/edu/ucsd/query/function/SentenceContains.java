package edu.ucsd.query.function;

import java.util.Set;

import edu.ucsd.xmlparser.entity.Sentence;

public class SentenceContains implements Contains<Sentence> {

	@Override
	public Set<Sentence> contains(Set<Sentence> sentences) {
		return sentences;
	}

}
