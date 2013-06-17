package com.canadainc.quran10;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;

public class Transliterator
{
	private String m_input;
	private String m_output;
	private Map<Pattern, String> m_priorityMap = new HashMap<Pattern, String>();
	private Map<Pattern, String> m_replaceMap = new HashMap<Pattern, String>();
	
	public Transliterator(String input, String output)
	{
		m_input = input;
		m_output = output;
		
		m_replaceMap.put( Pattern.compile("<u>", Pattern.CASE_INSENSITIVE), "<span style='text-decoration:underline'>" );
		m_replaceMap.put( Pattern.compile("<b>", Pattern.CASE_INSENSITIVE), "<span style='font-weight:bold'>" );
		m_replaceMap.put( Pattern.compile("<i>", Pattern.CASE_INSENSITIVE), "<span style='font-style:italic'>" );
		m_replaceMap.put( Pattern.compile("</u>", Pattern.CASE_INSENSITIVE), "</span>" );
		m_replaceMap.put( Pattern.compile("</i>", Pattern.CASE_INSENSITIVE), "</span>" );
		m_replaceMap.put( Pattern.compile("</b>", Pattern.CASE_INSENSITIVE), "</span>" );
		
		m_priorityMap.put( Pattern.compile("<b></b>", Pattern.CASE_INSENSITIVE), "" );
		m_priorityMap.put( Pattern.compile("<u></u>", Pattern.CASE_INSENSITIVE), "" );
		m_priorityMap.put( Pattern.compile("</u></i>", Pattern.CASE_INSENSITIVE), "</span>" );
		m_priorityMap.put( Pattern.compile("<i><u>", Pattern.CASE_INSENSITIVE), "<span style='text-decoration:underline;font-style:italic'>" );
	}
	
	
	public String translate() throws IOException
	{
		BufferedReader br = new BufferedReader( new FileReader(m_input) );
		Path path = Paths.get(m_output);
		BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8);
		
	    try {
	        StringBuilder sb = new StringBuilder();
	        String line = br.readLine();

	        while (line != null) {
	        	if ( !line.isEmpty() ) {
	        		String translated = translateLine(line);
	        		writer.write(translated);
	        		writer.newLine();
		            sb.append(translated);
		            sb.append("\n");
	        	}
	            line = br.readLine();
	        }
	        
	        return sb.toString();
	    } finally {
	        br.close();
	        writer.close();
	    }
	}
	
	
	public String translateLine(String line)
	{
		String[] tokens = line.split("\\|");
		line = tokens[2];
		
		for ( Pattern p: m_priorityMap.keySet() ) {
			line = p.matcher(line).replaceAll( m_priorityMap.get(p) );
		}
		
		for ( Pattern p: m_replaceMap.keySet() ) {
			line = p.matcher(line).replaceAll( m_replaceMap.get(p) );
		}
	    
	    line = tokens[0]+","+tokens[1]+",<html>"+line+"</html>";
		
		return line;
	}
}