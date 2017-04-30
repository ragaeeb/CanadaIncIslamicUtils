package com.canadainc.sunnah10.shamela;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.canadainc.islamicutils.io.NetworkBoundary;

public class ShamelaBookDownloader
{
	private int m_id;
	
	public ShamelaBookDownloader(int id)
	{
		m_id = id;
	}
	
	
	public void download()
	{
		String result = NetworkBoundary.getHTML("http://shamela.ws/browse.php/book/lazytree/"+m_id+"/0/echo");
		
		Document d = Jsoup.parse(result);
		Elements links = d.select("li[class]");
		
		for (int i = 0; i < links.size(); i++) {
			System.out.println( links.get(i) );
		}
	}
	
	
	private class Binding
	{
		public int pageNumber;
		public String title;
	}
}