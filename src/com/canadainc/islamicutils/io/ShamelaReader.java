package com.canadainc.islamicutils.io;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.Comparator;

public class ShamelaReader
{
	public static File[] getOrderedFiles(String folder)
	{
		File[] all = new File(folder).listFiles( new FilenameFilter()
		{
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".txt");
			}
		});

		Arrays.sort(all, new Comparator<File>()
		{
			@Override
			public int compare(File f1, File f2)
			{
				int i = Integer.parseInt( f1.getName().split("\\.")[0] );
				int j = Integer.parseInt( f2.getName().split("\\.")[0] );

				return i < j ? -1 : 1;
			}
		});

		return all;
	}
}