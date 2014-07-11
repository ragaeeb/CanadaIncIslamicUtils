package com.canadainc.sunnah10;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.*;

import com.canadainc.islamicutils.io.NetworkBoundary;

public class SunnahDownloader
{
	private Map<String,Integer> m_map = new HashMap<String,Integer>(13);
	private Collection<String> m_languages = new ArrayList<String>(4);
	private int m_total = 0;
	
	public SunnahDownloader()
	{
		m_map.put("bukhari", 97);
		m_map.put("abudawud", 43);
		m_map.put("muslim", 56);
		m_map.put("nasai", 51);
		m_map.put("tirmidhi", 49);
		m_map.put("ibnmajah", 37);
		m_map.put("malik", 61);
		m_map.put("riyadussaliheen", 20);
		m_map.put("adab", 56);
		m_map.put("shamail", 56);
		m_map.put("nawawi40", 1);
		m_map.put("qudsi40", 1);
		
		m_languages.add("arabic");
		m_languages.add("english");
		
		for (int value: m_map.values() ) {
			m_total += value;
		}
		
		m_total *= m_languages.size();
		
		/*m_map.put("bulugh", 16);
		m_languages.add("indonesian");
		m_languages.add("urdu"); */
	}
	
	
	public void startDownloading() throws FileNotFoundException, UnsupportedEncodingException
	{
		Collection<String> collections = m_map.keySet();
		int count = 0;
		
		for (String book: collections)
		{
			File dir = new File("res/"+book);
			
			if ( !dir.exists() && !dir.mkdir() ) {
				System.out.println("Couldn't create res/"+book);
				return;
			}
			
			for (String language: m_languages)
			{
				dir = new File("res/"+book+"/"+language);
				
				if ( !dir.exists() && !dir.mkdir() ) {
					System.out.println("Couldn't create res/"+book+"/"+language);
					return;
				}
				
				int entries = m_map.get(book);
				
				for (int page = 1; page <= entries; page++)
				{
					try {
						++count;
						generatePage(book, language, entries, page, count);
					} catch (IOException ex) {
						System.out.println("Not found, skipping...");
					}
				}
				
				if ( book.equals("ibnmajah") || book.equals("muslim") ) {
					++count;
					generatePage(book, language, entries, -1, count);
				}
			}
		}
	}


	private boolean generatePage(String book, String language, int entries, int page, int count) throws FileNotFoundException, UnsupportedEncodingException
	{
		File dir;
		String fileName = page+".txt";
		dir = new File("res/"+book+"/"+language+"/"+fileName);
		
		if ( !dir.exists() )
		{
			String content = NetworkBoundary.getHTML("http://sunnah.com/ajax/"+language+"/"+book+"/"+page);
			
			if ( !content.isEmpty() )
			{
				PrintWriter out = new PrintWriter(dir, "UTF-8");
				out.println(content);
				out.close();
				System.out.println("written ("+language+","+book+") "+page+"/"+entries+"; "+count+"/"+m_total);
			} else { // most likely doesn't have this language
				return false;
			}
		}
		
		return true;
	}
}