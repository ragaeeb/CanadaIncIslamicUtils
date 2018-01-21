package com.canadainc.islamicutils.controller;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

import org.apache.commons.lang3.StringUtils;

import com.canadainc.common.io.IOUtils;
import com.canadainc.quran10.AyatDownloader;
import com.canadainc.quran10.QuranArabicExtractor;
import com.canadainc.quran10.QuranPopulator;
import com.canadainc.quran10.Transliterator;
import com.canadainc.quran10.ibnkatheer.DirectoryAnalyzer;
import com.canadainc.quran10.ibnkatheer.TafsirController;

public class Application
{
	public static void testTransliteration()
	{
		Transliterator instance = new Transliterator("en.transliteration.txt", "translated.txt");

		// translate a line
		String line = "1|1|Bismi All<u>a</U>hi a<b>l</B>rra<u>h</U>m<u>a</U>ni a<b>l</B>rra<u>h</U>eem<b>i</b>";
		line = "2|7|Khatama All<u>a</u>hu AAal<u>a</u> quloobihim waAAal<u>a</u> samAAihim waAAal<u>a</u> ab<u>sa</u>rihim ghish<u>a</u>watun walahum AAa<u>tha</u>bun AAa<i><u>th</u></i>eem<b>un</b>";
		instance.translateLine(line);

		// translate full
		try {
			System.out.println( instance.translate() );
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public static void testDirectoryAnalysis()
	{
		DirectoryAnalyzer instance = new DirectoryAnalyzer();
		instance.getAllFiles();
	}


	public static void testTafsirIbnKatheer()
	{
		TafsirController instance = new TafsirController();
		instance.run();
	}


	public static void testPopulateArabicQuran()
	{
		QuranPopulator qap = new QuranPopulator("res/quran10/quran-data.xml", "res/quran10/supplications.csv", "res/quran10/quran_arabic.db", "res/quran10/similar.txt", new String[]{/*"turkish", "russian","malay"*/});

		try {
			qap.process();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public static void testDownloadAyatImages()
	{
		try {
			AyatDownloader a = new AyatDownloader( new QuranArabicExtractor("res/quran10/quran-data.xml"), true );
			a.beginDownload();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public static void main(String[] args)
	{
		//testBulugh();
		//testPopulateArabicQuran();
		//testDownloadAyatImages();
		/*try
		{
			collectWhatsApps();
		} catch (IOException e)
		{
			e.printStackTrace();
		} */
	}

	
	private static void collectWhatsApps() throws IOException
	{
		String[] numbers = IOUtils.readFileUtf8( new File("res/users.txt") ).split("\n");
		HashSet<String> allNumbers = new HashSet<String>();

		for (String number: numbers) {
			allNumbers.add(number);
		}
		
		File[] all = new File("res").listFiles( new FilenameFilter()
		{
			@Override
			public boolean accept(File dir, String name)
			{
				return dir.isDirectory() && name.matches("^\\d+$");
			}
		});
		
		for (File f: all)
		{
			File[] whatsappFiles = f.listFiles( new FilenameFilter()
			{
				@Override
				public boolean accept(File dir, String name) {
					return name.equals("app_status");
				}
			});
			
			for (File x: whatsappFiles)
			{
				String a = IOUtils.readFileUtf8(x);
				int start = a.indexOf("MyJid::");
				int end = a.indexOf("@s.whatsapp.net", start);
				
				if (start >= 0 && end > start)
				{
					a = a.substring(start+"MyJid::".length(), end);
					allNumbers.add(a);
				}
			}
		}
		
		ArrayList<String> sorted = new ArrayList<String>();
		sorted.addAll(allNumbers);
		Collections.sort(sorted);
		
		IOUtils.writeFile("res/users.txt", StringUtils.join(sorted, "\n") );
	}
	

	private static void testBulugh()
	{
		try
		{ /*
			BulughMaramParser bmp = new BulughMaramParser();
			List<Narration> narrations = bmp.readStaticBulugh( new File("res/sunnah10/static/english/bulugh") );
			
			//bmp.createDB(narrations);
			List<Narration> narrations2 = bmp.readStaticBulughArabic( new File("res/sunnah10/static/arabic/bulugh/assets") );

			System.out.println(narrations2.size()+"; "+narrations.size());
/*
			int k = Math.min(narrations.size(), narrations2.size());
			StringBuffer sb = new StringBuffer();

			for (int i = 0; i < k; i++) {
				sb.append( narrations2.get(i).text+"\n"+narrations.get(i).text+"\n\n\n" );
			}

			Writer out = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream("res/result.txt"), "UTF-8"));
			try {
				out.write( sb.toString() );
			} finally {
				out.close();
			} */
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}