package com.canadainc.sunnah10.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

public class Dictionary
{
	private Map<String, String> m_typos;

	public Dictionary() {
		m_typos = new LinkedHashMap<>();
	}


	public void add(String replacement, String...matches)
	{
		for (String match: matches) {
			m_typos.put(match, replacement);
		}
	}


	public void addCased(String replacement, String...matches)
	{
		for (String match: matches)
		{
			m_typos.put(match.toLowerCase(), replacement);
			m_typos.put(match.toUpperCase(), replacement);
		}
	}


	public void apply(Connection connection, String table, String pathPattern) throws SQLException
	{
		connection.setAutoCommit(false);

		PreparedStatement ps = connection.prepareStatement("UPDATE "+table+" SET json=replace(json,?,?) WHERE path LIKE '%"+pathPattern+"%'");

		for (String original: m_typos.keySet())
		{
			String replacement = m_typos.get(original);

			int i = 0;
			ps.setString(++i, original);
			ps.setString(++i, replacement);
			ps.execute();
		}

		connection.commit();
		ps.close();
	}
	
	
	public void reset() {
		m_typos.clear();
	}


	public String correctTypos(String body)
	{
		for ( String key: m_typos.keySet() ) {
			body = body.replace( key, m_typos.get(key) );
		}

		return body;
	}
}