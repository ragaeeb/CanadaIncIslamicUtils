package com.canadainc.sunnah10.shamela;

import java.util.HashSet;

import org.json.simple.JSONObject;

public class TypoProcessor
{
	private HashSet<Typo> m_typos;
	private HashSet<Typo> m_regexTypos;
	private HashSet<StripperData> m_toStrip;

	public TypoProcessor()
	{
		m_typos = new HashSet<>();
		m_regexTypos = new HashSet<>();
		m_toStrip = new HashSet<>();
	}


	public void add(int pageNumber, String value, String replacement)
	{
		m_typos.add(new Typo(pageNumber, value, replacement));
	}
	
	
	public void addRegexed(int pageNumber, String value, String replacement)
	{
		m_regexTypos.add(new Typo(pageNumber, value, replacement));
	}
	
	
	public void addNumericListStripper(int pageNumber, String startMatcher, String endMatcher, boolean forward) {
		addContentStripper(pageNumber, startMatcher, endMatcher, "[1-9]{1,2} - ", "", forward);
	}
	
	
	public void addNumericListStripper(int pageNumber, String startMatcher, String endMatcher) {
		addNumericListStripper(pageNumber, startMatcher, endMatcher, true);
	}
	
	
	public void addContentStripper(int pageNumber, String startMatcher, String endMatcher, String inner, String replacement, boolean forward) {
		m_toStrip.add(new StripperData(pageNumber, startMatcher, endMatcher, inner, replacement, forward));
	}

	
	public void process(JSONObject json, String value, String replacement, boolean regex)
	{
		String content = json.get("content").toString();
		content = regex ? content.replaceAll(value, replacement) : content.replace(value, replacement);
		json.replace("content", content);
	}
	

	public void process(JSONObject json)
	{
		int pageNumber = Integer.parseInt( json.get("pid").toString() );

		for (Typo typo: m_typos)
		{
			if (typo.pageNumber == pageNumber) {
				process(json, typo.value, typo.replacement, false);
			}
		}
		
		for (Typo typo: m_regexTypos)
		{
			if (typo.pageNumber == pageNumber) {
				process(json, typo.value, typo.replacement, true);
			}
		}
		
		for (StripperData data: m_toStrip)
		{
			if (data.pageNumber == pageNumber)
			{
				String content = json.get("content").toString();
				int start = data.forward ? content.indexOf(data.startMatcher) : content.lastIndexOf(data.startMatcher);
				int end = data.forward ? content.indexOf(data.endMatcher) : content.lastIndexOf(data.endMatcher);
				String beginning = content.substring(0, start);
				String hadeeth = content.substring(start, end).replaceAll("<br />", "").replaceAll("<span class=\"red\">"+data.inner+"</span>", data.replacement);
				String ending = content.substring(end);
				content = beginning+hadeeth+ending;
				json.replace("content", content);
			}
		}
	}
}