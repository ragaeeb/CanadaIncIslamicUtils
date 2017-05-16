package com.canadainc.sunnah10.processors.shamela;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import com.canadainc.common.io.IOUtils;
import com.canadainc.sunnah10.Book;
import com.canadainc.sunnah10.Chapter;
import com.canadainc.sunnah10.Narration;
import com.canadainc.sunnah10.Tahqeeq;

public class ShamelaSunanReader
{
	private Tahqeeq m_signatures;
	private List<Narration> m_narrations;
	private Map<String,List<Narration>> m_bookToNarrations;

	/**
	 * 
	 * @param collectionSig The signature of the collection name
	 * @param gradingSig The signature for the tahqeeq
	 */
	public ShamelaSunanReader(Tahqeeq signatures)
	{
		m_signatures = signatures;
		m_narrations = new ArrayList<>();
		m_bookToNarrations = new HashMap<>();
	}


	public Narration readNarration(File f) throws IOException
	{
		JSONObject json = (JSONObject)JSONValue.parse( IOUtils.readFileUtf8(f) );
		Document d = Jsoup.parse( (String)json.get("content") );

		List<Node> nodes = d.body().childNodes();
		Narration result = null;

		for (int i = 0; i < nodes.size(); i++)
		{
			Node n = nodes.get(i);

			if (n instanceof TextNode)
			{
				TextNode tn = (TextNode)n;
				String body = tn.text();

				if ( body.equals(m_signatures.collectionSignature) )
				{
					i += 2;
					n = nodes.get(i); // following is a <br> tag, and the one after that is the hadeeth with the number

					if (n instanceof TextNode)
					{
						tn = (TextNode)n;
						body = tn.text();

						result = new Narration();
						result.hadithNumber = body.split(" ")[0].trim();
					}
				} else if ( body.equals(m_signatures.gradeFootnoteSignature) ) {
					i += 2;

					if ( i < nodes.size() )
					{
						n = nodes.get(i); // following is a <br> tag, and the one after that is the hadeeth with the number
						tn = (TextNode)n;
						body = tn.text();
						result.grading = body;
					}
				}
			} else if ( n.nodeName().equals("span") && n.attr("class").equals("red") && n.childNode(0) instanceof TextNode ) {
				TextNode tn = (TextNode)n.childNode(0);
				String hadithNum = tn.text().split("\\s+")[0].trim();

				if (hadithNum.matches("\\d+$")) {
					result = new Narration();
					result.hadithNumber = hadithNum.split(" ")[0].trim();
				} 
			} else if ( n.nodeName().equals("span") && n.attr("class").equals("title") ) {
				if ( n.childNode(0).nodeName().equals("span") && n.attr("class").equals("title") ) {
					n = n.childNode(0);
				}

				if ( n.childNode(0) instanceof TextNode )
				{
					TextNode tn = (TextNode)n.childNode(0);
					String hadithNum = tn.text().replaceAll("[\\[\\]]+", "");

					result = new Narration();
					result.hadithNumber = hadithNum;
				} 
			} else if ( n.nodeName().equals("span") && n.attr("class").equals("footnote") ) {
				Node footer = n.childNode(0);

				if ( footer.childNodeSize() > 0 )
				{
					TextNode title = (TextNode)footer.childNode(0);

					if ( n.childNodeSize() > 1 && n.childNode(0).attr("class").equals("title") && title.text().equals(m_signatures.gradeFootnoteSignature) )
					{
						TextNode tn = (TextNode)n.childNode(1);
						result.grading = tn.text().trim();
					}
				} else {
					TextNode title = (TextNode)footer;

					if ( title.text().equals(m_signatures.gradeFootnoteSignature) )
					{
						if (result == null) {
							System.err.println("NULLRESULT");
						}

						title = (TextNode)n.childNode(2);
						result.grading = title.text().trim();
					}
				}
			}
		}

		return result;
	}
	
	private int m_lastHadith;
	public void readBulughMaram(File f) throws IOException
	{
		JSONObject json = (JSONObject)JSONValue.parse( IOUtils.readFileUtf8(f) );
		Document d = Jsoup.parse( (String)json.get("content") );
		
		List<Node> nodes = d.body().childNodes();
		Narration result = new Narration();
		
		for (int i = 0; i < nodes.size(); i++)
		{
			Node n = nodes.get(i);
			
			if ( n.attr("class").equals("title") )
			{
				TextNode tn = (TextNode)n.childNode(0);
				String title = tn.text();
				
				if ( title.startsWith("بَابُ") ) {
					result.chapter = new Chapter( title.trim(), 0 );
				} else if ( title.startsWith("كِتَابُ") ) {
					result.book = new Book( 0, title.trim() );
				}
			} else if ( n.attr("class").equals("red") ) {
				TextNode tn = (TextNode)n.childNode(0);
				String hadithIndex = tn.text().split(" ")[0];
				
				if ( hadithIndex.matches("\\d+$") )
				{
					result.hadithNumber = tn.text().split(" ")[0];
					int hadithNum = Integer.parseInt(result.hadithNumber);
					
					if ( (hadithNum != m_lastHadith+1) && m_lastHadith != 0) {
						System.out.println("***"+f.getName()+"; lastHadithWas="+m_lastHadith+" & currentHadith="+hadithNum);
					}
					m_lastHadith = hadithNum;
				}
			} else if (n instanceof TextNode) {
				TextNode tn = (TextNode)n;
				result.text += tn.text();
			} else if ( n.nodeName().equals("span") && n.attr("class").equals("footnote") ) {
				TextNode tn = (TextNode)n.childNode(0);
				result.grading = tn.text().trim();
			} 
		}
		
		if (result.text == null || result.hadithNumber == null) {
			System.out.println("NULLS***"+f.getName());
		}
		
		if ( !result.text.isEmpty() && !result.hadithNumber.isEmpty() ) {
			m_narrations.add(result);
		}
		
		Book lastBook = null;
		Chapter lastChapter = null;

		for (Narration n: m_narrations)
		{
			if (n.book.name != null) {
				lastBook = n.book;
			} else if (lastBook != null) {
				n.book = lastBook;
			}
			
			List<Narration> narrationsInBook = m_bookToNarrations.get(n.book.name);
			
			if (narrationsInBook == null) {
				narrationsInBook = new ArrayList<>();
				m_bookToNarrations.put(n.book.name, narrationsInBook);
			}
			
			narrationsInBook.add(n);
			
			if (n.chapter != null) {
				lastChapter = n.chapter;
			} else if (lastBook != null) {
				n.chapter = lastChapter;
			}
		}
		/*
		System.out.println("****"+m_bookToNarrations.size());
		
		for (String b: m_bookToNarrations.keySet())
		{
			System.out.println( b+"; "+m_bookToNarrations.get(b).size() );
		} */
	}


	public List<Narration> getNarrations() {
		return m_narrations;
	}
}