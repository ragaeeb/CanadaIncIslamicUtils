package com.canadainc.sunnah10;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.canadainc.common.io.IOUtils;

public class SunnahIO
{
	public static void printNarrators(NarratorCollector rc)
	{
		List<String> rijaal = new ArrayList<String>();
		rijaal.addAll( rc.getCollected() );
		Collections.sort(rijaal);

		StringBuffer sb = new StringBuffer();

		for (String s: rijaal) {
			sb.append(s+"\n");
		}

		IOUtils.writeFile("res/sunnah10/output.txt", sb.toString().trim());
	}

	public static void printBooks(BookCollector rc)
	{
		Map<String, Set<Book>> collectionToBooks = rc.getCollected();
		StringBuffer sb = new StringBuffer();

		for ( String key: collectionToBooks.keySet() )
		{
			sb.append(key+"\n");

			List<Book> books = new ArrayList<Book>();
			books.addAll( collectionToBooks.get(key) );
			Collections.sort(books);

			for (Book b: books) {
				sb.append( b.toString()+"\n" );
			}
		}

		IOUtils.writeFile("res/sunnah10/output.txt", sb.toString().trim());
	}
	
	
	public static void printGrades(GradeCollector ac)
	{
		Map<Integer, Grade> narrationToGrade = ac.getCollected();
		StringBuffer sb = new StringBuffer();

		for ( Integer key: narrationToGrade.keySet() ) {
			sb.append( narrationToGrade.get(key).toString()+"\n");
		}

		IOUtils.writeFile("res/sunnah10/output.txt", sb.toString().trim());
	}


	public static void printBody(Collection<Narration> narrations)
	{
		StringBuffer sb = new StringBuffer();

		for (Narration n: narrations) {
			sb.append(n.chapter.number+"; "+n.text+"\n");
		}

		IOUtils.writeFile("res/sunnah10/output.txt", sb.toString());
	}
	
	
	public static void printChapters(ChapterCollector ac)
	{
		Collection<Chapter> chapters = ac.getCollected();
		StringBuffer sb = new StringBuffer();

		for (Chapter c: chapters) {
			sb.append( c.toString()+"\n");
		}

		IOUtils.writeFile("res/sunnah10/output.txt", sb.toString().trim());
	}
}