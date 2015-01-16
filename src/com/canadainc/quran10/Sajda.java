package com.canadainc.quran10;

public class Sajda
{
	public int chapter;
	public int verse;
	public SajdaType type;

	
	public Sajda()
	{
	}
	
	
	public Sajda(int chapter, int verse, SajdaType type)
	{
		this.chapter = chapter;
		this.verse = verse;
		this.type = type;
	}
	
	
	public static enum SajdaType
	{
		Recommended,
		Obligatory
	}
}