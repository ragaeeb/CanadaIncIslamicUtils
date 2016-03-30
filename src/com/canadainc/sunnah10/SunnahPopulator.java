package com.canadainc.sunnah10;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SunnahPopulator
{
	/** (Key: Language, Value: [<Key: Collection, Value: Translator>]) */
	private Map< String, Map<String, Integer> > m_translations;

	private BookCollector m_bookCollector;

	private GradeCollector m_gradeCollector;

	private ChapterCollector m_chapterCollector;

	private NarrationCollector m_narrationCollector;
	

	public SunnahPopulator()
	{
		
		
		Map<String, Integer> english = new HashMap<String, Integer>();
		english.put("bukhari", -10); // Dr. M. Muhsin Khan
		english.put("muslim", -11); // Abdul Hamid Siddiqui
		english.put("abudawud", -12); // Ahmad Hasan
		english.put("malik", -13); // `A'isha `Abdarahman at-Tarjumana and Ya`qub Johnson
		english.put("adab", -14); // Muhammad b. Isma'il al- Bujari

		m_translations = new HashMap<String, Map<String, Integer> >(1);
		m_translations.put("english", english);

		Dictionary d = new Dictionary();
		m_bookCollector = new BookCollector();
		m_gradeCollector = new GradeCollector();
		m_chapterCollector = new ChapterCollector();
		m_narrationCollector = new NarrationCollector();

		m_bookCollector.setDictionary(d);
		m_chapterCollector.setDictionary(d);
		m_narrationCollector.setDictionary(d);
	}

	/**
	 * @param args
	 * @throws IOException 
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	public void process() throws Exception
	{
		CollectionFilter sf = new CollectionFilter();
		PagesFilter pf = new PagesFilter();
		File root = new File("res/sunnah10");
		File[] languages = root.listFiles(sf);

		for (File l: languages)
		{
			File[] collections = l.listFiles(sf);
			Map<String, Integer> translators = m_translations.get( l.getName() );
			boolean arabic = l.getName().equals("arabic");
			SunnahReader reader = new SunnahReader( arabic ? "arabicURN" : "englishURN" );

			for (File c: collections)
			{
				File[] chapters = c.listFiles(pf);
				int translator = 0;
				String collection = c.getName();

				if ( translators != null && translators.containsKey(collection) ) {
					translator = translators.get(collection);
				}

				for (File chapter: chapters)
				{
					Collection<Narration> currentNarrations = reader.readNarrations(chapter);

					for (Narration n: currentNarrations) {
						n.translator = translator;
					}

					m_narrationCollector.process(currentNarrations, arabic, collection);
					//m_narratorCollector.process(currentNarrations, arabic, collection);
					m_bookCollector.process(currentNarrations, arabic, collection);
					m_gradeCollector.process(currentNarrations, arabic, collection);
					m_chapterCollector.process(currentNarrations, arabic, collection);
				}
			}
		}
	}


	public Map< String, Set<Book> > getBooks() {
		return m_bookCollector.getCollected();
	}


	public Map<Integer, Grade> getGrades() {
		return m_gradeCollector.getCollected();
	}


	public Collection<Chapter> getChapters() {
		return m_chapterCollector.getCollected();
	}

	public Map<String, Collection<Narration>> getNarrations() {
		return m_narrationCollector.getCollected();
	}
	
	private class CollectionFilter implements FilenameFilter
	{
		private Collection<String> m_interested;

		public CollectionFilter()
		{
			m_interested = new HashSet<String>();
			//m_interested.add("arabic");
			m_interested.add("english");
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