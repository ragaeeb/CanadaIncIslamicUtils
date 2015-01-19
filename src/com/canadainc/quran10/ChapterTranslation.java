package com.canadainc.quran10;

public class ChapterTranslation
{
	public String translation;
	public String transliteration;

	public ChapterTranslation()
	{
	}

	public ChapterTranslation(String translation, String transliteration)
	{
		super();
		this.translation = translation;
		this.transliteration = transliteration;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((translation == null) ? 0 : translation.hashCode());
		result = prime * result
				+ ((transliteration == null) ? 0 : transliteration.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ChapterTranslation other = (ChapterTranslation) obj;
		if (translation == null) {
			if (other.translation != null)
				return false;
		} else if (!translation.equals(other.translation))
			return false;
		if (transliteration == null) {
			if (other.transliteration != null)
				return false;
		} else if (!transliteration.equals(other.transliteration))
			return false;
		return true;
	}

	@Override
	public String toString()
	{
		return "translation=" + translation
				+ ", transliteration=" + transliteration + "]";
	}
}