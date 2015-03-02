package edu.ucsd.questionanswering;

import java.util.List;

public class TellMeQuestionHandler implements QuestionHandler {

	@Override
	public Answer answerQuestion(List<ParsedWord> parsedQuestion) {
		Answer answer = new NoAnswer();
		if(parsedQuestion.size() < 4 || invalidForm(parsedQuestion)) {
			return answer;
		}
		
		return answer;
	}

	private boolean invalidForm(List<ParsedWord> parsedQuestion) {
		String firstWord = parsedQuestion.get(0).getWord().toLowerCase();
		String secondWord = parsedQuestion.get(1).getWord().toLowerCase();
		String thirdWord = parsedQuestion.get(2).getWord().toLowerCase();
		
		return !firstWord.equals("tell") || !secondWord.equals("me") || !thirdWord.equals("about");
	}

}
