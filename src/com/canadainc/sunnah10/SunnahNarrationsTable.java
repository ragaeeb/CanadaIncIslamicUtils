package com.canadainc.sunnah10;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import com.canadainc.islamicutils.io.DBUtils;

public class SunnahNarrationsTable implements SunnahTable
{
	private Connection m_connection;
	private SunnahPrimaryTable<Chapter> m_chapters;
	private SunnahPrimaryTable<String> m_collections;
	private boolean m_arabic;

	public SunnahNarrationsTable(SunnahPrimaryTable<String> collections, SunnahPrimaryTable<Chapter> chapters)
	{
		super();
		m_chapters = chapters;
		m_collections = collections;
	}


	public void process(Map<String, Collection<Narration>> collectionToNarrations) throws SQLException
	{
		ArrayList<String> columns = new ArrayList<String>();
		columns.add("id INTEGER PRIMARY KEY");
		columns.add("collection_id INTEGER NOT NULL");
		columns.add("book_id INTEGER NOT NULL");
		columns.add("chapter_id INTEGER");
		columns.add("in_book_number INTEGER");
		columns.add("hadith_number TEXT");
		columns.add("body TEXT");
		columns.add("translator_id INTEGER");

		DBUtils.createTable(m_connection, getTableName(), columns);

		PreparedStatement ps = m_connection.prepareStatement("INSERT INTO "+getTableName()+" VALUES "+DBUtils.generatePlaceHolders(columns));

		for ( String collection: collectionToNarrations.keySet() )
		{
			Collection<Narration> narrations = collectionToNarrations.get(collection);

			for (Narration n: narrations)
			{
				if ( !m_arabic && n.arabicId == 0 ) {
					// skip
				} else {
					int i = 0;
					ps.setInt(++i, m_arabic ? n.id : n.arabicId); // use the arabic id so we can link at runtime
					ps.setInt(++i, m_collections.getIdFor(collection));
					DBUtils.setNullInt(++i, n.book.id, ps);
					DBUtils.setNullInt(++i, m_chapters.getIdFor(n.chapter), ps);
					DBUtils.setNullInt(++i, n.inBookNumber, ps);
					ps.setString(++i, n.hadithNumber);
					ps.setString(++i, n.text);
					DBUtils.setNullInt(++i, n.translator, ps);
					ps.execute();
				}
			}
		}

		m_connection.commit();
		ps.close();
	}

	@Override
	public String getTableName() {
		return "narrations";
	}

	@Override
	public void setLanguage(String language) {
		m_arabic = language.equals("arabic");
	}


	@Override
	public void setConnection(Connection c) {
		m_connection = c;
	}
	
	Connection getConnection() {
		return m_connection;
	}


	@Override
	public void createIndices() throws SQLException
	{
		PreparedStatement ps = m_connection.prepareStatement("CREATE INDEX IF NOT EXISTS narrations_index ON narrations(collection_id,book_id,chapter_id,hadith_number)");
		ps.execute();
		ps.close();
	}
}