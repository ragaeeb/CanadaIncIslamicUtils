package com.canadainc.islamicutils.io;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;

import org.jsoup.helper.StringUtil;

public class DBUtils
{
	public static void createTable(Connection c, String table, Collection<String> columns) throws SQLException
	{
		PreparedStatement ps = c.prepareStatement("CREATE TABLE IF NOT EXISTS "+table+" ("+StringUtil.join(columns, ",")+")");
		ps.execute();
		ps.close();
	}
	
	
	public static void cleanUp(String db)
	{
		new File(db).delete();
		new File(db+"-journal").delete();
	}
	
	
	public static void setNullInt(int i, int value, PreparedStatement ps) throws SQLException
	{
		if (value == 0) {
			ps.setNull(i, Types.INTEGER);
		} else {
			ps.setInt(i, value);
		}
	}
	
	
	public static String generatePlaceHolders(Collection<String> columns)
	{
		Collection<String> placeHolders = new ArrayList<String>();
		
		for (String s: columns) {
			placeHolders.add("?");
		}
		
		return "("+StringUtil.join(placeHolders, ",")+")";
	}
}