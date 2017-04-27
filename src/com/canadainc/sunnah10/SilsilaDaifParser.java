package com.canadainc.sunnah10;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import com.canadainc.common.io.IOUtils;

public class SilsilaDaifParser
{
	private Map<String, Narration> m_narrations;

	public SilsilaDaifParser()
	{
		m_narrations = new HashMap<>();
	}


	public Collection<Narration> getNarrations()
	{
		ArrayList<Narration> narrations = new ArrayList<>();
		Collection<Narration> all = m_narrations.values();
		narrations.addAll(all);

		Collections.sort(narrations, new Comparator<Narration>()
		{
			@Override
			public int compare(Narration n1, Narration n2) {
				return n1.id < n2.id ? -1 : 1;
			}

		});
		return narrations;
	}

	public void print() {
		Set<String> keys = m_narrations.keySet();
		ArrayList<Integer> all = new ArrayList<>();

		for (String k: keys) {
			all.add( Integer.parseInt(k) );
		}

		Collections.sort(all);
		int last = 0;

		for (Integer i: all) {
			if (i-last != 1) {
				System.out.println(i-1);
			}

			last = i;
		}

		System.out.println(all.toString());
	}

	Narration readNarrations(File f) throws IOException
	{

		JSONObject json = (JSONObject)JSONValue.parse( IOUtils.readFileUtf8(f) );
		Document d = Jsoup.parse( (String)json.get("content") );
		Narration m_current = null;
		String hadithHeader = (String)json.get("hadith");

		for (Node e: d.body().childNodes())
		{
			String attribute = e.attr("class");

			if ( e.nodeName().equals("span") && ( attribute.equals("red") || attribute.equals("title") ) && ( e.childNode(0) instanceof TextNode ) )
			{
				TextNode tn = (TextNode)e.childNode(0);
				String hadithNum = tn.text().split("\\s+")[0].trim();

				if (hadithNum.matches("\\d+-$")) {
					hadithNum = hadithNum.replaceAll("-", "");
				} else if ( hadithHeader != null && hadithNum.startsWith(hadithHeader)) {
					hadithNum = hadithHeader;
				}

				if ( hadithNum.matches("\\d+$") && hadithHeader != null && !m_narrations.containsKey(hadithNum) && !tn.text().matches("\\d+\\s+-\\s+\\d+$") )
				{
					if (m_current != null) {
						System.out.println(f.getName()+" has multiple narrations! "+tn.text());
					}

					Narration n = new Narration();
					n.hadithNumber = hadithNum;
					n.inBookNumber = Integer.parseInt( (String)json.get("pid") );

					if ( !n.hadithNumber.isEmpty() ) {
						n.hadithNumber = hadithHeader.trim();
					}

					n.id = Integer.parseInt( n.hadithNumber );
					n.book = new Book( Integer.parseInt( (String)json.get("part") ), null );
					m_current = n;

					m_narrations.put(n.hadithNumber, n);
				}
			} else if ( e.nodeName().equals("#text") && m_current != null ) {
				TextNode tn = (TextNode)e;

				if (m_current.text == null) {
					m_current.text = tn.text();
				} else if (m_current.grading == null ) {
					String body = tn.text();
					body = body.replaceAll("[\"\\.]", "");

					if ( body.length() > 3 && body.length() <= 25 && !body.contains("؟") ) {
						m_current.grading = body;
					} else {
						m_current.text += tn.text();
					}
				}
			} else if (e instanceof TextNode && hadithHeader != null) {
				TextNode tn = (TextNode)e;
				String p = tn.text().trim();

				if ( p.startsWith(hadithHeader) )
				{
					String[] tokens = p.split("\\s+(?![^\\[]*\\])");
					
					if (tokens.length >= 4)
					{
						StringBuilder sb = new StringBuilder();
						
						for (int i = 3; i < tokens.length; i++) {
							sb.append(tokens[i]).append(" ");
						}
						
						String hadithNum = tokens[0];
						String body = sb.toString();
						
						if ( hadithNum.matches("\\d+$") && !m_narrations.containsKey(hadithNum) && !p.matches("\\d+\\s+-\\s+\\d+$") )
						{
							Narration n = new Narration();
							n.hadithNumber = hadithNum;
							n.inBookNumber = Integer.parseInt( (String)json.get("pid") );

							if ( !n.hadithNumber.isEmpty() ) {
								n.hadithNumber = hadithHeader.trim();
							}

							n.id = Integer.parseInt(n.hadithNumber);
							n.book = new Book( Integer.parseInt( (String)json.get("part") ), null );
							n.text = body;
							m_current = n;
							
							m_narrations.put(n.hadithNumber, n);
						}
					}
				}
			}
		}

		return m_current;
	}
}