package com.canadainc.sunnah10.processors.shamela;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.canadainc.common.io.DBUtils;

public class DatabaseUtils
{
	private static final String TARGET_DB_NAME = "target";
	private static final String TABLE_NAME = "narrations";

	public static final PreparedStatement createPopulation(String collection, Connection c, String... fields) throws SQLException
	{
		File f = new File(collection+".db");

		if ( f.exists() ) {
			f.delete();
		}

		c.setAutoCommit(true);

		System.out.println("Creating database...");			

		DBUtils.attach(c, collection+".db", TARGET_DB_NAME);

		c.setAutoCommit(false);

		List<String> columns = DBUtils.createNullColumns( DBUtils.createNotNullColumns("id INTEGER", "arabic_vowelled TEXT", "arabic_plain TEXT"), "indexed_number INTEGER", "translation TEXT", "translation_src TEXT", "commentary TEXT", "grading TEXT", "page_number INTEGER" );
		String tableName = TARGET_DB_NAME+"."+TABLE_NAME;
		DBUtils.createTable(c, tableName, columns);

		List<String> popFields = new ArrayList<>();
		popFields.add("indexed_number");
		popFields.add("arabic_vowelled");
		popFields.add("arabic_plain");
		popFields.add("page_number");
		popFields.add("grading");

		for (String field: fields) {
			popFields.add(field);
		}

		PreparedStatement ps = DBUtils.createInsert(c, tableName, popFields);

		return ps;
	}


	public static final void createIndex(Connection c) throws SQLException {
		DBUtils.execStatement(c, "CREATE INDEX IF NOT EXISTS "+TARGET_DB_NAME+".indexed_number ON "+TABLE_NAME+" (indexed_number)");
	}
}