package edu.ucsd.questionanswering;

import java.util.List;

public class ListAnswer implements Answer {
	private List<String> multipleStrings;
	
	public ListAnswer(List<String> multipleStrings) {
		this.multipleStrings = multipleStrings;
	}
	
	@Override
	public String asText() {
		StringBuilder sb = new StringBuilder();
		multipleStrings.stream().map(s -> sb.append(s + ". "));
		return sb.toString();
	}

	@Override
	public String toString() {
		return "ListAnswer [multipleStrings=" + multipleStrings + "]";
	}
}
