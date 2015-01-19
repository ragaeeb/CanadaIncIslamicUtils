package com.canadainc.sunnah10;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.text.WordUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.jsoup.helper.StringUtil;


public class SunnahBoundary
{
	private static Map<Integer, String> adabArabicChapterNames = new HashMap<Integer, String>();
	private static Pattern chapterPattern = Pattern.compile("^Chapter \\d+\\.*\\s*\\w", Pattern.CASE_INSENSITIVE);
	private String m_collection;
	private ArrayList<String> m_columns;
	private Connection m_connection;
	private Connection m_gradeConnection;
	private DateFormat m_format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
	private String m_language;
	static {
		adabArabicChapterNames.put(1, "كتاب الْوَالِدَيْنِ");
		adabArabicChapterNames.put(2, "كتاب صِلَةِ الرَّحِمِ");
		adabArabicChapterNames.put(3, "كتاب مَوَالِي");
		adabArabicChapterNames.put(6, "كتاب الْجَارِ");
		adabArabicChapterNames.put(7, "كتاب الْكَرَمِ وَ يَتِيمٌ");
		adabArabicChapterNames.put(11, "كتاب الْمَعْرُوفِ");
		adabArabicChapterNames.put(12, "كتاب الِانْبِسَاطِ إِلَى النَّاسِ");
		adabArabicChapterNames.put(13, "كتاب الْمَشُورَةِ");
		adabArabicChapterNames.put(17, "كتاب الزِّيَارَةِ");
		adabArabicChapterNames.put(18, "كتاب الأكَابِرِ");
		adabArabicChapterNames.put(19, "كتاب الصَّغِيرِ");
		adabArabicChapterNames.put(20, "كتاب رَحْمَةِ");
		adabArabicChapterNames.put(24, "كتاب السِّبَابِ");
		adabArabicChapterNames.put(25, "كتاب السَّرَفِ فِي الْبِنَاءِ");
		adabArabicChapterNames.put(26, "كتاب الرِّفْقِ");
		adabArabicChapterNames.put(28, "كتاب الظُّلْم");
		adabArabicChapterNames.put(33, "كتاب الأقوال");
		adabArabicChapterNames.put(34, "كتاب الأسْمَاءِ");
		adabArabicChapterNames.put(35, "كتاب الكُنْيَةِ");
		adabArabicChapterNames.put(36, "كتاب الشِّعْرِ");
		adabArabicChapterNames.put(37, "كتاب الْكَلامِ");
		adabArabicChapterNames.put(40, "كتاب الْعُطَاسَ والتثاؤب");
		adabArabicChapterNames.put(42, "كتاب السَّلامِ");
		adabArabicChapterNames.put(43, "كتاب الاسْتِئْذَانُ");
		adabArabicChapterNames.put(44, "كتاب أَهْلِ الْكِتَابِ");
		adabArabicChapterNames.put(45, "كتاب الرَّسَائِلِ‏");
		adabArabicChapterNames.put(46, "كتاب الْمَجَالِسِ");
		adabArabicChapterNames.put(49, "كتاب الصباح والمساء");
		adabArabicChapterNames.put(51, "كتاب الْبَهَائِمِ");
		adabArabicChapterNames.put(53, "كتاب الْخِتَانِ");
	}


