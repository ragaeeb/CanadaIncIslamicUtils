package com.canadainc.quran10.ibnkatheer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.canadainc.sunnah10.Sample;

public class TafsirController
{
	public void run()
	{
		DirectoryAnalyzer dir = new DirectoryAnalyzer();
		File[] files = dir.getAllFiles();

		FileAnalyzer fa = new FileAnalyzer();
		
		Map< String, List<VerseExplanation> > map = new HashMap< String, List<VerseExplanation> >(114);
		
		for (int i = 0; i < files.length; i++)
		{
			try {
				VerseExplanation exp = fa.analyze( files[i] );

				if (exp != null)
				{
					String current = files[i].getName();
					current = current.substring( 0, current.indexOf(".") );
					
					List<VerseExplanation> explanations = map.get(current);
					
					if (explanations == null) {
						explanations = new ArrayList<VerseExplanation>();
						map.put(current, explanations);
					}
					
					explanations.add(exp);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}			
		}
		
		try {
			Sample.run(map);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}