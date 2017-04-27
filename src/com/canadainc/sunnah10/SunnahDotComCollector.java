package com.canadainc.sunnah10;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.canadainc.common.io.IOUtils;

/**
 * Collects the narrations from a Sunnah.com schema.
 * @author rhaq
 *
 */
public class SunnahDotComCollector
{
	private File m_arabic;
	private File m_english;
	
	/** Hadith number to narration. */
	private Map<Integer, Narration> m_idToArabic;
	
	/** Hadith number to narration. */
	private Map<Integer, Narration> m_idToEnglish;
	
	private Map<Integer, Integer> m_idToTranslation;
	
	public SunnahDotComCollector(File arabic, File english)
	{
		m_arabic = arabic;
		m_english = english;
		m_idToArabic = new HashMap<>();
		m_idToEnglish = new HashMap<>();
		m_idToTranslation = new HashMap<>();
	}
	
	
	public void parse()
	{
		try
		{
			// skip i1857, mismatched translation
			// 1262050 which is i1886 (2nd one), starting here, we have to +1 on hadithNum [it's supposed to really be i1887] until 1891
			
			
			Collection<Narration> arabic = parseLanguage( m_arabic.listFiles( new PagesFilter() ), "arabicURN", "matchingEnglishURN" );
			Collection<Narration> english = parseLanguage( m_english.listFiles( new PagesFilter() ), "englishURN", "matchingArabicURN" );
			
			System.out.println(arabic.size()+"; english="+english.size()+"; "+m_idToTranslation.size());
			
			for (Narration n: arabic) {
				if (!n.hadithNumber.matches("\\d+$")) {
					System.out.println(n.hadithNumber);
				}

				if ( !m_idToTranslation.containsKey(n.id) && !m_idToTranslation.containsValue(n.id) ) {
					System.out.println("NoMatchFor: "+n);
				}
			}
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	
	private Collection<Narration> parseLanguage(File[] files, String idField, String translationField) throws IOException
	{
		Collection<Narration> narrations = new ArrayList<>();

		for (File f: files)
		{
			JSONArray array = (JSONArray)JSONValue.parse( IOUtils.readFileUtf8(f) );
			
			for (int j = 0; j < array.size(); j++)
			{
				JSONObject json = (JSONObject)array.get(j);

				Narration n = new Narration();
				n.id = readInt(json, idField);
				n.chapter = new Chapter( (String)json.get("babName"), readInt(json, "babNumber") );
				n.book = new Book( readInt(json, "bookID"), (String)json.get("bookName") );
				n.hadithNumber = (String)json.get("hadithNumber");
				n.inBookNumber = readInt(json, "ourHadithNumber");
				n.text = (String)json.get("hadithText");
				narrations.add(n);
				
				int translationId = readInt(json, translationField);
				
				if (translationId != 0) {
					m_idToTranslation.put(n.id, translationId );
				}
			}
		}
		
		return narrations;
	}
	
	
	private static int readInt(JSONObject json, String key)
	{
		int result = 0;
		String toConvert = (String)json.get(key);

		if (toConvert != null)
		{
			try {
				result = Integer.parseInt(toConvert);
			} catch (NumberFormatException ex) {
				double d = Double.parseDouble(toConvert);
				result = (int)d;
			}
		}

		return result;
	}
	
	
	private class PagesFilter implements FilenameFilter
	{
		@Override
		public boolean accept(File dir, String name) {
			return name.endsWith(".txt");
		}
	}
}