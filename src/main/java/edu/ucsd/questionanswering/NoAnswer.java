package edu.ucsd.questionanswering;

public class NoAnswer implements Answer {

	@Override
	public String asText() {
		return "Answer Unavailable.";
	}

}