package com.canadainc.sunnah10.processors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;

import com.canadainc.sunnah10.Book;
import com.canadainc.sunnah10.Chapter;
import com.canadainc.sunnah10.Narration;

/**
 * Collects the narrations from a Sunnah.com schema.
 * @author rhaq
 *
 */
public class SunnahDotComProcessor implements Processor 
{
	private static final String[] GRADE_FIELDS = new String[]{"grade1", "grade2"};
	private static final String[] PRIMARY_KEYS = new String[]{"arabicURN", "englishURN"};
	private static final String[] TRANSLATION_FIELDS = new String[]{"matchingArabicURN", "matchingEnglishURN"};
	protected final ArrayList<Narration> m_narrations = new ArrayList<>();
	private final Map<Integer,String> m_idToCollection = new HashMap<>();
	private final Map<Integer,Integer> m_idToTranslation = new HashMap<>();
	private static final String[] GRADELESS_COLLECTIONS = new String[]{"bukhari", "muslim"};


	// 			// skip i1857, mismatched translation
	// 1262050 which is i1886 (2nd one), starting here, we have to +1 on hadithNum [it's supposed to really be i1887] until 1891

	private static int readInt(JSONObject json, String key)
	{
		int result = 0;
		String toConvert = (String)json.get(key);

		if (toConvert != null)
		{
			if ( toConvert.matches("\\d+$") ) {
				result = Integer.parseInt(toConvert);
			} else {
				double d = Double.parseDouble(toConvert);
				result = (int)d;
			}
		}

		return result;
	}


	@Override
	public boolean preprocess(JSONObject json) {
		return true;
	}


	@Override
	public void process(JSONObject json)
	{
		Narration n = new Narration();
		n.id = Integer.parseInt( extractAvailable(PRIMARY_KEYS, json) );
		n.chapter = new Chapter( ((String)json.get("babName")).trim(), readInt(json, "babNumber") );
		n.book = new Book( readInt(json, "bookID"), ((String)json.get("bookName")).trim() );
		n.hadithNumber = (String)json.get("hadithNumber");
		n.inBookNumber = readInt(json, "ourHadithNumber");
		n.text = (String)json.get("hadithText");
		n.grading = (String)extractAvailable(GRADE_FIELDS, json);
		n.commentary = (String)json.get("annotations");

		String translationId = extractAvailable(TRANSLATION_FIELDS, json);

		if (translationId != null) {
			m_idToTranslation.put( n.id, Integer.parseInt(translationId) );
		}

		m_narrations.add(n);
	}


	private static final String extractAvailable(String[] keys, JSONObject json)
	{
		for (String key: keys)
		{
			if ( json.containsKey(key) ) {
				return (String)json.get(key);
			}
		}

		return null;
	}


	@Override
	public List<Narration> getNarrations() {
		return m_narrations;
	}


	@Override
	public boolean hasGrade(int id)
	{
		String collection = m_idToCollection.get(id);
		return Arrays.binarySearch(GRADELESS_COLLECTIONS, collection) != -1;
	}


	@Override
	public int getPageNumber(JSONObject json) {
		return Integer.parseInt( json.get("bookNumber").toString() );
	}
}