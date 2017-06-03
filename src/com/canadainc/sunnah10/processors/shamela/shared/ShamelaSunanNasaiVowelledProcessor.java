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

public class ShamelaSunanNasaiVowelledProcessor extends AbstractShamelaProcessor
{
	private Book m_book;
	private Chapter m_chapter;
	private final Map<Integer,Integer> m_hadithNumToIndex = new HashMap<>();

	@Override
	public void process(List<Node> nodes, JSONObject json)
	{
		Narration inner = null;
		Narration n = null;

		for (Node e: nodes)
		{
			if ( ShamelaUtils.isHadithNumberNode(e) )
			{
				Node next = e.nextSibling();
				int number = ShamelaUtils.parseHadithNumber(e);

				if ( ShamelaUtils.isTitleSpan(next) && ShamelaUtils.extractText(next).startsWith("كِتَابُ") ) {
					m_book = new Book(number, ShamelaUtils.extractText(next));
					break;
				} else {
					appendWithIndex(n);

					n = ShamelaUtils.createNewNarration(n, number, m_narrations);
					n.book = m_book;
					n.chapter = m_chapter;
				}
			} else if ( ShamelaUtils.isTextNode(e) && (n != null) ) {
				String body = ((TextNode)e).text();
				n.text += body;
			} else if ( ShamelaUtils.isTitleSpan(e) ) { // chapter heading
				String chapter = ShamelaUtils.extractText(e);
				m_chapter = new Chapter(chapter, m_chapter == null ? 1 : m_chapter.number+1);
			} else if ( ShamelaUtils.isClassSpanNode(e, "footnote") ) { // grading
				for (int i = 0; i < e.childNodeSize(); i++)
				{
					Node node = e.childNode(i);

					if ( ShamelaUtils.isTitleSpan(node) && ShamelaUtils.extractText(node).equals("[حكم الألباني]") ) {
						n.grading = ((TextNode)node.nextSibling()).text().trim();
					} else if ( ShamelaUtils.isHadithNumberNode(node) ) {
						inner = ShamelaUtils.createNewNarration(inner, node, m_narrations);
						inner.text += ((TextNode)node.nextSibling()).text();
					} else if ( ShamelaUtils.isTextNode(node) && ShamelaUtils.extractText(node).startsWith("[قال الألباني:") ) {
						inner.grading = ShamelaUtils.extractText(node);
					}
				}
			}
		}

		appendWithIndex(n);
		appendWithIndex(inner);
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