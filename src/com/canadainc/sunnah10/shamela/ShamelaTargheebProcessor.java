package com.canadainc.sunnah10.shamela;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import com.canadainc.common.text.TextUtils;
import com.canadainc.sunnah10.Book;
import com.canadainc.sunnah10.Chapter;
import com.canadainc.sunnah10.Narration;

public class ShamelaTargheebProcessor implements ShamelaProcessor
{
	private static final String BOOK_PREFIX = "كتاب";
	private ArrayList<Narration> m_narrations = new ArrayList<>();
	private TypoProcessor m_typos = new TypoProcessor();
	private Book m_book;
	private Chapter m_chapter;

	public ShamelaTargheebProcessor()
	{
		
	}

	@Override
	public void process(List<Node> nodes, JSONObject json)
	{
		Narration n = null;
		int nodeSize = nodes.size();

		for (int i = 0; i < nodeSize; i++)
		{
			Node e = nodes.get(i);
			System.out.println(e);

			if ( ShamelaUtils.isTitleSpan(e) )
			{
				String title = ShamelaUtils.extractText(e);
				
				if ( ( title.startsWith("[") && title.endsWith("]") && title.contains(BOOK_PREFIX) ) || title.startsWith(BOOK_PREFIX) )
				{
					title = TextUtils.extractInsideBrackets(title);
					m_book = new Book();
					m_book.name = TextUtils.extractInsideBrackets(title);
					
					++i; // the next node which is a text in 1 in brackets can be safely discarded because it doesn't seem to mean anything
				}
			} else if ( ShamelaUtils.isHadithNumberNode(e) )
			{
				if (n != null) {
					m_narrations.add(n);
				}

				n = new Narration();
				n.id = ShamelaUtils.parseHadithNumber(e);
			} else if ( ShamelaUtils.isTextNode(e) ) {
				String commentary = ((TextNode)e).text();
				n.commentary += commentary;
			} else if ( ShamelaUtils.isFootnote(e) ) {
				String commentary = ShamelaUtils.extractText(e);
				n.commentary += commentary;
			}
		}

		if (n != null) {
			m_narrations.add(n);
		}
	}

	@Override
	public boolean preprocess(JSONObject json) {
		return true;
	}

	@Override
	public List<Narration> getNarrations() {
		return m_narrations;
	}

	@Override
	public boolean hasGrade(int id) {
		return true;
	}
}