package com.canadainc.quran10;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.canadainc.common.io.IOUtils;

public class ChapterTranslationPopulator
{
	private String m_surahNamesFile;
	private String m_column;
	private Connection m_connection;
	private String m_sourceDb;
	private String m_table;
	
	public ChapterTranslationPopulator(String dbFile, String surahNamesFile, String column, String sourceDb, String table) throws ClassNotFoundException, SQLException
	{
		m_surahNamesFile = surahNamesFile;
		m_column = column;
		m_sourceDb = sourceDb;
		m_table = table;

		Class.forName("org.sqlite.JDBC"); // load the sqlite-JDBC driver using the current class loader
		m_connection = DriverManager.getConnection("jdbc:sqlite:"+dbFile);
	}
	
	
	public void run() throws IOException, SQLException
	{
		String all = IOUtils.readFile( m_surahNamesFile, StandardCharsets.UTF_16 ).trim();
		String[] names = all.split("\\$");
		
		PreparedStatement ps = m_connection.prepareStatement("UPDATE chapters SET "+m_column+"=? WHERE id=?");
		
		for (int i = 0; i < names.length; i++)
		{
			String name = names[i].trim();
			name = name.replaceAll("Al-\\s+", "Al-");
			
			int x = 0;
			ps.setString(++x, name);
			ps.setInt(++x, i+1);
			ps.addBatch();
		}
		
		ps.executeBatch();
		ps.close();
		
		if (m_table != null)
		{
			System.out.println("populate "+m_surahNamesFile);
			execute("DELETE FROM verses");
			execute("VACUUM");
			execute("ATTACH DATABASE 'res/quran10/"+m_sourceDb+".db' AS "+m_sourceDb);
			execute("INSERT INTO verses (chapter_id,verse_id,translation) SELECT surah_id,verse_id,text FROM "+m_table+" ORDER BY surah_id,verse_id");
			execute("DETACH DATABASE "+m_sourceDb);
		}
	}
	
	
	public void execute(String s) throws SQLException
	{
		PreparedStatement ps = m_connection.prepareStatement(s);
		ps.execute();
		ps.close();
	}
}