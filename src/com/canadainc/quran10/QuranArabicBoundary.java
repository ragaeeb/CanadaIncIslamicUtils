package com.canadainc.quran10;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.canadainc.common.io.IOUtils;

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
		execute("CREATE INDEX IF NOT EXISTS ayah_surah_index ON ayahs(surah_id,verse_number)");
		execute("CREATE INDEX IF NOT EXISTS juz_index ON juzs(surah_id,verse_number)");
		execute("CREATE INDEX IF NOT EXISTS mushaf_index ON mushaf_pages(surah_id,verse_number)");
		execute("CREATE INDEX IF NOT EXISTS qarees_index ON qarees(level)");
		execute("CREATE INDEX IF NOT EXISTS recitations_index ON recitations(qaree_id)");
		execute("CREATE INDEX IF NOT EXISTS related_index ON related(surah_id,from_verse_id,to_verse_id,other_surah_id,other_from_verse_id,other_to_verse_id)");
	}
	
	
	public void createTable() throws SQLException
	{
		execute("CREATE TABLE IF NOT EXISTS surahs (id INTEGER PRIMARY KEY, name TEXT, verse_count INTEGER, start INTEGER, type INTEGER, revelation_order INTEGER, rukus INTEGER)");
		execute("CREATE TABLE IF NOT EXISTS ayahs (surah_id INTEGER REFERENCES surahs(id), verse_number INTEGER, content TEXT, searchable TEXT, UNIQUE(surah_id,verse_number) ON CONFLICT REPLACE)");
		execute("CREATE TABLE IF NOT EXISTS juzs (id INTEGER PRIMARY KEY, surah_id INTEGER REFERENCES surahs(id), verse_number INTEGER, UNIQUE(surah_id,verse_number) ON CONFLICT REPLACE)");
		execute("CREATE TABLE IF NOT EXISTS hizbs (id INTEGER PRIMARY KEY, surah_id INTEGER REFERENCES surahs(id), verse_number INTEGER, UNIQUE(surah_id,verse_number) ON CONFLICT REPLACE)");
		execute("CREATE TABLE IF NOT EXISTS manzils (id INTEGER PRIMARY KEY, surah_id INTEGER REFERENCES surahs(id), verse_number INTEGER, UNIQUE(surah_id,verse_number) ON CONFLICT REPLACE)");
		execute("CREATE TABLE IF NOT EXISTS rukus (id INTEGER PRIMARY KEY, surah_id INTEGER REFERENCES surahs(id), verse_number INTEGER, UNIQUE(surah_id,verse_number) ON CONFLICT REPLACE)");
		execute("CREATE TABLE IF NOT EXISTS sajdas (surah_id INTEGER REFERENCES surahs(id), verse_number INTEGER, type INTEGER, UNIQUE(surah_id,verse_number) ON CONFLICT REPLACE)");
		execute("CREATE TABLE IF NOT EXISTS mushaf_pages (page_number INTEGER PRIMARY KEY, surah_id INTEGER REFERENCES surahs(id), verse_number INTEGER, UNIQUE(surah_id,verse_number) ON CONFLICT REPLACE)");
		execute("CREATE TABLE IF NOT EXISTS supplications (surah_id INTEGER REFERENCES surahs(id), verse_number_start INTEGER, verse_number_end INTEGER, UNIQUE(surah_id,verse_number_start) ON CONFLICT REPLACE);");
		execute("CREATE TABLE IF NOT EXISTS qarees (id INTEGER PRIMARY KEY, name TEXT NOT NULL, bio TEXT, level INTEGER DEFAULT 1)");
		execute("CREATE TABLE IF NOT EXISTS recitations (qaree_id INTEGER REFERENCES qarees(id) ON DELETE CASCADE, description TEXT, value TEXT NOT NULL)");
		//execute("CREATE TABLE IF NOT EXISTS images (surah_id INTEGER REFERENCES surahs(id), verse_number INTEGER, content BLOB, UNIQUE(surah_id,verse_number) ON CONFLICT REPLACE)");
		execute("CREATE TABLE related (surah_id INTEGER NOT NULL REFERENCES surahs(id), from_verse_id INTEGER NOT NULL, to_verse_id INTEGER NOT NULL, other_surah_id INTEGER NOT NULL REFERENCES surahs(id), other_from_verse_id INTEGER NOT NULL, other_to_verse_id INTEGER NOT NULL, UNIQUE(surah_id,from_verse_id,to_verse_id,other_surah_id,other_from_verse_id,other_to_verse_id) ON CONFLICT IGNORE)");
	}
	
	
	public void execute(String s) throws SQLException
	{
		PreparedStatement ps = m_connection.prepareStatement(s);
		ps.execute();
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
	
	
	public void populateImages(String sourceFolder) throws SQLException, FileNotFoundException
	{
		File folder = new File(sourceFolder);
		File[] listOfFiles = folder.listFiles( new FilenameFilter()
		{
			@Override
			public boolean accept(File dir, String name) {
				return name.matches("[1-9]{1,3}_[1-9]{1,3}\\.png");
			}
		} );

		if (listOfFiles != null)
		{
			PreparedStatement ps = m_connection.prepareStatement("INSERT INTO images (surah_id,verse_number,content) VALUES (?,?,?)");
			
			for (File f: listOfFiles)
			{
				String name = f.getName();
				int underscoreIndex = name.indexOf("_");
				
				int chapter = Integer.parseInt( name.substring(0, underscoreIndex) );
				int verse = Integer.parseInt( name.substring( underscoreIndex+1, name.lastIndexOf(".") ) );
				
				int i = 0;
				ps.setInt(++i, chapter);
				ps.setInt(++i, verse);
				ps.setBytes( ++i, IOUtils.getByteArrayFromFile(f) );
				ps.addBatch();
			}
			
			ps.executeBatch();
			ps.close();
		}
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
	
	
	public void populateRecitations(String qareesCsv, String recitationsCsv) throws SQLException, IOException
	{
		String content = IOUtils.readFileUtf8( new File(qareesCsv) );
		String[] data = content.split("\n");
		
		PreparedStatement ps = m_connection.prepareStatement("INSERT INTO qarees (id,name,level,bio) VALUES (?,?,?,?)");
		
		for (String s: data)
		{
			int i = 0;
			
			String[] tokens = s.split("\",\"");
			
			ps.setInt( ++i, Integer.parseInt( tokens[0].substring(1) ) );
			ps.setString( ++i, tokens[1] );
			ps.setInt( ++i, Integer.parseInt(tokens[2]) );
			ps.setString( ++i, tokens[3].substring( 0, tokens[3].length()-1 ) );
			ps.addBatch();
		}
		
		ps.executeBatch();
		ps.close();
		
		content = IOUtils.readFileUtf8( new File(recitationsCsv) );
		data = content.split("\n");
		
		ps = m_connection.prepareStatement("INSERT INTO recitations (qaree_id,description,value) VALUES (?,?,?)");
		
		for (String s: data)
		{
			int i = 0;
			
			String[] tokens = s.split("\",\"");
			
			ps.setInt( ++i, Integer.parseInt( tokens[0].substring(1) ) );
			ps.setString( ++i, tokens[1] );
			ps.setString( ++i, tokens[2].substring( 0, tokens[2].length()-1 ) );
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
	
	
	public void populateSimilar(Map< Supplication, List<Supplication> > similar) throws SQLException
	{
		PreparedStatement ps = m_connection.prepareStatement("INSERT INTO related (surah_id,from_verse_id,to_verse_id,other_surah_id,other_from_verse_id,other_to_verse_id) VALUES (?,?,?,?,?,?)");
		
		for ( Supplication s: similar.keySet() )
		{
			List<Supplication> values = similar.get(s);
			
			for (Supplication x: values)
			{
				int i = 0;
				ps.setInt( ++i, s.chapter );
				ps.setInt( ++i, s.verseStart );
				ps.setInt( ++i, s.verseEnd );
				ps.setInt( ++i, x.chapter );
				ps.setInt( ++i, x.verseStart );
				ps.setInt( ++i, x.verseEnd );
				ps.addBatch();
			}
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
	
	
	public void populateVerses(String sourceUthmani, String sourceClean) throws SQLException
	{
		execute("ATTACH DATABASE 'res/quran10/"+sourceUthmani+".db' AS '"+sourceUthmani+"'");
		execute("ATTACH DATABASE 'res/quran10/"+sourceClean+".db' AS '"+sourceClean+"'");
		execute("INSERT INTO ayahs (surah_id,verse_number,content,searchable) SELECT u.sura,u.aya,u.utext,s.stext FROM "+sourceUthmani+".quran_text u INNER JOIN "+sourceClean+".quran_text s ON s.sindex=u.uindex ORDER BY uindex ASC;");
		execute("DETACH DATABASE "+sourceUthmani);
		execute("DETACH DATABASE "+sourceClean);
	}


	Connection getConnection()
	{
		return m_connection;
	}
}