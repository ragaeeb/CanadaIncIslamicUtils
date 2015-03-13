package com.canadainc.quran10;

import java.io.IOException;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.canadainc.islamicutils.io.NetworkBoundary;


public class AyatDownloader
{
	private QuranArabicExtractor m_extractor;
	private static final String QURAN_DOT_COM_HOST = "http://c00022506.cdn1.cloudfiles.rackspacecloud.com/";
	private static final String EVERY_AYAH_HOST = "http://www.everyayah.com/data/quranpngs/";
	private String m_host;
	
	public AyatDownloader(QuranArabicExtractor extractor, boolean highQuality)
	{
		m_extractor = extractor;
		m_host = highQuality ? EVERY_AYAH_HOST : QURAN_DOT_COM_HOST;
	}
	
	
	public void beginDownload() throws IOException, ParserConfigurationException, SAXException
	{
		m_extractor.load();
		Map<Integer, SurahMetadata> surahToData = m_extractor.getMetadata();
		
		for ( int surahId: surahToData.keySet() )
		{
			int n = surahToData.get(surahId).verseCount;
			
			for (int i = 1; i <= n; i++)
			{
				try {
					String name = surahId+"_"+i+".png";
					NetworkBoundary.saveFile(m_host+name, "res/quran10/ayats/"+name);
				} catch (IOException e) {
					System.out.println(surahId+","+i);
					e.printStackTrace();
				}
			}
			
			System.out.println(surahId+" complete...");
		}
	}
}