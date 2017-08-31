package com.canadainc.sunnah10.processors.shamela;

import java.util.List;

import org.json.simple.JSONObject;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import com.canadainc.sunnah10.Book;
import com.canadainc.sunnah10.Narration;

public class ShamelaNasaiKubraProcessor extends AbstractShamelaProcessor
{
	private Book m_book;

	public ShamelaNasaiKubraProcessor()
	{
		m_typos.add(3089, "2030/ 1", "2030");
		m_typos.add(3089, "2030/ 2", "2030");
		m_typos.prependHadithNumber(6826, 4851);
		m_typos.prependHadithNumber(7367, 5296);
		m_typos.add(3089, "9775/ 9776", "9775");
	}

	@Override
	public void process(List<Node> nodes, JSONObject json)
	{
		Narration n = null;

		for (Node e: nodes)
		{
			if ( ShamelaUtils.isHadithNumberNode(e) ) {
				Node next = e.nextSibling();
				int number = ShamelaUtils.parseHadithNumber(e);

				if ( ShamelaUtils.isTitleSpan(next) && ( ShamelaUtils.extractText(next).startsWith("كِتَابُ") || ShamelaUtils.extractText(next).startsWith("سُورَةُ") ) ) {
					m_book = new Book(number, ShamelaUtils.extractText(next).trim());
					break;
				} else {
					n = ShamelaUtils.createNewNarration(n, number, m_narrations);
					n.book = m_book;
				}

				n = ShamelaUtils.createNewNarration(n, e, m_narrations);
			} else if ( ShamelaUtils.isTextNode(e) ) {
				String body = ((TextNode)e).text();

				if (n != null) {
					n.text += body;
				}
			} else if ( ShamelaUtils.isTitleSpan(e) && (n != null) ) {
				n.text += ShamelaUtils.extractText(e);
			}
		}

		ShamelaUtils.appendIfValid(n, m_narrations);
	}
}