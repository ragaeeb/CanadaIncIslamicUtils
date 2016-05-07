package com.canadainc.sunnah10;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;

public class SunnahPopulator
{
	private BookCollector m_bookCollector;

	private GradeCollector m_gradeCollector;

	private ChapterCollector m_chapterCollector;

	private NarrationCollector m_narrationCollector;
	
	private BulughMaramDatabase m_bulugh;

	private SunnahDatabaseBoundary m_db;

	private String m_srcPath;

	private String m_language;


	public SunnahPopulator(String language, String sourcePath) throws SQLException
	{
		Dictionary d = new Dictionary();
		m_bookCollector = new BookCollector();
		m_gradeCollector = new GradeCollector();
		m_chapterCollector = new ChapterCollector();
		m_narrationCollector = new NarrationCollector();

		m_bookCollector.setDictionary(d);
		m_chapterCollector.setDictionary(d);
		m_narrationCollector.setDictionary(d);
		
		m_bulugh = new BulughMaramDatabase(sourcePath+"/static_bulugh.db");
		m_bulugh.setLanguage(language);

		m_language = language;
		m_srcPath = sourcePath;
		m_db = new SunnahDatabaseBoundary(sourcePath+"/sunnah_"+language+".db");
		m_db.setLanguage(language);
	}

	/**
	 * @param args
	 * @throws IOException 
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	public void process() throws Exception
	{
		PagesFilter pf = new PagesFilter();
		File root = new File(m_srcPath+"/"+m_language);
		File[] collections = root.listFiles( new CollectionFilter() );
		SunnahReader reader = new SunnahReader();

		long now = System.currentTimeMillis();
		System.out.println("Reading collections: "+m_language);

		for (File c: collections)
		{
			File[] chapters = c.listFiles(pf);
			String collection = c.getName();

			for (File chapter: chapters)
			{
				Collection<Narration> currentNarrations = reader.readNarrations(chapter);

				m_narrationCollector.process(currentNarrations, m_language, collection);
				m_bookCollector.process(currentNarrations, m_language, collection);
				m_gradeCollector.process(currentNarrations, m_language, collection);
				m_chapterCollector.process(currentNarrations, m_language, collection);
			}
		}
		
		Collection<Narration> currentNarrations = m_bulugh.process();
		m_narrationCollector.process(currentNarrations, m_language, SunnahConstants.COLLECTION_BULUGH_MARAM);
		m_bookCollector.process(currentNarrations, m_language, SunnahConstants.COLLECTION_BULUGH_MARAM);
		m_gradeCollector.process(currentNarrations, m_language, SunnahConstants.COLLECTION_BULUGH_MARAM);
		m_chapterCollector.process(currentNarrations, m_language, SunnahConstants.COLLECTION_BULUGH_MARAM);

		System.out.println("Collections loaded in memory: "+(System.currentTimeMillis()-now)+" ms");

		m_db.process( m_bookCollector.getCollected(), m_chapterCollector.getCollected(), m_gradeCollector.getCollected(), m_narrationCollector.getCollected() );
		System.out.println("Finished: "+m_language+"\n");
	}


	Connection getConnection() {
		return m_db.getConnection();
	}


	public void close() throws SQLException {
		m_db.close();
	}


	private class CollectionFilter implements FilenameFilter
	{
		private Collection<String> m_interested;

		public CollectionFilter()
		{
			m_interested = new HashSet<String>();
			m_interested.add("abudawud");
			m_interested.add("adab");
			m_interested.add("bulugh");
			m_interested.add("bukhari");
			m_interested.add("ibnmajah");
			m_interested.add("malik");
			m_interested.add("muslim");
			m_interested.add("nasai");
			m_interested.add("nawawi40");
			m_interested.add("qudsi40");
			m_interested.add("riyadussaliheen");
			m_interested.add("tirmidhi");
		}

		@Override
		public boolean accept(File dir, String name) {
			return dir.isDirectory() && m_interested.contains(name);
		}
	}

	private class PagesFilter implements FilenameFilter
	{
		@Override
		public boolean accept(File dir, String name) {
			return name.endsWith(".txt");
		}
	}
}