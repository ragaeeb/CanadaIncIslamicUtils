package com.canadainc.quran10;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

public class QuranPopulator
{
	private String m_arabicXml;
	private String m_supplicationsPath;
	private String m_dbPath;
	
	public QuranPopulator(String arabicXml, String supplications, String dbPath)
	{
		m_arabicXml = arabicXml;
		m_supplicationsPath = supplications;
		m_dbPath = dbPath;
	}
	
	public void process() throws SQLException, IOException, ParserConfigurationException, SAXException, ClassNotFoundException
	{
		QuranArabicExtractor qae = new QuranArabicExtractor(m_arabicXml);
		qae.load();
		
		File f = new File(m_dbPath);
		f.delete();

		QuranArabicBoundary qb = new QuranArabicBoundary(m_dbPath);
		
		System.out.println("create tables...");
		qb.createTable();
		qb.createIndices();
		
		System.out.println("populate metadata...");
		qb.populateMetadata( qae.getMetadata() );
		
		System.out.println("populate divisions...");
		qb.populateHizbs( qae.getHizbs() );
		qb.populateJuzs( qae.getJuzs() );
		qb.populateManzils( qae.getManzils() );
		qb.populateMushafPages( qae.getMushafPages() );
		qb.populateRukus( qae.getRukus() );
		qb.populateSajdas( qae.getSajdas() );
		qb.populateSupplications( SupplicationCsvReader.load(m_supplicationsPath) );
		
		System.out.println("populate verses...");
		qb.populateVerses("source_uthmani", "source_clean");
	}
}