package com.canadainc.sunnah10.processors.shamela;

import java.util.List;

import org.json.simple.JSONObject;
import org.jsoup.nodes.Node;

import com.canadainc.sunnah10.Chapter;
import com.canadainc.sunnah10.Narration;

public class KhalqIbaadProcessor extends AbstractShamelaProcessor
{
	private static final String CHAPTER_HEADING = "بَابُ";
	private Chapter m_chapter;

	@Override
	public void process(List<Node> nodes, JSONObject json)
	{
		Narration n = null;
		boolean isChapter = false;

		for (int i = 0; i < nodes.size(); i++)
		{
			Node e = nodes.get(i);
			
			if ( ShamelaUtils.isTextNode(e) )
			{
				String body = e.toString().trim();

				if ( body.startsWith(CHAPTER_HEADING) ) {
					m_chapter = new Chapter(body, m_chapter == null ? 1 : m_chapter.number+1);
					isChapter = true;
				} else if (isChapter) {
					m_chapter.title += " "+body;
				} else {
					if (n == null) {
						n = new Narration( m_narrations.isEmpty() ? 1 : getPrev().id+1 );
						n.chapter = m_chapter;
					}

					n.text += body;
				}
			} else if ( ShamelaUtils.isTitleSpan(e) ) {
				String body = ShamelaUtils.extractText(e).trim();

				if ( body.startsWith(CHAPTER_HEADING) || body.startsWith("قِرَاءَةُ") ) {
					m_chapter = new Chapter(body, m_chapter == null ? 1 : m_chapter.number+1);
					isChapter = true;
				} else if (isChapter) {
					m_chapter.title += " "+body;
				} else if (n != null) {
					n.text += body;
				}
			}
		}

		ShamelaUtils.appendIfValid(n, m_narrations);
	}
}