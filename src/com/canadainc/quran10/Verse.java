package com.canadainc.quran10;

public class Verse
{
	public int chapter;
	public int verseNumber;
	public String content;
	

	public Verse(int chapter, int verseNumber, String text)
	{
		super();
		this.chapter = chapter;
		this.verseNumber = verseNumber;
		this.content = text;
	}
	
	
	public Verse()
	{
	}
}