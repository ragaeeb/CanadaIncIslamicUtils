package com.canadainc.sunnah10.processors.shamela;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.canadainc.islamicutils.io.DBUtils;
import com.canadainc.sunnah10.Narration;
import com.canadainc.sunnah10.processors.Processor;

public class ShamelaPopulator
{
	private String m_collection;
	private Processor m_processor;
	private static final String TABLE_NAME = "narrations";

	public ShamelaPopulator(String path, Processor processor)
	{
		m_collection = path;
		m_processor = processor;
	}


	public void process(Connection c) throws Exception
	{
		PreparedStatement ps = c.prepareStatement("SELECT * FROM "+m_collection+" ORDER BY id");
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


	public void validateSequence()
	{
		int n = m_processor.getNarrations().size();

		for (int i = 0; i < n-1; i++)
		{
			Narration current = m_processor.getNarrations().get(i);
			Narration next = m_processor.getNarrations().get(i+1);

			if (next.id-current.id != 1) {
				System.err.println("Page "+current.pageNumber+"; IdDiff(current,next): ("+current.id+"; "+next.id+")");
			}
		}
	}


	public void validateGrades()
	{
		HashMap<String, Integer> gradeToCount = new HashMap<>();
		HashMap<String, Integer> gradeToPage = new HashMap<>();

		for (Narration n: m_processor.getNarrations())
		{
			if ( n.grading == null && m_processor.hasGrade(n.id) ) {
				System.err.println("Page "+n.pageNumber+"; NoGrade(current): ("+n.id+")");
			} else if (n.grading != null) {
				String grading = n.grading.trim();
				Integer count = gradeToCount.get(grading.trim());

				if (count == null) {
					count = 0;
				}

				gradeToCount.put(grading, ++count);
				gradeToPage.put(grading, n.pageNumber);
			}
		}

		for (String key: gradeToCount.keySet()) {
			System.out.println(key+": "+gradeToCount.get(key)+"; page="+gradeToPage.get(key));
		}
	}


	public void write(Connection c) throws SQLException
	{
		c.setAutoCommit(false);

		System.out.println("Creating database...");			

		List<String> columns = DBUtils.createNullColumns( DBUtils.createNotNullColumns("id INTEGER", "ar_id INTEGER", "ar_body TEXT"), "en_body TEXT", "translation_src TEXT", "commentary TEXT", "chapter_number INTEGER", "chapter_name TEXT", "book_number INTEGER", "book_name TEXT" );
		DBUtils.createTable(c, TABLE_NAME, columns);		
		PreparedStatement ps = c.prepareStatement("INSERT INTO "+TABLE_NAME+" VALUES "+DBUtils.generatePlaceHolders(columns));

		for (Narration n: m_processor.getNarrations())
		{
			int i = 0;

			ps.setInt(++i, n.id);
			ps.setInt(++i, n.id);
			ps.setString(++i, n.text.trim());
			ps.setNull(++i, Types.OTHER);
			ps.setNull(++i, Types.OTHER);
			ps.setNull(++i, Types.OTHER);
			ps.setNull(++i, Types.OTHER);
			ps.setNull(++i, Types.OTHER);
			ps.setNull(++i, Types.OTHER);
			ps.setNull(++i, Types.OTHER);

			ps.execute();
		}

		c.commit();
		ps.close();
	}
}