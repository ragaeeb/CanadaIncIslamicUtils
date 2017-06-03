package com.canadainc.sunnah10.processors.shamela.shared;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import com.canadainc.sunnah10.Book;
import com.canadainc.sunnah10.Chapter;
import com.canadainc.sunnah10.Narration;
import com.canadainc.sunnah10.processors.shamela.AbstractShamelaProcessor;
import com.canadainc.sunnah10.processors.shamela.ShamelaUtils;

public class ShamelaTirmidhiVowelledProcessor extends AbstractShamelaProcessor
{
	private Book m_book;
	private Chapter m_chapter;
	private final Map<Integer,Integer> m_hadithNumToIndex = new HashMap<>();

	public ShamelaTirmidhiVowelledProcessor()
	{
		m_typos.prependHadithNumber(812, 458);
		m_typos.prependHadithNumber(551, 312);
		m_typos.add(552, "313", "312");
		m_typos.add(5070, "2978", "2977");
		m_typos.prependHadithNumber(5071, 2978);
	}

	@Override
	public void process(List<Node> nodes, JSONObject json)
	{
		Narration n = null;

		for (Node e: nodes)
		{
			if ( ShamelaUtils.isHadithNumberNode(e) )
			{
				Node next = e.nextSibling();
				int number = ShamelaUtils.parseHadithNumber(e);

				if ( ShamelaUtils.isTitleSpan(next) && ShamelaUtils.extractText(next).startsWith("أَبْوَابُ") ) {
					m_book = new Book(number, ShamelaUtils.extractText(next).trim());
					break;
				} else {
					appendWithIndex(n);

					n = ShamelaUtils.createNewNarration(n, number, m_narrations);
					n.book = m_book;
					n.chapter = m_chapter;
				}
			} else if ( ShamelaUtils.isTextNode(e) ) {
				String body = ((TextNode)e).text();

				if (n != null) {
					n.text += body;
				}
			} else if ( ShamelaUtils.isTitleSpan(e) ) { // chapter heading
				String chapter = ShamelaUtils.extractText(e).trim();
				m_chapter = new Chapter(chapter, m_chapter == null ? 1 : m_chapter.number+1);
			} else if ( ShamelaUtils.isClassSpanNode(e, "footnote") ) { // grading
				Node node = e.childNode(0);
				
				if ( ShamelaUtils.extractText(node).trim().equals("[حكم الألباني] :") ) {
					n.grading = ((TextNode)node.nextSibling()).text().trim();
				}
			}
		}

		appendWithIndex(n);
	}
	
	
	private void appendWithIndex(Narration n)
	{
		if ( n != null && !n.text.isEmpty() && ShamelaUtils.isArabicText(n.text) )
		{
			Integer index = m_hadithNumToIndex.get(n.id);

			if (index == null)
			{
				m_hadithNumToIndex.put(n.id, m_narrations.size());
				m_narrations.add(n);
			} else {
				Narration prev = m_narrations.get(index);
				prev.text += " "+n.text;
			}
		}
	}
}