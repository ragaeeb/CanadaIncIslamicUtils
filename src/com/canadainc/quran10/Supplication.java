package com.canadainc.quran10;

public class Supplication
{
	public int chapter;
	public int verseStart;
	public int verseEnd;
	
	
	public Supplication()
	{
	}


	@Override
	public int hashCode()
	{
		return chapter+verseStart+verseEnd+53;
	}


	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof Supplication) {
			Supplication s = (Supplication)obj;
			return s.chapter == chapter && s.verseEnd == verseEnd && s.verseStart == verseStart;
		}
		
		return false;
	}


	@Override
	public String toString()
	{
		return "chapter="+chapter+"&verseStart"+verseStart+"&verseEnd="+verseEnd;
	}


	public Supplication(int chapter, int verseStart, int verseEnd)
	{
		this.chapter = chapter;
		this.verseStart = verseStart;
		this.verseEnd = verseEnd;
	}
}