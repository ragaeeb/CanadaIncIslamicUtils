package com.canadainc.sunnah10;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import com.canadainc.islamicutils.io.DBUtils;

public class SunnahBooksTable implements SunnahTable
{
	private Connection m_connection;
	private SunnahPrimaryTable<String> m_collections;
	private boolean m_arabic;

	public SunnahBooksTable(SunnahPrimaryTable<String> collections)
	{
		super();
		m_collections = collections;
	}


	public void process(Map<String, Set<Book>> collectionToBooks) throws SQLException
	{
		ArrayList<String> columns = new ArrayList<String>();
		columns.add("collection_id INTEGER NOT NULL");
		columns.add("book_id INTEGER NOT NULL");
		columns.add("title TEXT NOT NULL");

		if (!m_arabic) {
			columns.add("transliteration TEXT");
		}

		DBUtils.createTable(m_connection, getTableName(), columns);

		PreparedStatement ps = m_connection.prepareStatement("INSERT INTO "+getTableName()+" VALUES "+DBUtils.generatePlaceHolders(columns));

		for ( String collection: collectionToBooks.keySet() )
		{
			Collection<Book> books = collectionToBooks.get(collection);

			for (Book b: books)
			{
				int i = 0;
				ps.setInt(++i, m_collections.getIdFor(collection));
				ps.setInt(++i, b.id);
				ps.setString(++i, b.name);

				if (!m_arabic) {
					ps.setString(++i, b.transliteration);
				}

				ps.execute();
			}
		}

		m_connection.commit();
		ps.close();
	}

	@Override
	public String getTableName() {
		return "books";
	}

	@Override
	public void setLanguage(String language) {
		m_arabic = language.equals("arabic");
	}


	@Override
	public void setConnection(Connection c) {
		m_connection = c;
	}
}