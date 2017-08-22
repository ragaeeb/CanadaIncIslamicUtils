package com.canadainc.sunnah10.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.sqlite.util.StringUtils;

import com.canadainc.common.io.IOUtils;
import com.canadainc.islamicutils.io.DBUtils;
import com.canadainc.sunnah10.Narration;

public class PlainTextParser
{
	private List<Narration> m_narrations = new ArrayList<>();
	private List<String> ids = new ArrayList<>();
	
	
	public PlainTextParser()
	{
	}
	
	
	public List<Narration> getNarrations() {
		return m_narrations;
	}
	
	
	public void read(String path) throws IOException
	{
		String[] ahadeeth = IOUtils.readFileUtf8( new File(path) ).trim().split("\n\n");
		
		for (String hadeeth: ahadeeth)
		{
			int index = hadeeth.indexOf(":");
			
			if (index > 0)
			{
				Narration n = new Narration( Integer.parseInt( hadeeth.substring(0,index) ) );
				n.text = "The Prophet ﷺ said: "+hadeeth.substring(index+1).trim();
				n.commentary = "https://iskandrani.wordpress.com/2008/02/09/selections-from-sahih-al-jami-as-saghir/";
				m_narrations.add(n);
				
				ids.add( String.valueOf(n.id) );
			} else if ( !m_narrations.isEmpty() ) {
				m_narrations.get( m_narrations.size()-1 ).text += "\n\n"+hadeeth;
			}
		}
	}
	
	
	public void write(Connection c) throws SQLException
	{
		c.setAutoCommit(false);

		System.out.println("Creating database...");

		PreparedStatement ps = c.prepareStatement("SELECT ar_id FROM narrations WHERE collection=? AND ar_id IN ("+StringUtils.join(ids, ",")+") AND en_body NOT NULL");
		ps.setString(1, "jaami");
		ResultSet rs = ps.executeQuery();
		
		Set<Integer> excluded = new HashSet<>();
		
		while ( rs.next() ) {
			excluded.add( rs.getInt("ar_id") );
		}
		
		//System.out.println(excluded);

		ps = c.prepareStatement("UPDATE narrations SET en_body=?, translation_src=? WHERE ar_id=?");

		for (Narration n: m_narrations)
		{
			if ( excluded.contains(n.id) ) {
				System.err.println("Skipping: "+n.id);
			} else {
				int i = 0;

				ps.setString(++i, n.text);
				ps.setString(++i, n.commentary);
				ps.setInt(++i, n.id);

				ps.execute();
			}
		}

		c.commit();
		ps.close();
	}
}