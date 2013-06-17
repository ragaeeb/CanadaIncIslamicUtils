package com.canadainc.quran10.ibnkatheer;

import java.io.*;
import java.util.*;

public class DirectoryAnalyzer
{
	public File[] getAllFiles()
	{
		File folder = new File("./ibnkatheer");
		File[] listOfFiles = folder.listFiles( new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".html");
			}
		});

		Arrays.sort(listOfFiles, new Comparator<File>()
		{
			private static final String regex = "\\d+.\\d+.html";
			
			@Override
			public int compare(File f1, File f2)
			{
				String n1 = f1.getName();
				String n2 = f2.getName();
				
				if ( n1.matches(regex) && n2.matches(regex) ) {
					int firstDot = n1.indexOf(".");
					int lastDot = n1.lastIndexOf(".");
					int firstVerse = Integer.parseInt( n1.substring(firstDot+1, lastDot) );
					int firstChapter = Integer.parseInt( n1.substring(0, firstDot) );
					
					firstDot = n2.indexOf(".");
					lastDot = n2.lastIndexOf(".");
					int secondVerse = Integer.parseInt( n2.substring(firstDot+1, lastDot) );
					int secondChapter = Integer.parseInt( n2.substring(0, firstDot) );
					
					if (firstChapter < secondChapter) {
						return -1;
					} else if (firstChapter > secondChapter) {
						return 1;
					} else {
						if (firstVerse < secondVerse) {
							return -1;
						} else if (firstVerse > secondVerse) {
							return 1;
						}
						
						return 0;
					}
				} else {
					return n1.compareTo(n2);
				}
			}
		});

		return listOfFiles;
	}
}