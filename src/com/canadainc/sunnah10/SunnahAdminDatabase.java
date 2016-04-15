package com.canadainc.sunnah10;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.MessageFormat;

public class SunnahAdminDatabase
{
	private Connection m_connection;

	public SunnahAdminDatabase(String path, String arabic, String english) throws SQLException
	{
		m_connection = DriverManager.getConnection("jdbc:sqlite:"+path);
		
		PreparedStatement ps = m_connection.prepareStatement("ATTACH DATABASE '"+arabic+"' AS arabic");
		ps.execute();
		
		ps = m_connection.prepareStatement("ATTACH DATABASE '"+english+"' AS english");
		ps.execute();
		ps.close();
		
		m_connection.setAutoCommit(false);
	}


	public void process() throws SQLException
	{
		PreparedStatement ps = m_connection.prepareStatement("CREATE TABLE collections (id INTEGER PRIMARY KEY, author INTEGER, name TEXT)");
		ps.execute();
		
		ps = m_connection.prepareStatement("INSERT INTO collections SELECT id,author,name FROM english.collections");
		ps.execute();
		
		ps = m_connection.prepareStatement("CREATE TABLE narrations (id INTEGER PRIMARY KEY, collection_id INTEGER, hadith_num_ar TEXT, hadith_num_en TEXT, body TEXT)");
		ps.execute();
		
		ps = m_connection.prepareStatement("INSERT INTO narrations SELECT a.id,a.collection_id,a.hadith_number,b.hadith_number,a.body FROM english.narrations a INNER JOIN arabic.narrations b ON a.id=b.id");
		ps.execute();
		
		ps = m_connection.prepareStatement("CREATE INDEX IF NOT EXISTS narrations_index ON narrations(collection_id,hadith_num_ar,hadith_num_en)");
		ps.execute();
		ps.close();
		
		m_connection.commit();
	}
	
	
	public void close() throws SQLException {
		m_connection.close();
	}
}