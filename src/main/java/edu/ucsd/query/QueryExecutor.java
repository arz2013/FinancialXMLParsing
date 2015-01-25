package edu.ucsd.query;

import javax.inject.Inject;
import java.util.List;

import edu.ucsd.query.dao.QueryFunctionDao;
import edu.ucsd.xmlparser.entity.Word;

public class QueryExecutor {
	@Inject
	private QueryFunctionDao queryFunctionDao;

	public List<Word> something(String word) {
		return queryFunctionDao.getWord(word);
	}
}
