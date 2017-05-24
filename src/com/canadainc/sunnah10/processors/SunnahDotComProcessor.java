package com.canadainc.sunnah10.processors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
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
	public static final String[] IGNORED_AHADEETH = new String[]{"1262090", "1258635", "1293310"};
	private static final String[] GRADE_FIELDS = new String[]{"grade1", "grade2"};
	private static final String[] PRIMARY_KEYS = new String[]{"arabicURN", "englishURN"};
	private static final String[] TRANSLATION_FIELDS = new String[]{"matchingArabicURN", "matchingEnglishURN"};
	protected final ArrayList<Narration> m_narrations = new ArrayList<>();
	private final Map<Integer,Integer> m_idToTranslation = new HashMap<>();
	private final Map<String,String> m_idToFixedHadithNum = new HashMap<>();
	private static final String[] APPEND_TO_PREV = new String[]{"1290300", "1291885"};
	private final Map<Integer,Integer> m_hadithNumToIndex = new HashMap<>();

	public SunnahDotComProcessor()
	{
		m_idToFixedHadithNum.put("1251710", "172");
		m_idToFixedHadithNum.put("1256410", "642");
		m_idToFixedHadithNum.put("1262050", "1887");
		m_idToFixedHadithNum.put("1262060", "1888");
		m_idToFixedHadithNum.put("1262070", "1889");
		m_idToFixedHadithNum.put("1262080", "1890");
		m_idToFixedHadithNum.put("1262480", "1929");
		m_idToFixedHadithNum.put("1262840", "1965");
		m_idToFixedHadithNum.put("1263830", "2064");
		m_idToFixedHadithNum.put("1263950", "2076");
		m_idToFixedHadithNum.put("1263960", "2077");
		m_idToFixedHadithNum.put("1266400", "2322");
		m_idToFixedHadithNum.put("1259530", "3808");
		m_idToFixedHadithNum.put("1293390", "4236");
	}

	public final Map<Integer,Integer> getTranslations() {
		return m_idToTranslation;
	}


	@Override
	public boolean preprocess(JSONObject json)
	{		
		String urn = (String)json.get("englishURN");

		if (urn != null)
		{
			if ( m_idToFixedHadithNum.containsKey(urn) ) {
				json.put("hadithNumber", m_idToFixedHadithNum.get(urn) );
			} else if ( ArrayUtils.contains(IGNORED_AHADEETH, urn) ) {
				return false;
			} else if ( urn.equals("1265570") ) {
				String body = json.get("hadithText").toString();
				int start = body.indexOf("It was narrated from Abu Hurairah");

				json.put("hadithText", body.substring(start).trim());
				json.put("hadithNumber", "2239");

				JSONObject prev = new JSONObject(json);
				prev.put("hadithText", body.substring(0,start).trim());
				prev.put("hadithNumber", "2238");
				prev.put("matchingArabicURN", "1323240");
				process(prev);
			} else if ( ArrayUtils.contains(APPEND_TO_PREV, urn) ) {
				int hadithNum = Integer.parseInt( json.get("hadithNumber").toString() );
				Integer index = m_hadithNumToIndex.get(hadithNum);

				if (index != null) {
					Narration n = m_narrations.get(index);
					n.text += " "+json.get("hadithText").toString();
				}

				return false;
			}
		}

		return true;
	}


	@Override
	public void process(JSONObject json)
	{
		Narration n = new Narration();
		n.id = Integer.parseInt( extractAvailable(PRIMARY_KEYS, json) );

		String babName = (String)json.get("babName");
		n.chapter = new Chapter( babName != null ? babName.trim() : null, readInt(json, "babNumber") );
		
		babName = (String)json.get("bookName");
		n.book = new Book( readInt(json, "bookID"), babName != null ? babName.trim() : null );
		n.hadithNumber = (String)json.get("hadithNumber");
		n.inBookNumber = readInt(json, "ourHadithNumber");
		n.text = (String)json.get("hadithText");
		n.grading = (String)extractAvailable(GRADE_FIELDS, json);
		n.commentary = (String)json.get("annotations");

		String translationId = extractAvailable(TRANSLATION_FIELDS, json);

		if (translationId != null) {
			m_idToTranslation.put( n.id, Integer.parseInt(translationId) );
		}

		if ( n.hadithNumber.matches("\\d+$") ) {
			m_hadithNumToIndex.put( Integer.parseInt(n.hadithNumber), m_narrations.size() );
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
	public boolean hasGrade(int id) {
		return false;
	}


	@Override
	public int getPageNumber(JSONObject json) {
		return Integer.parseInt( json.get("bookNumber").toString() );
	}

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
}