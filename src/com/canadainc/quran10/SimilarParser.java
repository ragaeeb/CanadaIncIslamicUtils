package com.canadainc.quran10;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.canadainc.common.io.IOUtils;
import com.canadainc.common.text.TextUtils;

public class SimilarParser
{
	private String m_input;
	private Map< Supplication, List<Supplication> > m_similar;
	private static String SURAH_HEADER = "Surah [0-9]{1,3} Aya[ht] [0-9]{1,3}$";
	private static String SURAH_RANGE_HEADER = "Surah [0-9]{1,3} Aya[ht] [0-9]{1,3} - [0-9]{1,3}$";
	private static String SIMILAR_AYAT = "\\([0-9]{1,3}:\\s+[0-9]{1,3}\\)$";
	private static String SIMILAR_AYAT_RANGE = "\\([0-9]{1,3}:\\s+[0-9]{1,3}\\s-\\s[0-9]{1,3}\\)$";

	public SimilarParser(String input)
	{
		m_input = input;
	}
	
	
	public void parse() throws IOException
	{
		m_similar = new HashMap< Supplication, List<Supplication> >();
		
		String[] contents = IOUtils.readFileUtf8( new File(m_input) ).trim().split("\n");
		Supplication active = new Supplication();
		
		for (int i = 0; i < contents.length; i++)
		{
			String current = contents[i];
			
			if ( current.matches(SURAH_HEADER) ) {
				active = new Supplication();
				String[] tokens = current.split("\\s+");
				active.chapter = Integer.parseInt(tokens[1]);
				active.verseStart = active.verseEnd = Integer.parseInt(tokens[3]);
			} else if ( current.matches(SURAH_RANGE_HEADER) ) {
				active = new Supplication();
				String[] tokens = current.split("\\s+");
				active.chapter = Integer.parseInt(tokens[1]);
				active.verseStart = Integer.parseInt(tokens[3]);
				active.verseEnd = Integer.parseInt(tokens[5]);
			}
			
			List<Supplication> all = m_similar.get(active);
			
			if (all == null) {
				all = new ArrayList<Supplication>();
			}
			
			Supplication similar = null;
			
			if ( current.matches(SIMILAR_AYAT) ) // 40,65
			{
				current = TextUtils.removeQuotes(current);
				
				similar = new Supplication();
				String[] tokens = current.split(":\\s+");
				similar.chapter = Integer.parseInt(tokens[0]);
				similar.verseStart = similar.verseEnd = Integer.parseInt(tokens[1]);
			} else if ( current.matches(SIMILAR_AYAT_RANGE) ) {
				current = TextUtils.removeQuotes(current);
				
				similar = new Supplication(); // 2: 1 - 2
				String[] tokens = current.split(":\\s+");
				similar.chapter = Integer.parseInt(tokens[0]);
				
				tokens = tokens[1].split("\\s+-\\s+");
				similar.verseStart = Integer.parseInt(tokens[0]);
				similar.verseEnd = Integer.parseInt(tokens[1]);
			}
			
			if ( similar != null && !similar.equals(active) ) {
				all.add(similar);
			}
			
			m_similar.put(active, all);
		}
	}


	public Map<Supplication, List<Supplication>> getSimilar() {
		return m_similar;
	}
}