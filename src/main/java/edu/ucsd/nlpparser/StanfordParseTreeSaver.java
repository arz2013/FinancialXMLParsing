package edu.ucsd.nlpparser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.neo4j.graphdb.Node;
import org.springframework.data.neo4j.support.Neo4jTemplate;

import edu.stanford.nlp.trees.Tree;
import edu.ucsd.xmlparser.dao.SentenceDao;
import edu.ucsd.xmlparser.entity.ApplicationRelationshipType;
import edu.ucsd.xmlparser.entity.NonLeafParseNode;
import edu.ucsd.xmlparser.entity.NonLeafToLeaf;
import edu.ucsd.xmlparser.entity.ParseChild;
import edu.ucsd.xmlparser.entity.Sentence;
import edu.ucsd.xmlparser.entity.SentenceToNonLeafParseNode;
import edu.ucsd.xmlparser.entity.Word;

/**
 * Persists the Stanford generated parse tree of sentences
 * 
 * @author rogertan
 *
 */
public class StanfordParseTreeSaver {
	private SentenceDao sentenceDao;
	private Sentence sentence;
	private Map<Word.TextAndPosition, Word> seenWords;
	private Neo4jTemplate template;
	
	private boolean isExcludeRoot = true;
	
	private List<String> inOrder = new ArrayList<String>();
	
	public StanfordParseTreeSaver(SentenceDao sentenceDao, Neo4jTemplate template, Sentence sentence, Map<Word.TextAndPosition, Word> seenWords) {
		if(sentenceDao == null) {
			throw new IllegalArgumentException("DAO can not be null.");
		} 
		
		if(template == null) {
			throw new IllegalArgumentException("Template can not be null.");
		}
		
		if(sentence == null) {
			throw new IllegalArgumentException("Sentence cannot be null.");
		}
		
		if(seenWords == null) {
			throw new IllegalArgumentException("Seen Words can't be null");
		}
		
		this.sentenceDao = sentenceDao;
		this.template = template;
		this.sentence = sentence;
		this.seenWords = seenWords;
	}
	
	public void performDepthFirstTraversal(Tree tree) {
		if(tree == null) {
			throw new IllegalArgumentException("Argument Tree can not be null");
		}
		
		innerDepthFirstTraversal(tree, null);
	}
	
	private Node innerDepthFirstTraversal(Tree tree, NonLeafParseNode parent) {
		List<Tree> children = tree.getChildrenAsList();
		NonLeafParseNode currentNode = NonLeafParseNode.newNonLeafParseNode(tree.value());
		
		if(tree.isLeaf()) {
			inOrder.add(tree.value());

			Word word = Word.newWord(tree.value(), inOrder.size());
			word = seenWords.get(word.getTextAndPosition());
			sentenceDao.save(new NonLeafToLeaf(parent, word));
			
			// template.createRelationshipBetween(template.getNode(parent.getId()), template.getNode(word.getId()), Rel.FIRST_CHILD.name(), new HashMap<String, Object>());
			
			return template.getNode(word.getId());
		} else {
			if(currentNode.isRoot() && !isExcludeRoot) { // Check if the current node is root and whether root needs to be excluded
				sentenceDao.save(currentNode);			
				sentenceDao.save(new SentenceToNonLeafParseNode(sentence, currentNode));
			} else if(parent != null) { 
				if(isExcludeRoot && parent.isRoot()) { // Check if root needs to be excluded, in which case we link the children of root directly to the sentence
					sentenceDao.save(currentNode);
					sentenceDao.save(new SentenceToNonLeafParseNode(sentence, currentNode));
				} else {
					sentenceDao.save(currentNode);
					sentenceDao.save(new ParseChild(parent, currentNode));
				}
			}
			
		}
		
		int childIndex = 0;
		Node prevNode = null;
		for(Tree child : children) {
			Node childNode = this.innerDepthFirstTraversal(child, currentNode);
			if(childIndex == 0) {
				if(currentNode.getId() != null) { // ROOT may not be saved
					template.createRelationshipBetween(template.getNode(currentNode.getId()), childNode, ApplicationRelationshipType.FIRST_CHILD.name(), new HashMap<String, Object>());
				} else {
					template.createRelationshipBetween(template.getNode(sentence.getId()), childNode, ApplicationRelationshipType.FIRST_CHILD.name(), new HashMap<String, Object>());
				}
			} else {
				template.createRelationshipBetween(prevNode, childNode, ApplicationRelationshipType.NEXT.name(), new HashMap<String, Object>());			
			}
			prevNode = childNode;
			childIndex++;
		}
		
		if(currentNode.getId() != null) {
			return template.getNode(currentNode.getId());
		} else {
			return null;
		}
	}
}
