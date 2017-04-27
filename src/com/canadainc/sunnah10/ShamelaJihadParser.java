package com.canadainc.sunnah10;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import com.canadainc.common.io.IOUtils;
import com.canadainc.islamicutils.io.DBUtils;

public class ShamelaJihadParser implements SunnahPrimaryTable<Narration>
{
	private ArrayList<Narration> m_narrations;
	private Connection m_connection;

	public ShamelaJihadParser()
	{
		m_narrations = new ArrayList<>();
	}

	public void readNarrations(File f) throws IOException
	{
		JSONObject json = (JSONObject)JSONValue.parse( IOUtils.readFileUtf8(f) );
		Document d = Jsoup.parse( (String)json.get("content") );
		Narration n = null;

		for (Node e: d.body().childNodes())
		{
			String attribute = e.attr("class");

			if ( e.nodeName().equals("span") && attribute.equals("red") && ( e.childNode(0) instanceof TextNode ) )
			{
				if (n != null) { // 2 narrations in 1
					m_narrations.add(n);
				}
				
				TextNode tn = (TextNode)e.childNode(0);
				n = new Narration();
				String hadithNum = tn.text().split("-")[0].trim();
				n.id = Integer.parseInt(hadithNum);
				n.text = "";
			} else if ( e.nodeName().equals("#text") ) {
				String body = ((TextNode)e).text();
				
				if ( ( body.startsWith("أَخْبَرَنَا") || body.startsWith("حَدَّثَنَا") ) && (n == null) )
				{
					Narration prev = m_narrations.get( m_narrations.size()-1 );
					n = new Narration();
					n.id = prev.id+1;
					n.text = "";
				} else if ( ( body.startsWith("قَالَ") || body.startsWith("فَقَالَ") ) && (n == null) ) {
					Narration prev = m_narrations.get( m_narrations.size()-1 );
					prev.text += "\n\n"+body;
				}
				
				if (n != null) {
					n.text += body;
				}
			} else if ( e.nodeName().equals("span") && attribute.equals("title") && ( e.childNode(0) instanceof TextNode ) ) {
				TextNode tn = (TextNode)e.childNode(0);
				n.text += tn.text();
			} 
		}
		
		if (n != null) {
			m_narrations.add(n);
		}
	}

	/**
	 * @return the narrations
	 */
	public Collection<Narration> getNarrations()
	{
		return m_narrations;
	}

	@Override
	public void setConnection(Connection c)
	{
		m_connection = c;
	}

	@Override
	public String getTableName()
	{
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
	public int getIdFor(Narration x)
	{
		return 0;
	}

	@Override
	public void process(Collection<Narration> narrations) throws SQLException
	{
		ArrayList<String> columns = new ArrayList<String>();
		columns.add("ar_id INTEGER PRIMARY KEY");
		columns.add("ar_body TEXT NOT NULL");
		columns.add("en_body TEXT");
		columns.add("translation_src TEXT");
		columns.add("commentary TEXT");
		columns.add("chapter_name TEXT");

		DBUtils.createTable(m_connection, getTableName(), columns);
		
		PreparedStatement ps = m_connection.prepareStatement("INSERT INTO "+getTableName()+" VALUES "+DBUtils.generatePlaceHolders(columns));

		for (Narration n: narrations)
		{
			int i = 0;
			ps.setInt(++i, n.id);
			ps.setString(++i, n.text.trim());
			ps.setNull(++i, Types.OTHER);
			ps.setNull(++i, Types.OTHER);
			ps.setNull(++i, Types.OTHER);
			ps.setNull(++i, Types.OTHER);
			System.out.println(n.id);
			
			ps.execute();
		}

		m_connection.commit();
		ps.close();
	}
}