package com.canadainc.quran10;

public class Supplication
{
	public int chapter;
	public int verseStart;
	public int verseEnd;
	
	
	public Supplication()
	{
	}
	
	
	public Supplication(int chapter, int verseStart, int verseEnd)
	{
		this.chapter = chapter;
		this.verseStart = verseStart;
		this.verseEnd = verseEnd;
	}
}