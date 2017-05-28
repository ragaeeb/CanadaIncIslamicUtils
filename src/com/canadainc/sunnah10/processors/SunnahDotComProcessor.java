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
	public static final String[] IGNORED_AHADEETH = new String[]{"1002090", "1004370", "1037350", "1262090", "1258635", "1293310"};
	private static final String[] GRADE_FIELDS = new String[]{"grade1", "grade2"};
	private static final String[] PRIMARY_KEYS = new String[]{"arabicURN", "englishURN"};
	private static final String[] TRANSLATION_FIELDS = new String[]{"matchingArabicURN", "matchingEnglishURN"};
	protected final ArrayList<Narration> m_narrations = new ArrayList<>();
	private final Map<Integer,Integer> m_idToTranslation = new HashMap<>();
	private final SunnahTypoProcessor m_typos = new SunnahTypoProcessor();

	public SunnahDotComProcessor()
	{	
		m_typos.fixHadithNumber(1002160, 215);
		m_typos.fixHadithNumber(1076740, 2497);
		m_typos.fixHadithNumber(1078600, 2687);
		m_typos.fixHadithNumber(1072920, 2111);
		m_typos.fixHadithNumber(1073140, 2134);

		// should this be 3052 or 3055?
		m_typos.fixHadithNumber(1082165, 3055);

		m_typos.fixHadithNumber(1084745, 4422);
		m_typos.fixHadithNumber(1085350, 4545);
		m_typos.fixHadithNumber(1086075, 4689);
		m_typos.fixHadithNumber(1086330, 4740);
		m_typos.fixHadithNumber(1086660, 4806);
		m_typos.fixHadithNumber(1086915, 4859);
		m_typos.fixHadithNumber(1251710, 172);
		m_typos.fixHadithNumber(1256410, 642);
		m_typos.fixHadithNumber(1259530, 3808);
		m_typos.fixHadithNumber(1262050, 1887);
		m_typos.fixHadithNumber(1262060, 1888);
		m_typos.fixHadithNumber(1262070, 1889);
		m_typos.fixHadithNumber(1262080, 1890);
		m_typos.fixHadithNumber(1262480, 1929);
		m_typos.fixHadithNumber(1262840, 1965);
		m_typos.fixHadithNumber(1263830, 2064);
		m_typos.fixHadithNumber(1263950, 2076);
		m_typos.fixHadithNumber(1263960, 2077);
		m_typos.fixHadithNumber(1266400, 2322);
		m_typos.fixHadithNumber(1293390, 4236);

		m_typos.fixRange(1002100, 1003540, -1);

		// BIG RANGE
		m_typos.fixRange(1004380, 1070770, -1, 1068370);
		m_typos.fixRange(1070810, 1071100, -1);
		m_typos.fixRange(1034000, 1034330, -29);
		m_typos.fixRange(1034340, 1037340, -30);
		m_typos.fixRange(1037348, 1039330, -31);
		m_typos.fixRange(1039350, 1039530, -32);
		m_typos.fixRange(1039540, 1039800, 548);
		m_typos.fixRange(1071120, 1076980, -2);
		m_typos.fixRange(1076985, 1079380, -1);
		m_typos.fixRange(1079390, 1079880, -2);
		m_typos.fixRange(1079900, 1082165, -3);
		m_typos.fixRange(1082170, 1082480, -2);
		m_typos.fixRange(1030960, 1033990, -2);
		m_typos.fixRange(1082500, 1085145, -5);
		m_typos.fixRange(1085148, 1087315, -4);
		m_typos.fixRange(1087325, 1088725, -3);
		m_typos.fixRange(1088730, 1090435, -2);
		m_typos.fixRange(1090445, 1091410, -3);

		m_typos.decompose(1087320, 4938, "And he (the narrator)");
		m_typos.decompose(1265570, 2238, "It was narrated from Abu Hurairah");

		m_typos.merge(1003860);
		m_typos.merge(1005135);
		m_typos.merge(1035440);
		m_typos.merge(1035450);
		m_typos.merge(1064940);
		m_typos.merge(1064960);
		m_typos.merge(1065000);
		m_typos.merge(1090440, 5564);
		m_typos.merge(1039340, 3919);
		m_typos.merge(1071110, 1928);
		m_typos.merge(1079890, 2821);
		m_typos.merge(1090440, 5564);
		m_typos.merge(1290300);
		m_typos.merge(1291885);

		// merge all lettered
		m_typos.merge(1039340, 3919);
	}

	public final Map<Integer,Integer> getTranslations() {
		return m_idToTranslation;
	}


	@Override
	public boolean preprocess(JSONObject json)
	{		
		String urn = (String)json.get("englishURN");

		if ( urn != null && ArrayUtils.contains(IGNORED_AHADEETH, urn) ) {
			return false;
		}

		return m_typos.process(json, this);
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
			m_typos.track( Integer.parseInt(n.hadithNumber), m_narrations.size() );
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