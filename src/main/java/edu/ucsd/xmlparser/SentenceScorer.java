package edu.ucsd.xmlparser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import edu.ucsd.xmlparser.entity.CValueCollectionNode;
import edu.ucsd.xmlparser.entity.CValueSectionNode;
import edu.ucsd.xmlparser.repository.CValueRepository;

public class SentenceScorer {
	@Inject
	private CValueRepository cValueRepository;
	
	public void scoreSentence() {
		List<CValueCollectionNode> nodes = cValueRepository.getAllCValueCollectionNodes();
		Map<String, Double> textToCValue = convertToMap(nodes);
		List<CValueSectionNode> secNodes = cValueRepository.getAllSectionNodes();
		Map<Long, Double> sentenceIdToCombinedValue = new HashMap<Long, Double>();
		Map<String, Set<Long>> textToSentenceIds = convertToSentenceIdMapping(secNodes);
		for(String text : textToSentenceIds.keySet()) {
			for(Long sentenceId : textToSentenceIds.get(text)) {
				Double cValue = textToCValue.get(text);
				Double currentValue = sentenceIdToCombinedValue.putIfAbsent(sentenceId, cValue);
				if(currentValue != null) {
					sentenceIdToCombinedValue.put(sentenceId, cValue + currentValue);
				}
			}
		}
		
	}

	private Map<String, Set<Long>> convertToSentenceIdMapping(
			List<CValueSectionNode> secNodes) {
		Map<String, Set<Long>> result = new HashMap<String, Set<Long>>();
		
		for(CValueSectionNode secNode : secNodes) {
			Set<Long> currentSentenceIds = result.putIfAbsent(secNode.getText(), secNode.getSentenceIds());
			if(currentSentenceIds != null) {
				currentSentenceIds.addAll(secNode.getSentenceIds());
				result.put(secNode.getText(), currentSentenceIds);
			}
		}
		
		return result;
	}

	private Map<String, Double> convertToMap(List<CValueCollectionNode> nodes) {
		Map<String, Double> result = new HashMap<String, Double>();
		for(CValueCollectionNode cValCollNode : nodes) {
			result.put(cValCollNode.getText(), cValCollNode.getCValue());
		}
		
		return result;
	}
}
