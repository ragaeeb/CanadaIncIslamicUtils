package com.canadainc.sunnah10;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.canadainc.islamicutils.io.DBUtils;

public class SunnahCollectionsTable implements SunnahPrimaryTable<String>
{
	private Connection m_connection;
	private Map<String, SunnahCollection> m_codeToCollection;

	public SunnahCollectionsTable() {
		m_codeToCollection = new HashMap<String, SunnahCollection>();
	}


	/**
	 * @param collectionToBooks
	 * @throws SQLException
	 */
	@Override
	public void process(Collection<String> collectionKeys) throws SQLException
	{
		ArrayList<String> columns = new ArrayList<String>();
		columns.add("id INTEGER PRIMARY KEY");
		columns.add("author INTEGER NOT NULL");
		columns.add("name TEXT NOT NULL");
		columns.add("description TEXT");

		DBUtils.createTable(m_connection, getTableName(), columns);

		PreparedStatement ps = m_connection.prepareStatement("INSERT INTO "+getTableName()+" (id,author,name) VALUES (?,?,?)");

		for (String key: collectionKeys)
		{
			SunnahCollection sc = m_codeToCollection.get(key);

			if (sc != null)
			{
				int i = 0;
				ps.setInt(++i, sc.id);
				ps.setInt(++i, sc.author);
				ps.setString(++i, sc.name);
				ps.execute();
			}
		}

		m_connection.commit();
		ps.close();
	}


	@Override
	/**
	 * 
	 * @param code Code (ie: bukhari), returns the ID associated with that collection in the
	 * table.
	 * @return
	 */
	public int getIdFor(String code) {
		return m_codeToCollection.get(code).id;
	}


	@Override
	public void setConnection(Connection c) {
		m_connection = c;
	}


	@Override
	public String getTableName() {
		return "collections";
	}


	@Override
	public void setLanguage(String language)
	{
		boolean arabic = language.equals("arabic");

		m_codeToCollection.clear();
		m_codeToCollection.put("abudawud", new SunnahCollection(1,52, arabic ? "سنن أبي داود" : "Sunan Abi Dawud") );
		m_codeToCollection.put("adab", new SunnahCollection(2,109, arabic ? "الأدب المفرد" : "Adab Al-Mufrad") );
		m_codeToCollection.put("bukhari", new SunnahCollection(3,109, arabic ? "صحيح البخاري" : "Sahih al-Bukhari") );
		m_codeToCollection.put("bulugh", new SunnahCollection(4,168, arabic ? "بلوغ المرام" : "Bulugh al-Maram") );
		m_codeToCollection.put("ibnmajah", new SunnahCollection(5,622, arabic ? "سنن ابن ماجه" : "Sunan Ibn Majah") );
		m_codeToCollection.put("malik", new SunnahCollection(6,79, arabic ? "موطأ مالك" : "Muwatta Malik") );
		m_codeToCollection.put("muslim", new SunnahCollection(7,129, arabic ? "صحيح مسلم" : "Sahih Muslim") );
		m_codeToCollection.put("nasai", new SunnahCollection(8,164, arabic ? "سنن النسائي" : "Sunan an-Nasa'i") );
		m_codeToCollection.put("nawawi40", new SunnahCollection(9,305, arabic ? "الأربعون النووية" : "40 Hadith Nawawi") );
		m_codeToCollection.put("qudsi40", new SunnahCollection(10,129, arabic ? "الحديث القدسي" : "40 Hadith Qudsi") );
		m_codeToCollection.put("riyadussaliheen", new SunnahCollection(11,305, arabic ? "رياض الصالحين" : "Riyad as-Salihin") );
		m_codeToCollection.put("tirmidhi", new SunnahCollection(12,543, arabic ? "جامع الترمذي" : "Jami` at-Tirmidhi") );
	}


	@Override
	public void createIndices() throws SQLException
	{
	}
}