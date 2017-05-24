package com.canadainc.sunnah10.processors.shamela;

import java.util.List;

import org.json.simple.JSONObject;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import com.canadainc.sunnah10.Book;
import com.canadainc.sunnah10.Chapter;
import com.canadainc.sunnah10.Narration;

public class ShamelaContinuedProcessor extends AbstractShamelaProcessor
{
	private Book m_book;
	private Chapter m_chapter;

	@Override
	public void process(List<Node> nodes, JSONObject json)
	{
		Narration n = null;

		for (Node e: nodes)
		{
			if ( ShamelaUtils.isHadithNumberNode(e) ) {
				n = ShamelaUtils.createNewNarration(n, e, m_narrations);
				n.book = m_book;
				n.chapter = m_chapter;
			} else if ( ShamelaUtils.isTextNode(e) ) {
				String body = ((TextNode)e).text();
				appendBody(n, body);
			} else if ( ShamelaUtils.isTitleSpan(e) ) {
				String body = ShamelaUtils.extractText(e);
				
				if ( body.startsWith("كِتَابُ") ) {
					m_book = new Book(m_book == null ? 1 : m_book.id+1, body);
				} else if ( nodes.size() == 1 ) {
					m_chapter = new Chapter(body, m_chapter == null ? 1 : m_chapter.number+1);
				} else {
					appendBody(n, body);
				}
			}
		}

		ShamelaUtils.appendIfValid(n, m_narrations);
	}


	private void appendBody(Narration n, String body)
	{
		if (n != null) {
			n.text += body;
		} else if ( !m_narrations.isEmpty() ) {
			m_narrations.get( m_narrations.size()-1 ).text += body;
		}
	}
}