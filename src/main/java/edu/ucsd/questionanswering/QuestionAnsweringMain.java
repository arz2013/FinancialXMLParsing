package edu.ucsd.questionanswering;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import edu.ucsd.system.SystemApplicationContext;

public class QuestionAnsweringMain {
	private static Logger logger = LoggerFactory.getLogger(QuestionAnsweringMain.class);
	
	public static void main(String[] args) {
		ApplicationContext ctx = SystemApplicationContext.getApplicationContext();
		QuestionAnsweringModule qaModule = QuestionAnsweringModule.class.cast(ctx.getBean("questionAnsweringModule"));
		logger.info(qaModule.answer("Which organizations did Chevron acquire?").toString());
		/**
		qaModule.answer("Which organizations did Walt Disney acquire?");
		qaModule.answer("What acquisitions did Chevron make?"); // Unable to answer this one
		qaModule.answer("When did Disney acquire Marvel?");
		*/
	}

}
