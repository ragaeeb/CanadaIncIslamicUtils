package com.canadainc.islamicutils.controller;

import java.io.File;
import java.io.IOException;

import com.canadainc.quran10.Transliterator;
import com.canadainc.quran10.ibnkatheer.DirectoryAnalyzer;
import com.canadainc.quran10.ibnkatheer.TafsirController;
import com.canadainc.sunnah10.HadithAnalyzer;

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
	
	
	public static void testHadith()
	{
		HadithAnalyzer m_instance = new HadithAnalyzer();
		
		try {
			File f = new File("test.html");
			m_instance.analyze(f);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public static void main(String[] args)
	{
		
	}
}