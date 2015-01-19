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
	
	
	@Override
	public int hashCode() {
		return chapter+verse+type.hashCode()+13;
	}


	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof Sajda) {
			Sajda s = (Sajda)obj;
			return s.chapter == chapter && s.verse == verse && s.type.equals(type);
		}
		
		return false;
	}


	@Override
	public String toString() {
		return "chapter="+chapter+"&verse="+verse+"&type="+type;
	}


	public static enum SajdaType
	{
		Recommended,
		Obligatory
	}
}