package com.canadainc.sunnah10;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.canadainc.islamicutils.io.DBUtils;

public class SunnahChaptersTable implements SunnahPrimaryTable<Chapter>
{
	private Connection m_connection;
	private Map<Chapter, Integer> m_chapterToId;

	public SunnahChaptersTable() {
		m_chapterToId = new HashMap<Chapter, Integer>();
	}
	
	
	@Override
	public void process(Collection<Chapter> chapters) throws SQLException
	{
		ArrayList<String> columns = new ArrayList<String>();
		columns.add("id INTEGER PRIMARY KEY");
		columns.add("number INTEGER");
		columns.add("title TEXT NOT NULL");

		DBUtils.createTable(m_connection, getTableName(), columns);
		
		PreparedStatement ps = m_connection.prepareStatement("INSERT INTO "+getTableName()+" VALUES "+DBUtils.generatePlaceHolders(columns));
		
		int x = 0;
		
		for (Chapter c: chapters)
		{
			int i = 0;
			ps.setInt(++i, ++x);
			DBUtils.setNullInt(++i, c.number, ps);
			ps.setString(++i, c.title);
			ps.execute();
			
			m_chapterToId.put(c,x);
		}
		
		m_connection.commit();
		ps.close();
	}
	
	

	@Override
	public int getIdFor(Chapter c)
	{
		Integer value = m_chapterToId.get(c);
		return value != null ? value.intValue() : 0;
	}



	@Override
	public void setConnection(Connection c) {
		m_connection = c;
	}


	@Override
	public String getTableName() {
		return "chapters";
	}


	@Override
	public void setLanguage(String language) {}
}