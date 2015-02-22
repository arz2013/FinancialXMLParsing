package edu.ucsd.questionanswering;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

public class QuestionAnsweringModule implements ApplicationContextAware {
	private ApplicationContext applicationContext;
	
	private StanfordCoreNLP pipeline;
	
	public QuestionAnsweringModule() {
		Properties props = new Properties();
		props.put("annotators", "tokenize, pos, ner"); 
		pipeline = new StanfordCoreNLP(props);
	}
	
	public void answer(String question) {
		List<String> questionTokens = new ArrayList<String>();
		StringTokenizer tokenizer = new StringTokenizer(question);
		while(tokenizer.hasMoreTokens()) {
			questionTokens.add(tokenizer.nextToken());
		}
		
		Annotation questionDocument = new Annotation(question);
		pipeline.annotate(questionDocument);
		
		List<CoreMap> sentences = questionDocument.get(SentencesAnnotation.class);

		for(CoreMap sentence: sentences) {
			for (CoreLabel token: sentence.get(TokensAnnotation.class)) {
				// this is the text of the token
				String word = token.get(TextAnnotation.class);
				// this is the POS tag of the token
				String pos = token.get(PartOfSpeechAnnotation.class);
				// this is the NER label of the token
				String ne = token.get(NamedEntityTagAnnotation.class);
				System.out.println(word + ", " + pos + ", " + ne);
			}
		}
	}

	@Override
	public void setApplicationContext(ApplicationContext arg0)
			throws BeansException {
		this.applicationContext = arg0;
	}
}
