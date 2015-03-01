package edu.ucsd.questionanswering;

import org.springframework.context.ApplicationContext;

import edu.ucsd.system.SystemApplicationContext;

public class QuestionAnsweringMain {

	public static void main(String[] args) {
		ApplicationContext ctx = SystemApplicationContext.getApplicationContext();
		QuestionAnsweringModule qaModule = QuestionAnsweringModule.class.cast(ctx.getBean("questionAnsweringModule"));
		System.out.println(qaModule.answer("Which organizations did Chevron acquire?"));
		/**
		qaModule.answer("Which organizations did Walt Disney acquire?");
		qaModule.answer("What acquisitions did Chevron make?"); // Unable to answer this one
		qaModule.answer("When did Disney acquire Marvel?");
		*/
	}

}
