package com.canadainc.quran10;

public class SurahMetadata
{
	public String name;
	public int verseCount;
	public int verseStart;
	public RevealedLocation type;
	public int revelationOrder;
	public int rukus;
	
	
	public SurahMetadata()
	{
	}


	public SurahMetadata(String name, int verseCount, int verseStart, RevealedLocation type, int revelationOrder, int rukus)
	{
		this.name = name;
		this.verseCount = verseCount;
		this.verseStart = verseStart;
		this.type = type;
		this.revelationOrder = revelationOrder;
		this.rukus = rukus;
	}


	@Override
	public int hashCode() {
		return name.hashCode()+verseCount+verseStart+type.hashCode()+revelationOrder+rukus+8;
	}


	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof SurahMetadata) {
			SurahMetadata sm = (SurahMetadata)obj;
			return sm.name.equals(name) && sm.revelationOrder == revelationOrder && sm.rukus == rukus && sm.type.equals(type) && sm.verseCount == verseCount && sm.verseStart == verseStart;
		}
		
		return false;
	}


	public static enum RevealedLocation
	{
		Meccan,
		Medinan
	}


	@Override
	public String toString()
	{
		return "name="+name+"&verseCount="+verseCount+"&verseStart="+verseStart+"&type="+type+"&revelationOrder="+revelationOrder+"&rukus="+rukus;
	}
}