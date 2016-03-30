/**
 * 
 */
package com.canadainc.sunnah10;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

import com.canadainc.islamicutils.io.DBUtils;

/**
 * @author rhaq
 *
 */
public class SunnahGradeTable implements SunnahTable
{
	private Connection m_connection;

	/**
	 * 
	 */
	public SunnahGradeTable()
	{
	}


	public void process(Map<Integer, Grade> grades) throws SQLException
	{
		ArrayList<String> columns = new ArrayList<String>();
		columns.add("id INTEGER PRIMARY KEY");
		columns.add("narration_id INTEGER NOT NULL");
		columns.add("author_id INTEGER NOT NULL");
		columns.add("body TEXT NOT NULL");
		columns.add("reference TEXT");

		DBUtils.createTable(m_connection, getTableName(), columns);

		PreparedStatement ps = m_connection.prepareStatement("INSERT INTO "+getTableName()+"(narration_id,author_id,body) VALUES (?,?,?)");

		for ( Integer narrationId: grades.keySet() )
		{
			Grade g = grades.get(narrationId);

			int i = 0;
			ps.setInt(++i, narrationId);
			ps.setInt(++i, g.author);
			ps.setString(++i, g.body);
			ps.execute();
		}

		m_connection.commit();
		ps.close();
	}


	/* (non-Javadoc)
	 * @see com.canadainc.sunnah10.SunnahTable#setConnection(java.sql.Connection)
	 */
	@Override
	public void setConnection(Connection c) {
		m_connection = c;
	}

	/* (non-Javadoc)
	 * @see com.canadainc.sunnah10.SunnahTable#getTableName()
	 */
	@Override
	public String getTableName() {
		return "grades";
	}

	/* (non-Javadoc)
	 * @see com.canadainc.sunnah10.SunnahTable#setLanguage(java.lang.String)
	 */
	@Override
	public void setLanguage(String language) {}
}