package edu.ucsd.questionanswering;

import edu.ucsd.xmlparser.entity.Word;

public interface SentenceFormHandler {
	Answer handleWord(Word word);
}
