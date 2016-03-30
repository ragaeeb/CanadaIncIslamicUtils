package com.canadainc.sunnah10;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import com.canadainc.islamicutils.io.DBUtils;

public class SunnahDatabaseBoundary
{
	private SunnahPrimaryTable<String> m_collections;
	private SunnahPrimaryTable<Chapter> m_chapters;
	private SunnahBooksTable m_books;

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
	}


	public void process(Map<String, Set<Book>> collectionToBooks, Collection<Chapter> chapters, Map<Integer, Grade> grades, Map<String, Collection<Narration>> collectionToNarrations) throws SQLException
	{
		m_collections.process( collectionToBooks.keySet() );
		m_chapters.process(chapters);
		m_books.process(collectionToBooks);

		/*
		ArrayList<String> columns = new ArrayList<String>();
		columns.add("id INTEGER PRIMARY KEY");
		columns.add("collection TEXT");
		columns.add("bookName TEXT");
		columns.add("babNumber INTEGER");
		columns.add("babName TEXT");
		columns.add("inBookNumber INTEGER");
		columns.add("hadithNumber TEXT");
		columns.add("hadithText TEXT");
		columns.add("bookID INTEGER"); */
	}


	public void setLanguage(String language)
	{
		m_collections.setLanguage(language);
		m_books.setLanguage(language);
		m_chapters.setLanguage(language);
	}
}