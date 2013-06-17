package com.canadainc.sunnah10;

import java.io.*;
import java.util.*;

import org.json.simple.*;
import org.jsoup.Jsoup;

import com.canadainc.islamicutils.io.NetworkBoundary;

public class HadithAnalyzer
{
	/**
	 * @param args
	 * @throws IOException 
	 */
	public String analyze(File f) throws IOException
	{
		Collection<Hadith> hadiths = new ArrayList<Hadith>();
		
		for (int i = 1; i <= 43; i++)
		{
			String content = NetworkBoundary.getHTML("http://sunnah.com/ajax/arabic/abudawud/"+i);
			Object obj = JSONValue.parse(content);
			JSONArray array = (JSONArray)obj;
			
			for (int j = 0; j < array.size(); j++)
			{
				Hadith h = new Hadith();
				
				JSONObject json = (JSONObject)array.get(j);
				h.volumeNumber = Integer.parseInt( (String)json.get("volumeNumber") );
				h.bookNumber = Integer.parseInt( (String)json.get("bookNumber") );
				h.bookName = (String)json.get("bookName");
				h.hadithNumber = (String)json.get("hadithNumber");
				h.collection = (String)json.get("collection");
				h.bookReference = Integer.parseInt( (String)json.get("ourHadithNumber") );
				h.chapterName = (String)json.get("babName");
				h.grade = (String)json.get("grade1");
				h.chapterNumber = json.get("babNumber") != null ? Double.parseDouble( (String)json.get("babNumber") ) : 0;
				h.id = Integer.parseInt( (String)json.get("arabicURN") );
				h.foreignId = Integer.parseInt( (String)json.get("matchingEnglishURN") );
				
				String text = (String)json.get("hadithText");
				text = Jsoup.parse(text).text();
				text = text.replaceAll("\\(\\)", "(sallahu alayhi wa'sallam)");
				h.text = text;
				
				hadiths.add(h);
			}	
		}
		
		try {
			HadithSample.run(hadiths);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		return "";
	}
}