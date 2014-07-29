package com.canadainc.sunnah10;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import com.canadainc.common.io.IOUtils;

public class SunnahPopulator
{
	/**
	 * @param args
	 * @throws IOException 
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	public void process() throws Exception
	{
		File root = new File("res");
		File[] collections = root.listFiles();;
		int total = 24;
		int current = 0;

		for (File f: collections)
		{
			if ( f.isDirectory() )
			{
				String collectionName = f.getName();

				File[] languages = f.listFiles();

				for (File l: languages)
				{
					if ( l.isDirectory() )
					{
						String language = l.getName();

						File[] chapters = l.listFiles( new FilenameFilter()
						{
							public boolean accept(File dir, String name) {
								return name.endsWith(".txt");
							}
						});

						Collection<String> chapterContents = new ArrayList<String>(chapters.length);

						for (File chapter: chapters)
						{
							String content = IOUtils.readFileUtf8(chapter);
							chapterContents.add(content);
						}
						
						++current;
						System.out.println(current+"/"+total);
						if (/* collectionName.equals("nasai") */true)
						{
							SunnahBoundary sb = new SunnahBoundary(language, collectionName);
							sb.createTable();
							sb.populate(chapterContents);
						}
					}
				}
			}
		}
		
		System.out.println("Populating Chapters...");
		SunnahBookManager sbm = new SunnahBookManager();
		sbm.attachDatabases();
		sbm.populateChapters();
		
		System.out.println("Dropping book names 1/2...");
		SunnahBoundary sb = new SunnahBoundary("arabic", "");
		sb.removeBookName();
		
		System.out.println("Dropping book names 2/2...");
		sb = new SunnahBoundary("english", "");
		sb.removeBookName();
		/*
		System.out.println("Vacuuming 1/2!");
		sb = new SunnahBoundary("arabic", "");
		sb.vacuum();
		
		System.out.println("Vacuuming 2/2!");
		sb = new SunnahBoundary("english", "");
		sb.vacuum(); */
	}
}