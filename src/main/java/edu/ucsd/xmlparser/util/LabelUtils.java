package edu.ucsd.xmlparser.util;

public abstract class LabelUtils {
	private static String LABELSTRATEGY_PREFIX = "_";
	
	public static String createLabel(String userSuppliedLabel) {
		return LABELSTRATEGY_PREFIX + userSuppliedLabel;
	}
	
	public static String labelPOS(String posInformation) {
		return "POS{" + posInformation + "}";
	}
	
	public static String labelNE(String neInformation) {
		return "NE{" + neInformation + "}";
	}
}
