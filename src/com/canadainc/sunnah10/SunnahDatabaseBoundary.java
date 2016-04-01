package com.canadainc.sunnah10;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class SunnahDatabaseBoundary
{
	private SunnahPrimaryTable<String> m_collections;
	private SunnahPrimaryTable<Chapter> m_chapters;
	private SunnahBooksTable m_books;
	private SunnahGradeTable m_grades;
	private SunnahNarrationsTable m_narrations;

	public SunnahDatabaseBoundary(String path) throws SQLException
	{
		Connection connection = DriverManager.getConnection("jdbc:sqlite:"+path);
		connection.setAutoCommit(false);

		m_collections = new SunnahCollectionsTable();
		m_collections.setConnection(connection);

		m_chapters = new SunnahChaptersTable();
		m_chapters.setConnection(connection);

		m_books = new SunnahBooksTable(m_collections);
		m_books.setConnection(connection);
		
		m_grades = new SunnahGradeTable();
		m_grades.setConnection(connection);
		
		m_narrations = new SunnahNarrationsTable(m_collections, m_chapters);
		m_narrations.setConnection(connection);
	}


	public void process(Map<String, Set<Book>> collectionToBooks, Collection<Chapter> chapters, Map<Integer, Grade> grades, Map<String, Collection<Narration>> collectionToNarrations) throws SQLException
	{
		long prev = System.currentTimeMillis();
		System.out.println("Populating database...");
		
		m_collections.process( collectionToBooks.keySet() );
		m_chapters.process(chapters);
		m_books.process(collectionToBooks);
		m_grades.process(grades);
		m_narrations.process(collectionToNarrations);
		
		long current = System.currentTimeMillis();
		System.out.println("Populated: "+(current-prev)+" ms");
		
		System.out.println("Indexing...");
		m_collections.createIndices();
		m_chapters.createIndices();
		m_books.createIndices();
		m_grades.createIndices();
		m_narrations.createIndices();
		System.out.println("Indexed: "+(System.currentTimeMillis()-current)+" ms");
	}
	
	
	public void close() throws SQLException {
		m_narrations.getConnection().close();
	}


	public void setLanguage(String language)
	{
		m_collections.setLanguage(language);
		m_books.setLanguage(language);
		m_chapters.setLanguage(language);
		m_grades.setLanguage(language);
		m_narrations.setLanguage(language);
	}
	
	
	Connection getConnection() {
		return m_narrations.getConnection();
	}
}