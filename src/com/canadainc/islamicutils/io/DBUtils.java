package com.canadainc.islamicutils.io;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.jsoup.helper.StringUtil;

public class DBUtils
{
	public static void createTable(Connection c, String table, Collection<String> columns) throws SQLException
	{
		PreparedStatement ps = c.prepareStatement("CREATE TABLE IF NOT EXISTS "+table+" ("+StringUtil.join(columns, ",")+")");
		ps.execute();
		ps.close();
	}


	public static void isolateColumnNames(List<String> columns, String... toRemove)
	{
		//TODO: Bring all the manual usages to use this one
		
		for (int i = columns.size()-1; i >= 0; i--) {
			columns.set( i, columns.get(i).split(" ")[0] );
		}

		columns.removeAll( Arrays.asList(toRemove) );
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
		// TODO: http://stackoverflow.com/questions/1235179/simple-way-to-repeat-a-string-in-java

		for (String s: columns) {
			placeHolders.add("?");
		}

		return "("+StringUtil.join(placeHolders, ",")+")";
	}
}