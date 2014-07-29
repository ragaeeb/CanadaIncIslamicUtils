package com.canadainc.sunnah10;

import java.io.File;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.canadainc.common.io.IOUtils;

public class HisnulMuslimParser
{
	private static String JSON_START = "data: ";
	
	public HisnulMuslimParser()
	{
	}
	
	
	public void parse(String file) throws IOException
	{
		String jsonData = IOUtils.readFileUtf8( new File(file) );
		int start = jsonData.indexOf(JSON_START);
		int end = jsonData.lastIndexOf("    }");
		jsonData = jsonData.substring(start+JSON_START.length(), end).trim();
		
		JSONArray array = (JSONArray)JSONValue.parse(jsonData);
		
		for (int i = 0; i < array.size(); i++)
		{
			JSONObject json = (JSONObject)array.get(i);
			String chapterTitle = (String)json.get("title");
			JSONArray duas = (JSONArray)json.get("duas");
			
			System.out.println("title: "+chapterTitle);
			
			for (int j = 0; j < duas.size(); j++)
			{
				json = (JSONObject)array.get(j);
			}
		}
	}
}