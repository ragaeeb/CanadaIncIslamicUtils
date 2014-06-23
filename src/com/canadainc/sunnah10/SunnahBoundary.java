package com.canadainc.sunnah10;

import java.io.File;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.jsoup.helper.StringUtil;


public class SunnahBoundary
{
	private String m_collection;
	private Collection<String> m_columns;
	private Connection m_connection;
	private DateFormat m_format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
	private String m_language;


	public SunnahBoundary(String language, String collection) throws ClassNotFoundException, SQLException
	{
		m_language = language;
		m_collection = collection;

		File dbFile = new File("res/"+getTableName()+".db");

		if ( dbFile.exists() ) {
			dbFile.delete();
		}

		Class.forName("org.sqlite.JDBC"); // load the sqlite-JDBC driver using the current class loader
		m_connection = DriverManager.getConnection("jdbc:sqlite:res/"+getTableName()+".db");

		m_columns = new ArrayList<String>();
		m_columns.add("id INTEGER PRIMARY KEY");
		m_columns.add("volumeNumber INTEGER");
		m_columns.add("bookNumber INTEGER");
		m_columns.add("bookName TEXT");
		m_columns.add("babNumber INTEGER");
		m_columns.add("babName TEXT");
		m_columns.add("hadithNumber TEXT");
		m_columns.add("hadithText TEXT");
		m_columns.add("bookID INTEGER");
		m_columns.add("grade TEXT");
		m_columns.add("ourHadithNumber INTEGER");
		m_columns.add("last_updated INTEGER");

		if ( isNonArabicLanguage() ) {
			m_columns.add("arabic_id INTEGER");
		}
	}


	public void createTable() throws SQLException
	{
		PreparedStatement ps = m_connection.prepareStatement("CREATE TABLE IF NOT EXISTS narrations ("+StringUtil.join(m_columns, ",")+")");
		ps.execute();
		ps.close();

		if ( isNonArabicLanguage() )
		{
			ps = m_connection.prepareStatement("CREATE INDEX 'fk_arabic_id' ON 'narrations' ('arabic_id' ASC);");
			ps.execute();
			ps.close();
		}
	}


	private String getTableName() {
		return m_collection+"_"+m_language;
	}


	public void populate(Collection<String> jsonContents) throws Exception
	{
		try
		{
			final String placeHolders = StringUtil.join( Collections.nCopies( m_columns.size(), "?" ), "," );
			String primaryKey = null;
			m_connection.setAutoCommit(false);
			System.out.println("populate() "+getTableName());

			for (String jsonData: jsonContents)
			{
				Object obj = JSONValue.parse(jsonData);
				JSONArray array = (JSONArray)obj;

				for (int j = 0; j < array.size(); j++)
				{
					int i = 0;

					JSONObject json = (JSONObject)array.get(j);
					PreparedStatement ps = m_connection.prepareStatement("INSERT INTO narrations VALUES("+placeHolders+")");

					if (primaryKey == null) {
						primaryKey = getPrimaryKey(json);
					}

					ps.setInt( ++i, readInt(json, primaryKey) );
					ps.setInt( ++i, readInt(json, "volumeNumber") );
					ps.setInt( ++i, readInt(json, "bookNumber") );
					ps.setString( ++i, (String)json.get("bookName") );
					ps.setInt( ++i, readInt(json, "babNumber") );
					ps.setString( ++i, (String)json.get("babName") );
					ps.setString( ++i, (String)json.get("hadithNumber") );
					ps.setString( ++i, readSanitizedString(json, "hadithText") );
					ps.setInt( ++i, readInt(json, "bookID") );
					ps.setString( ++i, (String)json.get("grade1") );
					ps.setInt( ++i, readInt(json, "ourHadithNumber") );
					ps.setLong( ++i, readDate(json, "last_updated") );

					if ( isNonArabicLanguage() ) {
						ps.setInt( ++i, readInt(json, "matchingArabicURN") );
					}

					ps.execute();
					ps.close();
				}
			}

			m_connection.commit();
		}

		catch (Exception e)
		{
			e.printStackTrace();
			try {
				m_connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}

			System.err.println( e.getMessage() );
			throw e;
		}

		finally
		{
			try {
				m_connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
				System.err.println(e);
			}
		}
	}
	
	
	private boolean isNonArabicLanguage() {
		return !m_language.equals("arabic");
	}
	
	
	private static final String getPrimaryKey(JSONObject json)
	{
		Collection<String> primaryKeys = new HashSet<String>(4);
		primaryKeys.add("arabicURN");
		primaryKeys.add("englishURN");
		primaryKeys.add("indonesianURN");
		primaryKeys.add("urduURN");

		for (String key: primaryKeys)
		{
			if ( json.containsKey(key) ) {
				return key;
			}
		}

		return null;
	}
	private static int readInt(JSONObject json, String key)
	{
		int result = 0;
		String toConvert = (String)json.get(key);

		if (toConvert == null) {
			return 0;
		}

		try {
			result = Integer.parseInt(toConvert);
		} catch (NumberFormatException ex) {
			double d = Double.parseDouble(toConvert);
			result = (int)d;
		}

		return result;
	}
	private static String readSanitizedString(JSONObject json, String key)
	{
		String toConvert = (String)json.get(key);

		if ( toConvert.endsWith("</b>") ) {
			toConvert = toConvert.substring( 0, toConvert.lastIndexOf("</b>") );
		}

		return toConvert;
	}


	private final long readDate(JSONObject json, String key) throws ParseException
	{
		String toConvert = (String)json.get(key);

		if (toConvert == null) {
			return 0;
		} else {
			return m_format.parse(toConvert).getTime();
		}
	}
}