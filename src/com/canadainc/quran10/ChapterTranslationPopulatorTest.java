package com.canadainc.quran10;

import static org.junit.Assert.fail;

import org.junit.Test;

public class ChapterTranslationPopulatorTest
{
	@Test
	public void testRun()
	{
		try {
			ChapterTranslationPopulator ctp = new ChapterTranslationPopulator("res/quran10/quran_spanish.db", "res/quran10/other_languages/SoarNameEsp.txt", "translation", "translation_source", "spanish");
			ctp.run();
			
			ctp = new ChapterTranslationPopulator("res/quran10/quran_french.db", "res/quran10/other_languages/SoarNamesFr.txt", "transliteration", "translation_source", "french");
			ctp.run();
			
			ctp = new ChapterTranslationPopulator("res/quran10/quran_hausa.db", "res/quran10/other_languages/SoarNamesHausa.txt", "transliteration", "translation_source", null);
			ctp.run();
			
			ctp = new ChapterTranslationPopulator("res/quran10/quran_indo.db", "res/quran10/other_languages/SoarNamesIndo.txt", "transliteration", "translation_source", "indo");
			ctp.run();
			
			ctp = new ChapterTranslationPopulator("res/quran10/quran_urdu.db", "res/quran10/other_languages/SoarNamesUrdo.txt", "translation", "translation_source", "urdu");
			ctp.run();
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
}