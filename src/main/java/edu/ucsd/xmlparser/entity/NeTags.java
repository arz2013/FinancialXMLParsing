package edu.ucsd.xmlparser.entity;

public enum NeTags {
	ORGANIZATION, PERSON, MONEY;
	
	public static boolean isValid(String nameEntity) {
		for(NeTags tag : NeTags.values()) {
			if(tag.name().toLowerCase().equals(nameEntity.toLowerCase())) {
				return true;
			}
		}
		
		return false;
	}
	
	public static boolean isOrganizationOrPerson(String neTag) {
		return NeTags.ORGANIZATION.name().equals(neTag) || NeTags.PERSON.name().equals(neTag);
	}
}
