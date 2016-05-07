package com.canadainc.sunnah10;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BulughMaramDatabase
{
	private Connection m_connection;
	private Map<Integer,Integer> m_bookIdToOffset;
	private String m_language;

	public BulughMaramDatabase(String path) throws SQLException
	{
		m_connection = DriverManager.getConnection("jdbc:sqlite:"+path);

		m_bookIdToOffset = new HashMap<Integer,Integer>();
		m_bookIdToOffset.put(2,2102000);
		m_bookIdToOffset.put(7,2110000);
		m_bookIdToOffset.put(8,2112000);
	}


	public List<Narration> process() throws SQLException
	{
		ArrayList<Narration> narrations = new ArrayList<Narration>();

		if ( m_language.equals("arabic") ) {
			populateNarrations(narrations, "ar_hadith_number", "ar_body", "ar_book_name", "ar_chapter_name");
		} else if ( m_language.equals("english") ) {
			populateNarrations(narrations, "en_hadith_number", "en_body", "en_book_name", "en_chapter_name");
		}

		return narrations;
	}


	/**
	 * @param narrations
	 * @throws SQLException
	 */
	private void populateNarrations(ArrayList<Narration> narrations, String hadithNumColumn, String bodyColumn, String bookNameColumn, String chapterNameColumn) throws SQLException
	{
		PreparedStatement ps = m_connection.prepareStatement("SELECT id,"+hadithNumColumn+","+bodyColumn+",en_book_num,en_chapter_num,"+bookNameColumn+","+chapterNameColumn+" FROM bulugh_narrations WHERE ar_body NOT NULL ORDER BY id");
		ResultSet rs = ps.executeQuery();
		boolean arabic = m_language.equals("arabic");

		while ( rs.next() )
		{
			Narration n = new Narration();
			n.book = new Book( rs.getInt("en_book_num"), rs.getString(bookNameColumn) );
			n.id = m_bookIdToOffset.get(n.book.id) + rs.getInt("id");
			n.chapter = new Chapter( rs.getString(chapterNameColumn), rs.getInt("en_chapter_num") );
			n.hadithNumber = rs.getString(hadithNumColumn);
			n.text = rs.getString(bodyColumn);
			
			Narration prev = narrations.isEmpty() ? new Narration() : narrations.get( narrations.size()-1 );

			if ( n.book.equals(prev.book) ) {
				n.inBookNumber = prev.inBookNumber+1;
			} else { // restart
				n.inBookNumber = 1;
			}

			if (!arabic) {
				n.arabicId = n.id;
			}

			narrations.add(n);
		}
	}


	public void setLanguage(String language) {
		m_language = language;
	}


	public void close() throws SQLException {
		m_connection.close();
	}
}