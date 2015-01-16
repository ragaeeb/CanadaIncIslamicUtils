package com.canadainc.quran10;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class QuranArabicExtractor
{
	private String m_inputXml;
	private Map<Integer,SurahMetadata> m_metadata;

	public QuranArabicExtractor(String inputXml)
	{
		m_inputXml = inputXml;
	}


	public void load() throws IOException, ParserConfigurationException, SAXException
	{
		m_metadata = new HashMap<Integer, SurahMetadata>(114);

		File fXmlFile = new File(m_inputXml);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(fXmlFile);
		doc.getDocumentElement().normalize();

		parseSuras(doc);
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

				System.out.println(sm);
			}
		}
	}
	
	
	private void parseJuzs(Document doc)
	{
		NodeList nList = doc.getElementsByTagName("juzs");
		Element nNode = (Element)nList.item(0);
		nList = nNode.getElementsByTagName("juz");

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

				System.out.println(sm);
			}
		}
	}
}