package com.canadainc.sunnah10;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import com.canadainc.common.io.IOUtils;

public class SunnahCollectionsReader
{
	private String m_path;
	private Collection<String> m_languages;
	
	public SunnahCollectionsReader(String path)
	{
		m_path = path;
		m_languages = new HashSet<String>();
	}
	
	
	void addLanguage(String language) {
		m_languages.add(language);
	}
	
	
	/**
	 * @param args
	 * @throws IOException 
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	public void process() throws Exception
	{
		File root = new File(m_path);
		File[] collections = root.listFiles();;

		for (File f: collections)
		{
			if ( f.isDirectory() )
			{
				File[] languages = f.listFiles();

				for (File l: languages)
				{
					if ( l.isDirectory() && m_languages.contains( l.getName() ) )
					{
						File[] chapters = l.listFiles( new FilenameFilter()
						{
							public boolean accept(File dir, String name) {
								return name.endsWith(SunnahConstants.CHAPTER_EXTENSION);
							}
						});

						Collection<String> chapterContents = new ArrayList<String>(chapters.length);

						for (File chapter: chapters)
						{
							String content = IOUtils.readFileUtf8(chapter);
							chapterContents.add(content);
						}
					}
				}
			}
		}
	}
}