package com.canadainc.sunnah10.utils;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

import org.json.simple.JSONObject;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import com.canadainc.common.text.TextUtils;
import com.canadainc.sunnah10.processors.shamela.AbstractShamelaProcessor;
import com.canadainc.sunnah10.processors.shamela.DatabasePopulator;
import com.canadainc.sunnah10.processors.shamela.ShamelaPopulator;
import com.canadainc.sunnah10.processors.shamela.ShamelaUtils;

public class NarrationSearcher
{
	public NarrationSearcher()
	{
	}


	public void prompt(Connection c) throws Exception
	{
		Scanner scanIn = new Scanner(System.in);

		System.out.println("Enter Collection:");
		String key = scanIn.nextLine().trim();
		PageFilter p = new PageFilter();

		String path = null;
		String collection = key;
		int index = key.indexOf("/");
		boolean shamela = index == -1;

		if (!shamela)
		{
			path = key.substring(index+1);
			collection = key.substring(0, index);
		}

		DatabasePopulator sp = new ShamelaPopulator(collection, path, p);
		Map<Integer, String> results = p.getResults();

		while ( results.isEmpty() )
		{
			System.out.println("Enter query:");
			p.setQuery( scanIn.nextLine().trim() );
			sp.process(c);

			if ( results.isEmpty() ) {
				System.out.println("No matches...");
			}
		}

		while ( results.size() != 1 )
		{
			int current = results.size();
			System.out.println(current+" matches...");

			System.out.println("Enter query:");
			String query = scanIn.nextLine().trim();

			if ( query == null || query.isEmpty() ) {
				break;
			}

			p.setQuery(query);
			p.narrowFound();

			if ( current == results.size() ) {
				System.out.println("No match");
			}
		}

		for (Integer page: results.keySet()) {
			System.out.println("Page "+page+"; "+results.get(page));
		}

		scanIn.close();
	}


	private final class PageFilter extends AbstractShamelaProcessor
	{
		private String query;
		private Map<Integer, String> m_results = new TreeMap<>();

		public void setQuery(String query) {
			this.query = TextUtils.normalize(query);
		}


		public Map<Integer, String> getResults() {
			return m_results;
		}


		public void narrowFound()
		{
			ArrayList<Integer> toRemove = new ArrayList<>();

			for (Integer page: m_results.keySet())
			{
				if ( !m_results.get(page).contains(query) ) {
					toRemove.add(page);
				}
			}

			if ( toRemove.size() < m_results.size() )
			{
				for (Integer page: toRemove) {
					m_results.remove(page);
				}
			}
		}


		@Override
		public void process(List<Node> nodes, JSONObject json)
		{

			ArrayList<String> result = new ArrayList<>();

			for (Node e: nodes)
			{
				if ( ShamelaUtils.isTextNode(e) )
				{
					String body = ((TextNode)e).text();

					if ( TextUtils.normalize(body).contains(query) ) {
						result.add(body);
					}
				}
			}

			if ( !result.isEmpty() ) {
				m_results.put( getPageNumber(json), result.toString() );
			}
		}
	}

	private class MatchData
	{
		private String matched = new String();
		private Set<Integer> matchIndices = new HashSet<>();

		public int getMatchStart() {
			Integer[] arr = matchIndices.toArray( new Integer[matchIndices.size()] );
			return arr[0];
		}
	}
}