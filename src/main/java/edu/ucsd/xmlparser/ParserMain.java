package edu.ucsd.xmlparser;

import java.io.File;

public class ParserMain {

	public static void main(String[] args) throws Exception {
		File financialFile = new File(ParserMain.class.getClassLoader().getResource("sample-financial.xml").getFile());
		FinancialXMLParser parser = new FinancialXMLParser();
		parser.parse(financialFile);
	}

}
