package com.canadainc.quran10;

public class SurahAyat
{
	public int chapter;
	public int verse;
	
	public SurahAyat()
	{
	}
	
	public SurahAyat(int chapter, int verse)
	{
		this.chapter = chapter;
		this.verse = verse;
	}
	
	
	@Override
	public String toString()
	{
		return "chapter="+chapter+"&verse="+verse;
	}

	@Override
	public int hashCode()
	{
		return chapter+verse+5;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof SurahAyat) {
			SurahAyat sa = (SurahAyat)obj;
			return sa.chapter == chapter && sa.verse == verse;
		}
		
		return false;
	}
}
