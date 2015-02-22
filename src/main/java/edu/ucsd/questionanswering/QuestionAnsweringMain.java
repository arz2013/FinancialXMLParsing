package edu.ucsd.questionanswering;

import org.springframework.context.ApplicationContext;

import edu.ucsd.system.SystemApplicationContext;

public class QuestionAnsweringMain {

	public static void main(String[] args) {
		ApplicationContext ctx = SystemApplicationContext.getApplicationContext();
		QuestionAnsweringModule qaModule = QuestionAnsweringModule.class.cast(ctx.getBean("questionAnsweringModule"));
		qaModule.answer("Which organizations did Disney acquire?");
	}

}
