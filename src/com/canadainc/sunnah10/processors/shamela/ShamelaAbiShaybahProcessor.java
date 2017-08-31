package com.canadainc.sunnah10.processors.shamela;

import java.util.List;

import org.json.simple.JSONObject;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import com.canadainc.sunnah10.Book;
import com.canadainc.sunnah10.Narration;

public class ShamelaAbiShaybahProcessor extends AbstractShamelaProcessor
{
	private Book m_book;

	public ShamelaAbiShaybahProcessor()
	{
		m_typos.add(1970, "1783 -<br /><br />", "");
		m_typos.prependHadithNumber(4377, 3962);
		m_typos.add(5945, "5352 -<br /><br />", "");
		m_typos.add(12746, "11432 -<br /><br />", "");
		m_typos.add(12953, "11623 -<br /><br />", "");
		m_typos.add(13008, "11671 -<br /><br />", "");
		m_typos.add(13008, "11672", "11671");
		m_typos.prependHadithNumber(13009, 11672);
		m_typos.prependHadithNumber(13010, 11673);
		m_typos.prependHadithNumber(15462, 13827);
		m_typos.prependHadithNumber(18363, 16348);
		m_typos.prependHadithNumber(18364, 16349);
		m_typos.prependHadithNumber(19231, 17140);
		m_typos.prependHadithNumber(38327, 33772);
		m_typos.prependHadithNumber(38328, 33773);
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

				if ( ShamelaUtils.isTitleSpan(next) && ShamelaUtils.extractText(next).startsWith("كِتَابُ") ) {
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