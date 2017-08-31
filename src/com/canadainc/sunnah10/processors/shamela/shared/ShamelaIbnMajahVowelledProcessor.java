package com.canadainc.sunnah10.processors.shamela.shared;

import java.util.List;

import org.json.simple.JSONObject;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import com.canadainc.sunnah10.Book;
import com.canadainc.sunnah10.Chapter;
import com.canadainc.sunnah10.Narration;
import com.canadainc.sunnah10.processors.shamela.AbstractShamelaProcessor;
import com.canadainc.sunnah10.processors.shamela.ShamelaUtils;

public class ShamelaIbnMajahVowelledProcessor extends AbstractShamelaProcessor
{
	private Book m_book;
	private Chapter m_chapter;

	public ShamelaIbnMajahVowelledProcessor() {
		m_typos.add(3677, "2708", "2696");
	}

	@Override
	public void process(List<Node> nodes, JSONObject json)
	{
		Narration n = null;

		for (Node e: nodes)
		{
			if ( ShamelaUtils.isHadithNumberNode(e) )
			{
				int number = ShamelaUtils.parseHadithNumber(e);

				if (!m_narrations.isEmpty() && ( number == getPrev().id ) ) {
					n = getPrev();
				} else if (n != null && n.id == number) {
					// part of the same narration, keep going
				} else {
					n = ShamelaUtils.createNewNarration(n, number, m_narrations);
					n.book = m_book;
					n.chapter = m_chapter;
				}
			} else if ( ShamelaUtils.isTextNode(e) ) {
				String body = ((TextNode)e).text();

				if (n != null) {
					n.text += body;
				} else if ( !m_narrations.isEmpty() ) {
					getPrev().text += " "+body;
				}
			} else if ( ShamelaUtils.isTitleSpan(e) ) {
				String chapter = ShamelaUtils.extractText(e);

				if ( chapter.startsWith("بَابُ") ) {
					m_chapter = new Chapter(chapter, m_chapter == null ? 1 : m_chapter.number+1);
					
					//TODO: page 15, page 685, 729
				} else {
					m_book = new Book(m_book == null ? 0 : m_book.id+1, chapter);
					m_chapter = null; // since chapters reset to 1 every book
				}
			} else if ( ShamelaUtils.isClassSpanNode(e, "footnote") ) { // grading
				for (int i = 0; i < e.childNodeSize(); i++)
				{
					Node node = e.childNode(i);

					if ( ShamelaUtils.isTitleSpan(node) && ShamelaUtils.extractText(node).equals("[حكم الألباني]") ) {
						node = node.nextSibling(); // skip <br>
						String grade = ((TextNode)node.nextSibling()).text().trim();

						if (n != null) {
							n.grading = grade;
						} else if ( !m_narrations.isEmpty() ) {
							getPrev().grading += " "+grade;
						}
					}
				}
			}
		}

		if ( m_narrations.isEmpty() || ( n != getPrev() ) ) {
			ShamelaUtils.appendIfValid(n, m_narrations);
		}
	}
}