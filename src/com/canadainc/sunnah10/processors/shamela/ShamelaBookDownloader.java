package com.canadainc.sunnah10.processors.shamela;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;
import java.util.TreeMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import com.canadainc.islamicutils.io.NetworkBoundary;

public class ShamelaBookDownloader
{
	private int m_id;
	private Map<Integer,String> m_pageToEntry;

	public ShamelaBookDownloader(int id)
	{
		m_id = id;
		m_pageToEntry = new TreeMap<>();
	}


	public void download() {
		downloadBook(new String(), 0);
	}


	private void downloadBook(String prefix, int folderId)
	{
		String result = NetworkBoundary.getHTML("http://shamela.ws/browse.php/book/lazytree/"+m_id+"/"+folderId+"/echo");

		Document d = Jsoup.parse(result);
		Elements elements = d.select("li[class=f]");

		for (Element folder: elements)
		{
			int subFolderId = getReferenceId( folder.childNode(0) );

			Node ahref = folder.childNode(1);
			int folderPageNumber = getReferenceId(ahref);
			String book = ((TextNode)ahref.childNode(0)).text().trim();

			String current = prefix+book;
			m_pageToEntry.put(folderPageNumber, current);

			downloadBook(current+"/", subFolderId);
		}

		elements = d.select("li[class=p]");

		for (Element chapter: elements)
		{
			Node ahref = chapter.childNode(1);
			int chapterPageNumber = getReferenceId(ahref);
			String chapterName = ((TextNode)ahref.childNode(0)).text().trim();

			m_pageToEntry.put(chapterPageNumber, prefix+chapterName);
		}
	}


	private int getReferenceId(Node n)
	{
		String folderInfo = n.attr("onclick");
		folderInfo = folderInfo.substring( folderInfo.indexOf("(")+1, folderInfo.indexOf(",") );

		return Integer.parseInt(folderInfo);
	}


	/**
	 * @return the pageToEntry
	 */
	public Map<Integer, String> getPageToEntry() {
		return m_pageToEntry;
	}


	void setPageToEntries(Map<Integer, String> map) {
		m_pageToEntry = map;
	}


	public void writeToDB(Connection connection, String table) throws SQLException, IOException
	{
		connection.setAutoCommit(false);

		JSONArray pages = new JSONArray();

		for (int page: m_pageToEntry.keySet())
		{
			JSONObject j = new JSONObject();
			j.put("pid", page);
			j.put("path", m_pageToEntry.get(page));

			pages.add(j);
		}

		JSONObject j = new JSONObject();
		j.put("data", pages);

		PreparedStatement ps = connection.prepareStatement("INSERT INTO "+table+"(file_name,json) VALUES (?,?)");
		ps.setString(1, "index.txt");
		ps.setString(2, j.toJSONString());
		ps.execute();

		connection.commit();
		ps.close();
	}
}