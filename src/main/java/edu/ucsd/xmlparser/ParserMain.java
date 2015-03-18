package edu.ucsd.xmlparser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import edu.ucsd.cvalue.CValueDocumentCalculator;
import edu.ucsd.system.SystemApplicationContext;

public class ParserMain {
	private static Logger logger = LoggerFactory.getLogger(ParserMain.class);
	
	public static void main(String[] args) throws Exception {
		ApplicationContext context = SystemApplicationContext.getApplicationContext();

		String[] fileNames = { "Annual_Report_Chevron_2013.xml", "Annual_Report_Disney_2013.xml" };
		List<File> files = new ArrayList<File>();
		for(String fileName : fileNames) {
			files.add(new File(ParserMain.class.getClassLoader().getResource(fileName).getFile()));
		}
		FinancialXMLParser parser = FinancialXMLParser.class.cast(context.getBean("financialXMLParser"));
		for(File file : files) {
			logger.info("Now processing file: " + file);
			parser.parseAndLoad(file, 2013);
			parser.reset();
		}

		logger.info("Calculating Collection Level CValues");
		CValueDocumentCalculator cValueDocumentCalculator = CValueDocumentCalculator.class.cast(context.getBean("cValueDocumentCalculator"));
		cValueDocumentCalculator.computeCollectionLevelCValue();
		
		logger.info("Performing Sentence Scoring.");
		SentenceScorer sentenceScorer = SentenceScorer.class.cast(context.getBean("sentenceScorer"));
		sentenceScorer.scoreSentence();
	}

}
