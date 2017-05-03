package com.canadainc.sunnah10.shamela;

import java.util.HashSet;

import org.json.simple.JSONObject;

public class TypoProcessor
{
	private HashSet<Typo> m_typos;

	public TypoProcessor()
	{
		m_typos = new HashSet<>();
	}


	public void add(int pageNumber, String value, String replacement)
	{
		m_typos.add(new Typo(pageNumber, value, replacement));
	}

	
	public void process(JSONObject json, String value, String replacement)
	{
		String content = json.get("content").toString();
		content = content.replace(value, replacement);
		json.replace("content", content);
	}
	

	public void process(JSONObject json)
	{
		int pageNumber = Integer.parseInt( json.get("pid").toString() );

		for (Typo typo: m_typos)
		{
			if (typo.pageNumber == pageNumber) {
				process(json, typo.value, typo.replacement);
			}
		}
	}
}