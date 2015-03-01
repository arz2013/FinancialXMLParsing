package edu.ucsd.questionanswering;

import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class WhenQuestionHandler implements QuestionHandler, ApplicationContextAware {
	private ApplicationContext applicationContext;

	@Override
	public Answer answerQuestion(List<ParsedWord> parsedQuestion) {
		return new NoAnswer();
	}

	@Override
	public void setApplicationContext(ApplicationContext arg0)
			throws BeansException {
		this.applicationContext = arg0;
	}
}
