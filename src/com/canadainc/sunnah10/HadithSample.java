package com.canadainc.sunnah10;

import java.sql.*;
import java.util.*;


public class HadithSample
{


	public static void run(Collection<Hadith> hadiths) throws ClassNotFoundException
	{
		// load the sqlite-JDBC driver using the current class loader
		Class.forName("org.sqlite.JDBC");

		Connection connection = null;
		try
		{
			// create a database connection
			connection = DriverManager.getConnection("jdbc:sqlite:sunnah.db");

			for (Hadith h: hadiths)
			{
				//sqlite> CREATE TABLE abudawud (english_id INTEGER PRIMARY KEY, arabic_id INTEGER, grade TEXT, chapter_name TEXT, chapter_number REAL, book_reference INTEGER, bookNumber INTEGER, bookName TEXT, volumeNumber INTEGER, hadithNumber INTEGER, text TEXT);
				PreparedStatement ps = connection.prepareStatement("INSERT INTO abudawud_arabic VALUES(?,?,?,?,?,?,?,?,?,?,?)");
				int i = 0;
				ps.setInt( ++i, h.id );
				ps.setInt( ++i, h.foreignId );
				ps.setString( ++i, h.grade );
				ps.setString( ++i, h.chapterName );
				ps.setDouble( ++i, h.chapterNumber );
				ps.setInt( ++i, h.bookReference );
				ps.setInt( ++i, h.bookNumber );
				ps.setString( ++i, h.bookName );
				ps.setInt( ++i, h.volumeNumber );
				ps.setString( ++i, h.hadithNumber );
				ps.setString( ++i, h.text );

				ps.execute();
				ps.close();
			}
		}
		catch(SQLException e)
		{
			// if the error message is "out of memory", 
			// it probably means no database file is found
			e.printStackTrace();
			System.err.println(e.getMessage());
		}
		finally
		{
			try
			{
				if(connection != null)
					connection.close();
			}
			catch(SQLException e)
			{
				// connection close failed.
				e.printStackTrace();
				System.err.println(e);
			}
		}
	}
}