package edu.ucsd.xmlparser;

import java.io.File;

import org.springframework.context.ApplicationContext;

import edu.ucsd.system.SystemApplicationContext;

public class ParserMain {

	public static void main(String[] args) throws Exception {
		File financialFile = new File(ParserMain.class.getClassLoader().getResource("sample-financial.xml").getFile());
		ApplicationContext context = SystemApplicationContext.getApplicationContext();
		FinancialXMLParser parser = new FinancialXMLParser();
		parser.parse(financialFile);
	}

}
