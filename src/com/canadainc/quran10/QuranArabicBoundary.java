package com.canadainc.quran10;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;

public class QuranArabicBoundary
{
	private Connection m_connection;
	
	public QuranArabicBoundary(String path) throws SQLException, ClassNotFoundException
	{
		Class.forName("org.sqlite.JDBC"); // load the sqlite-JDBC driver using the current class loader
		m_connection = DriverManager.getConnection("jdbc:sqlite:"+path);
	}
	
	
	public void createIndices() throws SQLException
	{
	}
	
	
	public void createTable() throws SQLException
	{
		execute("CREATE TABLE IF NOT EXISTS surahs (id INTEGER PRIMARY KEY, name TEXT, verse_count INTEGER, start INTEGER, type INTEGER, revelation_order INTEGER, rukus INTEGER)");
		execute("CREATE TABLE IF NOT EXISTS ayahs (id INTEGER PRIMARY KEY, surah_id INTEGER REFERENCES surahs(id), verse_number INTEGER, content TEXT, searchable TEXT, UNIQUE(surah_id,verse_number) ON CONFLICT REPLACE)");
		execute("CREATE TABLE IF NOT EXISTS juzs (id INTEGER PRIMARY KEY, surah_id INTEGER REFERENCES surahs(id), verse_number INTEGER, UNIQUE(surah_id,verse_number) ON CONFLICT REPLACE)");
		execute("CREATE TABLE IF NOT EXISTS hizbs (id INTEGER PRIMARY KEY, surah_id INTEGER REFERENCES surahs(id), verse_number INTEGER, UNIQUE(surah_id,verse_number) ON CONFLICT REPLACE)");
		execute("CREATE TABLE IF NOT EXISTS manzils (id INTEGER PRIMARY KEY, surah_id INTEGER REFERENCES surahs(id), verse_number INTEGER, UNIQUE(surah_id,verse_number) ON CONFLICT REPLACE)");
		execute("CREATE TABLE IF NOT EXISTS rukus (id INTEGER PRIMARY KEY, surah_id INTEGER REFERENCES surahs(id), verse_number INTEGER, UNIQUE(surah_id,verse_number) ON CONFLICT REPLACE)");
		execute("CREATE TABLE IF NOT EXISTS sajdas (surah_id INTEGER REFERENCES surahs(id), verse_number INTEGER, type INTEGER, UNIQUE(surah_id,verse_number) ON CONFLICT REPLACE)");
		execute("CREATE TABLE IF NOT EXISTS mushaf_pages (page_number INTEGER PRIMARY KEY, surah_id INTEGER REFERENCES surahs(id), verse_number INTEGER, UNIQUE(surah_id,verse_number) ON CONFLICT REPLACE)");
		execute("CREATE TABLE IF NOT EXISTS supplications (surah_id INTEGER REFERENCES surahs(id), verse_number_start INTEGER, verse_number_end INTEGER, UNIQUE(surah_id,verse_number_start) ON CONFLICT REPLACE);");
	}
	
	
	public void populateMetadata(Map<Integer,SurahMetadata> supplications) throws SQLException
	{
		PreparedStatement ps = m_connection.prepareStatement("INSERT INTO surahs (id,name,verse_count,start,type,revelation_order,rukus) VALUES (?,?,?,?,?,?,?)");
		
		for ( int chapter: supplications.keySet() )
		{
			SurahMetadata s = supplications.get(chapter);
			
			int i = 0;
			ps.setInt(++i, chapter);
			ps.setString(++i, s.name);
			ps.setInt(++i, s.verseCount);
			ps.setInt(++i, s.verseStart);
			ps.setInt(++i, s.type.ordinal()+1 ); // because we don't want a value of 0 in the database
			ps.setInt(++i, s.revelationOrder);
			ps.setInt(++i, s.rukus);
			ps.addBatch();
		}
		
		ps.executeBatch();
		ps.close();
	}
	
	
	public void populateJuzs(Map<Integer,SurahAyat> supplications) throws SQLException
	{
		PreparedStatement ps = m_connection.prepareStatement("INSERT INTO juzs (id,surah_id,verse_number) VALUES (?,?,?)");
		
		for ( int id: supplications.keySet() )
		{
			int i = 0;
			
			SurahAyat s = supplications.get(id);
			ps.setInt(++i, id);
			ps.setInt(++i, s.chapter);
			ps.setInt(++i, s.verse);
			ps.addBatch();
		}
		
		ps.executeBatch();
		ps.close();
	}
	
	
	public void populateHizbs(Map<Integer,SurahAyat> supplications) throws SQLException
	{
		PreparedStatement ps = m_connection.prepareStatement("INSERT INTO hizbs (id,surah_id,verse_number) VALUES (?,?,?)");
		
		for ( int id: supplications.keySet() )
		{
			int i = 0;
			
			SurahAyat s = supplications.get(id);
			ps.setInt(++i, id);
			ps.setInt(++i, s.chapter);
			ps.setInt(++i, s.verse);
			ps.addBatch();
		}
		
		ps.executeBatch();
		ps.close();
	}
	
	
	public void populateManzils(Map<Integer,SurahAyat> supplications) throws SQLException
	{
		PreparedStatement ps = m_connection.prepareStatement("INSERT INTO manzils (id,surah_id,verse_number) VALUES (?,?,?)");
		
		for ( int id: supplications.keySet() )
		{
			int i = 0;
			
			SurahAyat s = supplications.get(id);
			ps.setInt(++i, id);
			ps.setInt(++i, s.chapter);
			ps.setInt(++i, s.verse);
			ps.addBatch();
		}
		
		ps.executeBatch();
		ps.close();
	}
	
	
	public void populateRukus(Map<Integer,SurahAyat> supplications) throws SQLException
	{
		PreparedStatement ps = m_connection.prepareStatement("INSERT INTO rukus (id,surah_id,verse_number) VALUES (?,?,?)");
		
		for ( int id: supplications.keySet() )
		{
			int i = 0;
			
			SurahAyat s = supplications.get(id);
			ps.setInt(++i, id);
			ps.setInt(++i, s.chapter);
			ps.setInt(++i, s.verse);
			ps.addBatch();
		}
		
		ps.executeBatch();
		ps.close();
	}
	
	
	public void populateSupplications(Collection<Supplication> supplications) throws SQLException
	{
		PreparedStatement ps = m_connection.prepareStatement("INSERT INTO supplications (surah_id,verse_number_start,verse_number_end) VALUES (?,?,?)");
		
		for (Supplication s: supplications)
		{
			int i = 0;
			ps.setInt(++i, s.chapter);
			ps.setInt(++i, s.verseStart);
			ps.setInt(++i, s.verseEnd);
			ps.addBatch();
		}
		
		ps.executeBatch();
		ps.close();
	}
	
	
	public void populateMushafPages(Map<Integer,SurahAyat> pages) throws SQLException
	{
		PreparedStatement ps = m_connection.prepareStatement("INSERT INTO mushaf_pages (page_number,surah_id,verse_number) VALUES (?,?,?)");
		
		for ( int pageNumber: pages.keySet() )
		{
			int i = 0;
			
			SurahAyat s = pages.get(pageNumber);
			ps.setInt(++i, pageNumber);
			ps.setInt(++i, s.chapter);
			ps.setInt(++i, s.verse);
			ps.addBatch();
		}
		
		ps.executeBatch();
		ps.close();
	}
	
	
	public void populateSajdas(Collection<Sajda> pages) throws SQLException
	{
		PreparedStatement ps = m_connection.prepareStatement("INSERT INTO sajdas (surah_id,verse_number,type) VALUES (?,?,?)");
		
		for (Sajda s: pages)
		{
			int i = 0;
			ps.setInt( ++i, s.chapter );
			ps.setInt( ++i, s.verse );
			ps.setInt( ++i, s.type.ordinal()+1 );
			ps.addBatch();
		}
		
		ps.executeBatch();
		ps.close();
	}
	
	
	public void populateVerses(String sourceUthmani, String sourceClean) throws SQLException
	{
		execute("ATTACH DATABASE 'res/quran10/"+sourceUthmani+".db' AS '"+sourceUthmani+"'");
		execute("ATTACH DATABASE 'res/quran10/"+sourceClean+".db' AS '"+sourceClean+"'");
		execute("INSERT INTO ayahs (id,surah_id,verse_number,content,searchable) SELECT u.uindex,u.sura,u.aya,u.utext,s.stext FROM "+sourceUthmani+".quran_text u INNER JOIN "+sourceClean+".quran_text s ON s.sindex=u.uindex ORDER BY uindex ASC;");
		execute("DETACH DATABASE "+sourceUthmani);
		execute("DETACH DATABASE "+sourceClean);
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