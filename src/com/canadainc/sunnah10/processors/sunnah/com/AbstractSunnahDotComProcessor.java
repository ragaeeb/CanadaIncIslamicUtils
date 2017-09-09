package com.canadainc.sunnah10.processors.sunnah.com;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;

import com.canadainc.sunnah10.Book;
import com.canadainc.sunnah10.Chapter;
import com.canadainc.sunnah10.Narration;
import com.canadainc.sunnah10.processors.Processor;

/**
 * Collects the narrations from a Sunnah.com schema.
 * @author rhaq
 *
 */
public class AbstractSunnahDotComProcessor implements Processor 
{
	private static final String[] GRADE_FIELDS = new String[]{"grade1", "grade2"};
	private static final String[] PRIMARY_KEYS = new String[]{"arabicURN", "englishURN"};
	private static final String[] TRANSLATION_KEYS = new String[]{"matchingArabicURN", "matchingEnglishURN"};
	protected final ArrayList<Narration> m_narrations = new ArrayList<>();
	protected final SunnahTypoProcessor m_typos = new SunnahTypoProcessor();
	private Map<Integer,Integer> m_idToTranslation = new HashMap<>();


	@Override
	public boolean preprocess(JSONObject json) {
		return m_typos.process(json, this);
	}


	public void process(Narration n) {
		m_narrations.add(n);
	}


	@Override
	public final void process(JSONObject json)
	{
		Narration n = new Narration();
		n.id = Integer.parseInt( extractAvailable(PRIMARY_KEYS, json) );

		String babName = (String)json.get("babName");
		n.chapter = new Chapter( babName != null ? babName.trim() : null, readInt(json, "babNumber") );

		babName = (String)json.get("bookName");
		n.book = new Book( getPageNumber(json), babName != null ? babName.trim() : null );
		n.hadithNumber = (String)json.get("hadithNumber");
		n.inBookNumber = readInt(json, "ourHadithNumber");
		n.text = (String)json.get("hadithText");
		n.grading = (String)extractAvailable(GRADE_FIELDS, json);

		n.commentary = (String)json.get("annotations");

		String translationValue = extractAvailable(TRANSLATION_KEYS, json);
		int translationId = !translationValue.isEmpty() ? Integer.parseInt( translationValue ) : 0;

		if (translationId > 0) {
			m_idToTranslation.put(n.id, translationId);
		} else {
			System.err.println("TranslationMissingFor: "+n.id);
		}

		process(n);
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
	public final List<Narration> getNarrations() {
		return m_narrations;
	}


	@Override
	public boolean hasGrade(int id) {
		return false;
	}


	@Override
	public final int getPageNumber(JSONObject json) {
		return readInt(json, "bookID");
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


	public Map<Integer,Integer> getTranslations() {
		return m_idToTranslation;
	}


	/**
	 * @return the typos
	 */
	SunnahTypoProcessor getTypos() {
		return m_typos;
	}


	@Override
	public void postProcess()
	{
	}
}