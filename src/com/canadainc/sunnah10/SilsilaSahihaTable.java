package com.canadainc.sunnah10;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;

import com.canadainc.islamicutils.io.DBUtils;

public class SilsilaSahihaTable implements SunnahPrimaryTable<Narration>
{
	private Connection m_connection;
	
	public SilsilaSahihaTable()
	{
	}

	@Override
	public void setConnection(Connection c) {
		m_connection = c;
	}

	@Override
	public String getTableName() {
		return "narrations";
	}

	@Override
	public void setLanguage(String language)
	{
	}

	@Override
	public void createIndices() throws SQLException
	{
	}

	@Override
	public int getIdFor(Narration x) {
		return 0;
	}

	@Override
	public void process(Collection<Narration> narrations) throws SQLException
	{
		ArrayList<String> columns = new ArrayList<String>();
		columns.add("id INTEGER PRIMARY KEY");
		columns.add("ar_id INTEGER NOT NULL");
		columns.add("ar_body TEXT NOT NULL");
		columns.add("en_body TEXT");
		columns.add("grading TEXT");
		columns.add("translation_src TEXT");
		columns.add("book_number INTEGER NOT NULL");
		columns.add("book_name TEXT");
		columns.add("in_book_number INTEGER");

		DBUtils.createTable(m_connection, getTableName(), columns);
		
		PreparedStatement ps = m_connection.prepareStatement("INSERT INTO "+getTableName()+" VALUES "+DBUtils.generatePlaceHolders(columns));
		int idx = 0;

		for (Narration n: narrations)
		{
			int i = 0;
			ps.setInt(++i, ++idx);
			ps.setInt(++i, n.id);
			ps.setString(++i, n.text.trim());
			ps.setNull(++i, Types.OTHER);
			ps.setString(++i, n.grading != null ? n.grading.trim() : n.grading);
			ps.setNull(++i, Types.OTHER);
			ps.setInt(++i, n.book.id);
			ps.setNull(++i, Types.OTHER);
			
			if (n.inBookNumber > 0) {
				ps.setInt(++i, n.inBookNumber);
			} else {
				ps.setNull(++i, Types.OTHER);
			}
			
			ps.execute();
		}

		m_connection.commit();
		ps.close();
	}
}