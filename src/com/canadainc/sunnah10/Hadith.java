package com.canadainc.sunnah10;

public class Hadith
{
	public int bookNumber;
	public int volumeNumber;
	public String bookName;
	public String hadithNumber;
	public String collection;
	public String text;
	public int bookReference;
	public String chapterName;
	public String grade;
	public double chapterNumber;
	public int id;
	public int foreignId;
	@Override
	public String toString()
	{
		return "book#:"+bookNumber+", volumeNumber:"+volumeNumber+", bookName:"+bookName+", hadith: "+hadithNumber+", text: "+text;
	}
}
