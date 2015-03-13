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
	private String m_similarPath;
	private String[] m_translations;
	private boolean m_applyImages;
	
	public QuranPopulator(String arabicXml, String supplications, String dbPath, String similarPath, String[] translationPaths)
	{
		m_arabicXml = arabicXml;
		m_supplicationsPath = supplications;
		m_dbPath = dbPath;
		m_similarPath = similarPath;
		m_translations = translationPaths;
		m_applyImages = false;
	}
	
	
	public void setApplyImages(boolean applyImages)
	{
		m_applyImages = applyImages;
	}


	public QuranPopulator(String arabicXml, String supplications, String similarPath, String dbPath)
	{
		this(arabicXml, supplications, dbPath, similarPath, new String[0]);
	}
	
	public void process() throws SQLException, IOException, ParserConfigurationException, SAXException, ClassNotFoundException
	{
		QuranArabicExtractor qae = new QuranArabicExtractor(m_arabicXml);
		qae.load();
		
		File f = new File(m_dbPath);
		f.delete();

		QuranArabicBoundary qb = new QuranArabicBoundary(m_dbPath);
		
		System.out.println("create tables...");
		qb.createTable(m_applyImages);
		
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
		
		System.out.println("populate recitations...");
		qb.populateRecitations("res/quran10/qarees.csv", "res/quran10/recitations.csv");
		
		if (m_applyImages)
		{
			System.out.println("populate images...");
			qb.populateImages("res/quran10/ayats");
		}
		
		System.out.println("populate similar...");
		SimilarParser sp = new SimilarParser(m_similarPath);
		sp.parse();
		qb.populateSimilar( sp.getSimilar() );
		
		System.out.println("remove basmalahs...");
		qb.execute("UPDATE ayahs SET content=TRIM(REPLACE(content, 'بِسْمِ ٱللَّهِ ٱلرَّحْمَٰنِ ٱلرَّحِيمِ', '')) WHERE content LIKE 'بِسْمِ ٱللَّهِ ٱلرَّحْمَٰنِ ٱلرَّحِيمِ%' AND verse_number=1 AND surah_id != 1 AND surah_id != 9");
		qb.execute("UPDATE ayahs SET searchable=TRIM(REPLACE(searchable, 'بسم الله الرحمن الرحيم', '')) WHERE searchable LIKE 'بسم الله الرحمن الرحيم%' AND verse_number=1 AND surah_id != 1 AND surah_id != 9");
		qb.execute("UPDATE ayahs SET content=TRIM(REPLACE(content, 'بِّسْمِ ٱللَّهِ ٱلرَّحْمَٰنِ ٱلرَّحِيمِ', '')) WHERE content LIKE 'بِّسْمِ ٱللَّهِ ٱلرَّحْمَٰنِ ٱلرَّحِيمِ%' AND verse_number=1 AND (surah_id=95 OR surah_id=97)");
		//qb.execute("UPDATE ayahs SET content=TRIM(REPLACE(content, 'بِّسْمِ ٱللَّهِ ٱلرَّحْمَٰنِ', '')) WHERE content LIKE 'بِّسْمِ ٱللَّهِ ٱلرَّحْمَٰنِ%' AND verse_number=1 AND surah_id = 97");
		
		System.out.println("Creating indices...");
		qb.createIndices(m_applyImages);
		
		System.out.println("vacuum... ");
		qb.execute("VACUUM");
		qb.getConnection().close();
		
		for (String translation: m_translations)
		{
			String translationPath = "res/quran10/quran_"+translation+".db";
			
			f = new File(translationPath);
			f.delete();
			
			System.out.println("populate... "+translation);
			QuranTranslationBoundary qtb = new QuranTranslationBoundary(translationPath);
			qtb.createTable();
			qtb.populateChapters( qae.getEnglish() );
			qtb.populateVerses("translation_source", translation);
			qtb.createIndices();
			System.out.println("vacuum... ");
			qtb.execute("VACUUM");
			qtb.getConnection().close();
		}
	}
}