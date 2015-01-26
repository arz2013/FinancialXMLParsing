package edu.ucsd.query.function;

import java.util.Set;

import edu.ucsd.xmlparser.entity.Sentence;

public interface Contains<P> {
	Set<P> contains(Set<Sentence> sentences);
}
