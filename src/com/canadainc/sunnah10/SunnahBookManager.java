package com.canadainc.sunnah10;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SunnahBookManager
{
	private Connection m_connection;
	
	public SunnahBookManager() throws SQLException
	{
		m_connection = DriverManager.getConnection("jdbc:sqlite:res/original_grades_arabic.db");
	}
	
	
	public void attachDatabases() throws SQLException
	{
		System.out.println("attaching databases...");
		PreparedStatement ps = m_connection.prepareStatement("ATTACH DATABASE 'res/sunnah_arabic.db' AS 'sunnah_arabic'");
		ps.execute();
		ps.close();
		
		ps = m_connection.prepareStatement("ATTACH DATABASE 'res/sunnah_english.db' AS 'sunnah_english'");
		ps.execute();
		ps.close();
		System.out.println("attached...");
	}
	
	
	public void populateChapters() throws SQLException
	{
		System.out.println("populate chapters 1/2...");
		PreparedStatement ps = m_connection.prepareStatement("INSERT INTO sunnah_english.chapters (collection,bookID,name) SELECT a.collection,a.bookID,e.bookName as translated_book_name FROM sunnah_arabic.narrations a INNER JOIN sunnah_english.narrations e ON a.id=e.arabic_id GROUP BY a.collection,a.bookID ORDER BY a.collection,a.bookID ASC");
		ps.execute();
		ps.close();
		
		System.out.println("populate chapters 2/2...");
		ps = m_connection.prepareStatement("INSERT INTO sunnah_arabic.chapters (collection,bookID,name) SELECT a.collection,a.bookID,a.bookName FROM sunnah_arabic.narrations a INNER JOIN sunnah_english.narrations e ON a.id=e.arabic_id GROUP BY a.collection,a.bookID ORDER BY a.collection,a.bookID ASC");
		ps.execute();
		ps.close();
	}
}