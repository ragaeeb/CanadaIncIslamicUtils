package com.canadainc.quran10;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.canadainc.quran10.Sajda.SajdaType;

public class QuranArabicExtractor
{
	private Map<Integer,SurahAyat> m_hizbs;
	private String m_inputXml;
	private Map<Integer,SurahAyat> m_juzs;
	private Map<Integer,SurahAyat> m_manzils;
	private Map<Integer,SurahMetadata> m_metadata;
	private Map<Integer,SurahAyat> m_mushafPages;
	private Map<Integer,SurahAyat> m_rukus;
	private ArrayList<Sajda> m_sajdas;
	private Map<Integer,ChapterTranslation> m_english;

	public QuranArabicExtractor(String inputXml)
	{
		m_inputXml = inputXml;
	}


	public void load() throws IOException, ParserConfigurationException, SAXException
	{
		m_metadata = new HashMap<Integer, SurahMetadata>(114);
		m_english = new HashMap<Integer, ChapterTranslation>(114);
		m_juzs = new HashMap<Integer, SurahAyat>(30);
		m_hizbs = new HashMap<Integer, SurahAyat>(240);
		m_manzils = new HashMap<Integer, SurahAyat>(7);
		m_mushafPages = new HashMap<Integer, SurahAyat>(604);
		m_rukus = new HashMap<Integer, SurahAyat>(556);
		m_sajdas =  new ArrayList<Sajda>(15);

		File fXmlFile = new File(m_inputXml);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(fXmlFile);
		doc.getDocumentElement().normalize();

		parseSuras(doc);
		parseSurahAyat(doc, "juzs", "juz", m_juzs);
		parseSurahAyat(doc, "hizbs", "quarter", m_hizbs);
		parseSurahAyat(doc, "manzils", "manzil", m_manzils);
		parseSurahAyat(doc, "pages", "page", m_mushafPages);
		parseSurahAyat(doc, "rukus", "ruku", m_rukus);
		parseSajdas(doc);
		
		/**
		 * from_surah,from_verse,to_surah,to_verse
		 * 1,1,2,141
		 * 
		 * 
		 * juz,from_surah_id,from_verse,to_surah_id
		 * 1,  1, 1
		 */
	}
	
	
	public Map<Integer, SurahAyat> getHizbs()
	{
		return m_hizbs;
	}


	public Map<Integer, SurahAyat> getJuzs()
	{
		return m_juzs;
	}


	public Map<Integer, SurahAyat> getManzils()
	{
		return m_manzils;
	}


	public Map<Integer, SurahMetadata> getMetadata()
	{
		return m_metadata;
	}


	public Map<Integer, SurahAyat> getMushafPages()
	{
		return m_mushafPages;
	}


	public Map<Integer, SurahAyat> getRukus()
	{
		return m_rukus;
	}


	public ArrayList<Sajda> getSajdas()
	{
		return m_sajdas;
	}


	public Map<Integer, ChapterTranslation> getEnglish()
	{
		return m_english;
	}


	private void parseSajdas(Document doc)
	{
		NodeList nList = doc.getElementsByTagName("sajdas");
		Element nNode = (Element)nList.item(0);
		nList = nNode.getElementsByTagName("sajda");

		for (int i = 0; i < nList.getLength(); i++)
		{
			Node x = nList.item(i);

			if ( x.getNodeType() == Node.ELEMENT_NODE )
			{
				Element eElement = (Element) x;

				Sajda sm = new Sajda();
				sm.chapter = Integer.parseInt( eElement.getAttribute("sura") );
				sm.verse = Integer.parseInt( eElement.getAttribute("aya") );
				sm.type = SajdaType.valueOf( StringUtils.capitalize( eElement.getAttribute("type") ) );

				m_sajdas.add(sm);
			}
		}
	}


	private void parseSuras(Document doc)
	{
		NodeList nList = doc.getElementsByTagName("suras");
		Element nNode = (Element)nList.item(0);
		nList = nNode.getElementsByTagName("sura");

		for (int i = 0; i < nList.getLength(); i++)
		{
			Node x = nList.item(i);

			if ( x.getNodeType() == Node.ELEMENT_NODE )
			{
				Element eElement = (Element) x;

				SurahMetadata sm = new SurahMetadata();
				sm.verseCount = Integer.parseInt( eElement.getAttribute("ayas") );
				sm.verseStart = Integer.parseInt( eElement.getAttribute("start") );
				sm.rukus = Integer.parseInt( eElement.getAttribute("rukus") );
				sm.revelationOrder = Integer.parseInt( eElement.getAttribute("order") );
				sm.name = eElement.getAttribute("name");
				sm.type = SurahMetadata.RevealedLocation.valueOf( eElement.getAttribute("type") );

				int chapter = Integer.parseInt( eElement.getAttribute("index") );
				m_metadata.put(chapter, sm);
				
				ChapterTranslation english = new ChapterTranslation();
				english.translation = eElement.getAttribute("ename");
				english.transliteration = eElement.getAttribute("tname");
				m_english.put(chapter, english);
			}
		}
	}
	
	
	private static void parseSurahAyat(Document doc, String parentTag, String childTag, Map<Integer, SurahAyat> result)
	{
		NodeList nList = doc.getElementsByTagName(parentTag);
		Element nNode = (Element)nList.item(0);
		nList = nNode.getElementsByTagName(childTag);

		for (int i = 0; i < nList.getLength(); i++)
		{
			Node x = nList.item(i);

			if ( x.getNodeType() == Node.ELEMENT_NODE )
			{
				Element eElement = (Element) x;

				SurahAyat sm = new SurahAyat();
				sm.chapter = Integer.parseInt( eElement.getAttribute("sura") );
				sm.verse = Integer.parseInt( eElement.getAttribute("aya") );

				int index = Integer.parseInt( eElement.getAttribute("index") );
				result.put(index, sm);
			}
		}
	}
}