	public SunnahBoundary(String language, String collection) throws SQLException
	{
		m_language = language;
		m_collection = collection;

		m_connection = DriverManager.getConnection("jdbc:sqlite:res/sunnah10/"+getTableName()+".db");
		m_gradeConnection = DriverManager.getConnection("jdbc:sqlite:res/sunnah10/original_grades_"+language+".db");

		m_columns = new ArrayList<String>();
		m_columns.add("id INTEGER PRIMARY KEY");
		m_columns.add("collection TEXT");
		m_columns.add("bookName TEXT");
		m_columns.add("babNumber INTEGER");
		m_columns.add("babName TEXT");
		m_columns.add("inBookNumber INTEGER");
		m_columns.add("hadithNumber TEXT");
		m_columns.add("hadithText TEXT");
		m_columns.add("bookID INTEGER");

		if ( isNonArabicLanguage() ) {
			m_columns.add("arabic_id INTEGER");
		}
	}
	
	
	public void createIndices() throws SQLException
	{
		System.out.println("Creating indices...");
		
		PreparedStatement ps;
		
		if ( isNonArabicLanguage() )
		{
			ps = m_connection.prepareStatement("CREATE INDEX IF NOT EXISTS 'fk_arabic_id' ON 'narrations' ('arabic_id' ASC);");
			ps.execute();
			ps.close();
		}
		
		ps = m_connection.prepareStatement("CREATE INDEX IF NOT EXISTS 'fk_collection' ON 'narrations' ('collection' ASC);");
		ps.execute();
		ps.close();
		
		ps = m_connection.prepareStatement("CREATE INDEX IF NOT EXISTS 'fk_book_id' ON 'narrations' ('bookID' ASC);");
		ps.execute();
		ps.close();
		
		ps = m_connection.prepareStatement("CREATE INDEX IF NOT EXISTS 'fk_in_book_number' ON 'narrations' ('inBookNumber' ASC);");
		ps.execute();
		ps.close();
		
		ps = m_gradeConnection.prepareStatement("CREATE INDEX IF NOT EXISTS 'fk_arabic_id' ON 'grades' ('arabic_id' ASC);");
		ps.execute();
		ps.close();
		
		ps = m_gradeConnection.prepareStatement("CREATE INDEX IF NOT EXISTS 'fk_muhaddith_id' ON 'grades' ('m_id' ASC);");
		ps.execute();
		ps.close();
	}
	
	
	public void removeBookName() throws SQLException
	{
		m_columns.remove("bookName TEXT");
		
		PreparedStatement ps = m_connection.prepareStatement("ALTER TABLE narrations RENAME TO narrations_temp");
		ps.execute();
		ps.close();
		
		createTable();
		
		for (int i = 0; i < m_columns.size(); i++) {
			String column = m_columns.get(i);
			column = column.split(" ")[0];
			m_columns.set(i, column);
		}
		
		System.out.println( "INSERT INTO narrations SELECT "+StringUtil.join(m_columns, ",")+" from narrations_temp" );
		/*
		ps = m_connection.prepareStatement("INSERT INTO narrations SELECT ("+StringUtil.join(m_columns, ",")+") from narrations_temp");
		ps.execute();
		ps.close();
		
		ps = m_connection.prepareStatement("DROP TABLE narrations_temp");
		ps.execute();
		ps.close(); */
	}
	
	
	public void vacuum() throws SQLException
	{
		m_connection = DriverManager.getConnection("jdbc:sqlite:res/sunnah10/"+getTableName()+".db");
		m_gradeConnection = DriverManager.getConnection("jdbc:sqlite:res/sunnah10/original_grades_"+m_language+".db");
		
		createIndices();
		
		PreparedStatement ps = m_connection.prepareStatement("VACUUM");
		ps.execute();
		ps.close();
		
		ps = m_gradeConnection.prepareStatement("VACUUM");
		ps.execute();
		ps.close();
	}
	
	
	public void createTable() throws SQLException
	{
		PreparedStatement ps = m_connection.prepareStatement("CREATE TABLE IF NOT EXISTS narrations ("+StringUtil.join(m_columns, ",")+")");
		ps.execute();
		ps.close();

		ps = m_connection.prepareStatement("CREATE TABLE IF NOT EXISTS chapters (collection TEXT NOT NULL, bookID INTEGER NOT NULL, name TEXT NOT NULL, UNIQUE(collection,bookID) ON CONFLICT REPLACE)");
		ps.execute();
		ps.close();
		
		ps = m_gradeConnection.prepareStatement("CREATE TABLE IF NOT EXISTS grades (id INTEGER PRIMARY KEY, arabic_id INTEGER NOT NULL, m_id INTEGER NOT NULL, grade TEXT NOT NULL, reference TEXT);");
		ps.execute();
		ps.close();
	}
	
	
	public void populate(Collection<String> jsonContents) throws Exception
	{
		try
		{
			final String placeHolders = StringUtil.join( Collections.nCopies( m_columns.size(), "?" ), "," );
			String primaryKey = null;
			m_connection.setAutoCommit(false);

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
					
					int bookID = readInt(json, "bookID");
					int id = readInt(json, primaryKey);

					ps.setInt( ++i, id );
					ps.setString( ++i, m_collection);
					//ps.setInt( ++i, readInt(json, "volumeNumber") );
					//ps.setInt( ++i, readInt(json, "bookNumber") );
					
					String bookName = getBookName(json, bookID);
					ps.setString( ++i, bookName);
					
					int babNumber = readInt(json, "babNumber");
					
					int inBookNumber = readInt(json, "ourHadithNumber");
					
					String babName = (String)json.get("babName");
					
					if (babName != null) {
						babName = WordUtils.capitalizeFully(babName).trim();
					}
					
					if ( babNumber == 0 && babName != null && !babName.isEmpty() )
					{
						Matcher matcher = chapterPattern.matcher(babName);

						if ( matcher.find() )
						{
							String match = babName.substring( matcher.start(), matcher.end() ).trim();
							babNumber = Integer.parseInt( match.replaceAll("\\D+","") );
							
							for (int k = matcher.end()-1; k < babName.length(); k++)
							{
								char current = babName.charAt(k);
								
								if ( Character.isAlphabetic(current) ) {
									babName = babName.substring(k);
									break;
								}
							}
						}
					}
					
					ps.setInt( ++i, babNumber );
					ps.setString( ++i, babName );
					ps.setInt( ++i, inBookNumber );
					ps.setString( ++i, getHadithNumber(json) );
					ps.setString( ++i, readSanitizedString(json, "hadithText").trim() );
					ps.setInt( ++i, bookID );
					//ps.setLong( ++i, readDate(json, "last_updated") );
					
					int arabicURN = 0;
					
					if ( isNonArabicLanguage() ) {
						arabicURN = readInt(json, "matchingArabicURN");
						ps.setInt( ++i, arabicURN );
					}

					ps.execute();
					ps.close();
					
					fillGrade( isNonArabicLanguage() ? arabicURN : id, (String)json.get("grade1"), m_collection.equals("nasai") || m_collection.equals("tirmidhi") ? 2 : 1 );
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


	private void fillGrade(int arabicID, String grade, int muhaddith) throws SQLException
	{
		if ( grade != null && !grade.isEmpty() )
		{
			int i = 0;
			
			PreparedStatement ps = m_gradeConnection.prepareStatement("INSERT INTO grades (arabic_id, m_id, grade) VALUES(?,?,?)");
			ps.setInt( ++i, arabicID );
			ps.setInt( ++i, muhaddith);
			ps.setString( ++i, grade.replaceAll("<[^>]*>", "") );
			ps.execute();
			ps.close();
		}
	}


	private String getBookName(JSONObject json, int bookID)
	{
		String bookName = (String)json.get("bookName");
		
		if ( ( bookName == null || bookName.isEmpty() ) && m_collection.equals("nasai") ) {
			if (bookID == 47) {
				bookName = "كتاب الإيمان وشرائعه";
			} else if (bookID == 4) {
				bookName = "كتاب الغسل والتيمم";
			}
		} else if ( ( bookName == null || bookName.isEmpty() ) && m_collection.equals("nawawi40") ) {
			bookName = isNonArabicLanguage() ? "40 Hadith Nawawi" : "الأربعون النووية";
		} else if ( ( bookName == null || bookName.isEmpty() ) && m_collection.equals("qudsi40") ) {
			bookName = isNonArabicLanguage() ? "40 Hadith Qudsi" : "الحديث القدسي";
		} else {
			bookName = WordUtils.capitalizeFully( (String)json.get("bookName") );
			bookName = bookName.replace("\n", " ").trim();
			
			if ( bookName.equalsIgnoreCase("ok") && m_language.equals("arabic") && m_collection.equals("adab") )
			{
				if ( adabArabicChapterNames.containsKey(bookID) ) {
					bookName = adabArabicChapterNames.get(bookID);
				} else {
					bookName = "كتاب";
				}
			}
			
			if (bookName == null || bookName.isEmpty()) {
				System.out.println("WARNING!");
				System.out.println(bookName);
				System.out.println(bookID);
				System.out.println(m_collection);
			}
		}
		
		return bookName;
	}
	private String getTableName() {
		return /*m_collection*/"sunnah"+"_"+m_language;
	}
	private boolean isNonArabicLanguage() {
		return !m_language.equals("arabic");
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
	
	private static final String getPrimaryKey(JSONObject json)
	{
		Collection<String> primaryKeys = new HashSet<String>(4);
		primaryKeys.add("arabicURN");
		primaryKeys.add("englishURN");
		//primaryKeys.add("indonesianURN");
		//primaryKeys.add("urduURN");

		for (String key: primaryKeys)
		{
			if ( json.containsKey(key) ) {
				return key;
			}
		}

		return null;
	}
	
	
	private static String getHadithNumber(JSONObject json)
	{
		String result = ( (String)json.get("hadithNumber") ).trim();
		
		if ( result.startsWith("Introduction ") ) {
			result = result.substring( "Introduction ".length() );
		}
		
		return result;
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


	private String readSanitizedString(JSONObject json, String key)
	{
		String toConvert = (String)json.get(key);

		if ( toConvert.endsWith("</b>") ) {
			toConvert = toConvert.substring( 0, toConvert.lastIndexOf("</b>") );
		}
		
		toConvert = toConvert.replace("()", "ﷺ");
		
		if ( m_language.equals("english") )
		{
			// [\[\(]SAW[\]\)]
			
			toConvert = toConvert.replace("(S)", "ﷺ");
			toConvert = toConvert.replace("(s.a.w)", "ﷺ");
			toConvert = toConvert.replace("(S.A.W)", "ﷺ");
			toConvert = toConvert.replace("[SAW]", "ﷺ");
			toConvert = toConvert.replace("(SAW)", "ﷺ");
			toConvert = toConvert.replace("SAW0", "ﷺ");
			toConvert = toConvert.replace("SWAS", "ﷺ");
			toConvert = toConvert.replace("(saW)", "ﷺ");
			toConvert = toConvert.replace("(saas)", "ﷺ");
			toConvert = toConvert.replace("(SWAS)", "ﷺ");
			toConvert = toConvert.replace("p.b.u.h", "ﷺ");
			toConvert = toConvert.replace("pbuh", "ﷺ");
			toConvert = toConvert.replace("P.B.U.H.", "ﷺ");
			toConvert = toConvert.replaceAll("\\s+", " ");
		}
		
		toConvert = toConvert.replaceAll("<[^>]*>", "");

		return toConvert;
	}
}