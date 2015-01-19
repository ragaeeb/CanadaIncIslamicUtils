package com.canadainc.quran10;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.canadainc.common.io.IOUtils;
import com.canadainc.common.text.TextUtils;

public class SupplicationCsvReader
{
	public static List<Supplication> load(String input) throws IOException
	{
		List<Supplication> result = new ArrayList<Supplication>();
		String contents = IOUtils.readFileUtf8( new File(input) );
		String[] tokens = contents.split("\n");
		
		for (int i = 1; i < tokens.length; i++)
		{
			String line = tokens[i].trim();
			
			String[] elements = line.split(",");
			
			if (elements.length > 2)
			{
				int chapter = parseIntegerValue(elements[0]);
				int start = parseIntegerValue(elements[1]);
				int end = parseIntegerValue(elements[2]);
				
				result.add( new Supplication(chapter,start,end) );
			}
		}
		
		return result;
	}
	
	
	private static int parseIntegerValue(String input) {
		return Integer.parseInt( TextUtils.removeQuotes(input) );
	}
}