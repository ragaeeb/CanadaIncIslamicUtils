package com.canadainc.sunnah10.utils;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.canadainc.common.io.IOUtils;
import com.canadainc.islamicutils.io.DBUtils;

public class JsonToSqliteConverter
{
	private String m_folder;

	public JsonToSqliteConverter(String folder)
	{
		m_folder = folder;
	}

	public void convert() throws Exception
	{
		File file = new File(m_folder);
		File[] all = file.listFiles( new FilenameFilter()
		{
			@Override
			public boolean accept(File dir, String name) {
				return !name.equals(".DS_Store");
			}
		});

		Connection c = DriverManager.getConnection("jdbc:sqlite:"+m_folder+"/"+file.getName()+".db");
		c.setAutoCommit(false);

		for (File f: all) {
			parseFile(c,f);
		}

		c.commit();
		c.close();
	}

	public void parseFile(Connection c, File f) throws Exception
	{
		List<String> result = Files.readAllLines( Paths.get( f.getPath() ), StandardCharsets.UTF_8).stream().filter(line -> line.startsWith("[")).collect(Collectors.toList()); // discard comments
		String table = f.getName().split("\\.")[0];

		for (String s: result)
		{
			JSONArray arr = (JSONArray)JSONValue.parse(s);

			if ( arr.isEmpty() ) {
				break;
			}

			JSONObject json = (JSONObject)arr.get(0);
			Collection<String> keys = json.keySet();
			List<String> columns = keys.stream().map(x -> x+" TEXT").collect(Collectors.toList());
			DBUtils.createTable(c, table, columns);
			PreparedStatement ps = c.prepareStatement("INSERT INTO "+table+" VALUES "+DBUtils.generatePlaceHolders(columns));

			for (Object o: arr)
			{
				json = (JSONObject)o;
				int i = 0;

				for (String key: keys) {
					ps.setString(++i, json.get(key).toString());
				}

				ps.execute();
			}

			ps.close();
		}
	}
}