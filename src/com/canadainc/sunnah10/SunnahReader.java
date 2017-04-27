package com.canadainc.sunnah10;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.canadainc.common.io.IOUtils;

public class SunnahReader
{
	public List<Narration> readNarrations(File filePath) throws IOException
	{
		ArrayList<Narration> narrations = new ArrayList<Narration>();
		JSONArray array = (JSONArray)JSONValue.parse( IOUtils.readFileUtf8(filePath) );

		if (array != null)
		{
			for (int j = 0; j < array.size(); j++)
			{
				JSONObject json = (JSONObject)array.get(j);
				narrations.add( parseNarration(json) );
			}
		}

		return narrations;
	}

	
	private static final Object extractAvailable(String[] keys, JSONObject json)
	{
		for (String key: keys)
		{
			if ( json.containsKey(key) ) {
				return json.get(key);
			}
		}
		
		return null;
	}
	

	public Narration parseNarration(JSONObject json)
	{
		Narration n = new Narration();
		n.chapter = new Chapter( (String)json.get("babName"), readInt(json, "babNumber") );
		n.book = new Book( readInt(json, "bookID"), (String)json.get("bookName") );
		n.hadithNumber = (String)json.get("hadithNumber");
		n.inBookNumber = readInt(json, "ourHadithNumber");
		n.text = (String)json.get("hadithText");
		n.grading = (String)extractAvailable(new String[]{"grade1", "grade2"}, json);
		
		if ( json.containsKey("arabicURN") ) {
			n.id = Integer.parseInt( (String)json.get("arabicURN") );
		} else if ( json.containsKey("matchingArabicURN") ) {
			n.id = Integer.parseInt( (String)json.get("matchingArabicURN") );
		} else {
			System.err.println("UnmatchedTranslation: "+json.toJSONString());
		}
		
		if (n.id == 0) {
			System.err.println("UnsetID: "+json.toJSONString());
		}

		return n;
	}


	private static int readInt(JSONObject json, String key)
	{
		int result = 0;
		String toConvert = (String)json.get(key);

		if (toConvert != null)
		{
			try {
				result = Integer.parseInt(toConvert);
			} catch (NumberFormatException ex) {
				double d = Double.parseDouble(toConvert);
				result = (int)d;
			}
		}

		return result;
	}
}