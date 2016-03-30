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
	private static final String KEY_GRADE_2 = "grade2";
	private static final String KEY_GRADE_1 = "grade1";
	private String m_primaryKey;

	public SunnahReader(String pk)
	{
		m_primaryKey = pk;
	}


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


	public Narration parseNarration(JSONObject json)
	{
		Narration n = new Narration();
		n.arabicId = readInt(json, "matchingArabicURN");
		n.chapter = new Chapter( (String)json.get("babName"), readInt(json, "babNumber") );
		n.book = new Book( readInt(json, "bookID"), (String)json.get("bookName") );
		n.hadithNumber = (String)json.get("hadithNumber");
		n.inBookNumber = readInt(json, "ourHadithNumber");
		n.id = readInt(json, m_primaryKey);
		n.text = (String)json.get("hadithText");
		n.grading = (String)json.get(KEY_GRADE_1);

		if ( ( n.grading == null || n.grading.isEmpty() ) && json.containsKey(KEY_GRADE_2) ) {
			n.grading = (String)json.get(KEY_GRADE_2);
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