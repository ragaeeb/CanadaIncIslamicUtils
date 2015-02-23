package com.canadainc.quran10;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import com.canadainc.common.io.IOUtils;

public class ChapterExtractor
{
	private String m_path;
	private List<String> m_result;
	
	public List<String> getResult()
	{
		return m_result;
	}


	public ChapterExtractor(String path)
	{
		m_path = path;
	}
	
	
	public void load() throws IOException
	{
		String[] chapters = IOUtils.readFile(m_path, StandardCharsets.UTF_16).split("\\$");
		m_result = new ArrayList<String>(114);
		
		for (String chapter: chapters) {
			m_result.add(chapter);
		}
	}
}