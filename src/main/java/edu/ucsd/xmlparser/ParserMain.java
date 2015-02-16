package edu.ucsd.xmlparser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.ApplicationContext;

import edu.ucsd.system.SystemApplicationContext;

public class ParserMain {

	public static void main(String[] args) throws Exception {
		String[] fileNames = { "ShortenedTestDocument.xml", "ShortenedTestDocument2.xml" };
		List<File> files = new ArrayList<File>();
		for(String fileName : fileNames) {
			files.add(new File(ParserMain.class.getClassLoader().getResource(fileName).getFile()));
		}
		ApplicationContext context = SystemApplicationContext.getApplicationContext();
		FinancialXMLParser parser = FinancialXMLParser.class.cast(context.getBean("financialXMLParser"));
		for(File file : files) {
			parser.parseAndLoad(file, 2013);
		}
	}

}
