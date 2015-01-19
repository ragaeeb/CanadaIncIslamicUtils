package com.canadainc.quran10;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

public class QuranTranslationBoundary
{
	private Connection m_connection;
	

	public QuranTranslationBoundary(String path) throws SQLException, ClassNotFoundException
	{
		Class.forName("org.sqlite.JDBC"); // load the sqlite-JDBC driver using the current class loader
		m_connection = DriverManager.getConnection("jdbc:sqlite:"+path);
	}


	public void createIndices() throws SQLException
	{
	}


	public void createTable() throws SQLException
	{
		execute("CREATE TABLE IF NOT EXISTS chapters (id INTEGER PRIMARY KEY, transliteration TEXT, translation TEXT)");
		execute("CREATE TABLE IF NOT EXISTS verses (id INTEGER PRIMARY KEY, translation TEXT)");
	}


	public void populateChapters(Map<Integer,ChapterTranslation> supplications) throws SQLException
	{
		PreparedStatement ps = m_connection.prepareStatement("INSERT INTO chapters (id,transliteration,translation) VALUES (?,?,?)");

		for ( int chapter: supplications.keySet() )
		{
			ChapterTranslation s = supplications.get(chapter);

			int i = 0;
			ps.setInt(++i, chapter);
			ps.setString(++i, s.transliteration);
			ps.setString(++i, s.translation);
			ps.addBatch();
		}

		ps.executeBatch();
		ps.close();
	}


	public void populateVerses(String translationSource, String tableName) throws SQLException
	{
		execute("ATTACH DATABASE 'res/quran10/"+translationSource+".db' AS '"+translationSource+"'");
		execute("INSERT INTO verses (id,translation) SELECT rowid,text FROM "+tableName);
		execute("DETACH DATABASE "+translationSource);
	}


	public void execute(String s) throws SQLException
	{
		PreparedStatement ps = m_connection.prepareStatement(s);
		ps.execute();
		ps.close();
	}


	Connection getConnection()
	{
		return m_connection;
	}
}