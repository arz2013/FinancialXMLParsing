package edu.ucsd.questionanswering;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class QuestionAnswering {
	public static void processQuery(String question) {
		List<String> questionTokens = new ArrayList<String>();
		StringTokenizer tokenizer = new StringTokenizer(question);
		while(tokenizer.hasMoreTokens()) {
			questionTokens.add(tokenizer.nextToken());
		}
		
	}
}
