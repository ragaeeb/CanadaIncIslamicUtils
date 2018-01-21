package com.canadainc.sunnah10.processors.shamela;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.canadainc.common.io.DBUtils;
import com.canadainc.common.text.TextUtils;
import com.canadainc.sunnah10.Narration;
import com.canadainc.sunnah10.processors.Processor;
import com.canadainc.sunnah10.utils.SunnahUtils;

public class ShamelaPopulator implements DatabasePopulator
{
	private String m_collection;
	private String m_path;
	protected Processor m_processor;

	public ShamelaPopulator(String collection, Processor processor)
	{
		m_collection = collection;
		m_processor = processor;
	}


	public ShamelaPopulator(String collection, String path, Processor processor)
	{
		m_collection = collection;
		m_processor = processor;
		m_path = path;
	}


	/* (non-Javadoc)
	 * @see com.canadainc.sunnah10.processors.shamela.DatabasePopulator#process(java.sql.Connection)
	 */
	@Override
	public void process(Connection c) throws Exception
	{
		String query = "SELECT * FROM "+m_collection;

		if (m_path != null) {
			query += " WHERE path=?";
		}

		query += " ORDER BY id";

		PreparedStatement ps = c.prepareStatement(query);

		if (m_path != null) {
			ps.setString(1, m_path);
		}

		ResultSet rs = ps.executeQuery();

		int lastSize = 0;

		while ( rs.next() )
		{
			Object obj = JSONValue.parse( rs.getString("json") );
			JSONArray arr = new JSONArray();

			if (obj instanceof JSONArray) {
				arr = (JSONArray)obj;
			} else {
				arr.add(obj);
			}

			for (Object o: arr)
			{
				JSONObject json = (JSONObject)o;

				if (json == null) {
					System.err.println("Invalid JSON: "+rs.getString("file_name")+"; "+rs.getInt("id"));
				}			

				boolean process = m_processor.preprocess(json);

				if (process)
				{
					int pageNumber = m_processor.getPageNumber(json);

					try {
						m_processor.process(json);
					} catch (Exception ex) {
						System.err.println("ErrorOnPage: "+pageNumber);
						ex.printStackTrace();
						throw ex;
					}

					List<Narration> narrations = m_processor.getNarrations();
					int newSize = narrations.size();

					for (int i = lastSize; i < newSize; i++) {
						narrations.get(i).pageNumber = pageNumber;
					}

					lastSize = newSize;
				}
			}
		}

		ps.close();
		rs.close();
	}


	/* (non-Javadoc)
	 * @see com.canadainc.sunnah10.processors.shamela.DatabasePopulator#write(java.sql.Connection)
	 */
	@Override
	public void write(Connection c) throws SQLException
	{
		PreparedStatement ps = DatabaseUtils.createPopulation(m_collection, c);

		List<Narration> narrations = SunnahUtils.sort(m_processor.getNarrations(), true);
		
		for (Narration n: narrations)
		{
			int i = 0;

			ps.setInt(++i, n.id);
			ps.setString(++i, n.text.trim());
			ps.setString(++i, TextUtils.normalize(n.text.trim()));
			DBUtils.setNullInt(++i, n.pageNumber, ps);

			if ( n.grading == null || n.grading.trim().isEmpty() ) {
				ps.setNull(++i, Types.OTHER);
			} else {
				ps.setString(++i, n.grading.trim());
			}

			ps.execute();
		}
		
		DatabaseUtils.createIndex(c);

		c.commit();
		ps.close();
	}
}