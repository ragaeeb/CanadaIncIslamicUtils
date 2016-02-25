package com.canadainc.sunnah10;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import com.canadainc.common.io.IOUtils;

public class SunnahPopulator
{
	private Map< String, Map<String, String> > m_translations;

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

	public SunnahPopulator()
	{
		Map<String, String> english = new HashMap<String, String>();
		english.put("bukhari", "Dr. M. Muhsin Khan");
		english.put("muslim", "Abdul Hamid Siddiqui");

		m_translations = new HashMap<String, Map<String, String> >(1);
		m_translations.put("english", english);
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
		Collection<Narration> narrations = new ArrayList<Narration>(200);
		File root = new File("res/sunnah10");
		File[] languages = root.listFiles(sf);
		NarrationDecorator nd = new NarrationDecorator();
		RijaalCollector rc = new RijaalCollector();

		for (File l: languages)
		{
			File[] collections = l.listFiles(sf);
			Map<String, String> translators = m_translations.get( l.getName() );
			boolean arabic = l.getName().equals("arabic");
			SunnahReader reader = new SunnahReader( arabic ? "arabicURN" : "englishURN" );

			for (File c: collections)
			{
				File[] chapters = c.listFiles(pf);
				String translator = null;
				String collection = c.getName();

				if (translators != null) {
					translator = translators.get(collection);
				}

				for (File chapter: chapters)
				{
					Collection<Narration> currentNarrations = reader.readNarrations(chapter);
					nd.decorate(currentNarrations, arabic, collection);
					rc.process(currentNarrations, arabic, collection);

					for (Narration n: currentNarrations) {
						n.translator = translator;
					}

					narrations.addAll(currentNarrations);
				}
			}
		}

		System.out.println( "Collected "+narrations.size() );

		printNarrators(rc);
		//printBody(narrations);
	}

	private void printNarrators(RijaalCollector rc)
	{
		List<String> rijaal = new ArrayList<String>();
		rijaal.addAll( rc.getCollected() );
		Collections.sort(rijaal);

		StringBuffer sb = new StringBuffer();

		for (String s: rijaal) {
			sb.append(s+"\n");
		}

		IOUtils.writeFile("res/sunnah10/output.txt", sb.toString().trim());
	}


	private void printBody(Collection<Narration> narrations)
	{
		StringBuffer sb = new StringBuffer();

		for (Narration n: narrations) {
			sb.append(n.babNumber+"; "+n.text+"\n");
		}

		IOUtils.writeFile("res/sunnah10/output.txt", sb.toString());
	}
}