package com.canadainc.sunnah10.shamela;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.canadainc.common.io.IOUtils;
import com.canadainc.islamicutils.io.DBUtils;
import com.canadainc.sunnah10.Narration;

public class ShamelaPopulator
{
	private String m_path;
	private ShamelaProcessor m_processor;
	private static final String TABLE_NAME = "narrations";

	public ShamelaPopulator(String path, ShamelaProcessor processor)
	{
		m_path = path;
		m_processor = processor;
	}
	
	
	public void process() throws IOException
	{
		File[] all = new File(m_path).listFiles( new FilenameFilter()
		{
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".txt");
			}
		});
		
		for (File f: all)
		{
			JSONObject json = (JSONObject)JSONValue.parse( IOUtils.readFileUtf8(f) );
			m_processor.preprocess(json);

			Document d = Jsoup.parse( json.get("content").toString() );
			
			try {
				m_processor.process( d.body().childNodes(), json );
				ShamelaUtils.assertSequential(m_processor.getNarrations(), true);
			} catch (Exception ex) {
				System.err.println( f.getName()+"; "+ex.getMessage() );
				//throw ex;
			}
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