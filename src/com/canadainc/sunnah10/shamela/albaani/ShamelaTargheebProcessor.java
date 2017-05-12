package com.canadainc.sunnah10.shamela.albaani;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;
import org.jsoup.nodes.Node;

import com.canadainc.common.text.TextUtils;
import com.canadainc.sunnah10.Book;
import com.canadainc.sunnah10.Chapter;
import com.canadainc.sunnah10.Narration;
import com.canadainc.sunnah10.shamela.ShamelaProcessor;
import com.canadainc.sunnah10.shamela.ShamelaUtils;
import com.canadainc.sunnah10.shamela.TypoProcessor;

public class ShamelaTargheebProcessor implements ShamelaProcessor
{
	private static final String BOOK_PREFIX = "كتاب";
	private ArrayList<Narration> m_narrations = new ArrayList<>();
	private TypoProcessor m_typos = new TypoProcessor();
	private Book m_book;
	private Chapter m_chapter;

	public ShamelaTargheebProcessor()
	{
		m_typos.add(355,"574","547");
		m_typos.add(1151,"1286","2186");
		m_typos.add(1503,"9022","3022");
		m_typos.add(1643,"3231","3331");
	}

	@Override
	public void process(List<Node> nodes, JSONObject json)
	{
		Narration n = null;
		int nodeSize = nodes.size();

		for (int i = 0; i < nodeSize; i++)
		{
			Node e = nodes.get(i);

			if ( ShamelaUtils.isTitleSpan(e) )
			{
				String title = ShamelaUtils.extractText(e);

				if ( ( title.startsWith("[") && title.endsWith("]") && title.contains(BOOK_PREFIX) ) )
				{
					title = TextUtils.removeQuotes(title);
					String[] tokens = title.split("-");

					if (tokens.length > 1)
					{
						m_book = new Book();
						m_book.name = tokens[1].trim();
						m_book.id = Integer.parseInt( tokens[0].trim() );

						++i; // the next node which is a text in 1 in brackets can be safely discarded because it doesn't seem to mean anything
					}
				}
			} else if ( ShamelaUtils.isHadithNumberNode(e) ) {
				Node next = e.nextSibling();
				String content = ShamelaUtils.extractText(next);
				int number = ShamelaUtils.parseHadithNumber(e);
				boolean isHeading = processHeading(i, content, number);

				if ( ShamelaUtils.isTitleSpan(next) && isHeading ) { // chapter
					++i;
				} else if ( ShamelaUtils.isTextNode(next) && !isHeading && isHadithNumberValid(number) ) {					
					n = ShamelaUtils.createNewNarration(n, e, m_narrations);

					n.inBookNumber = Integer.parseInt( TextUtils.extractInside(content, "(", ")") );
					n.book = m_book;
					n.chapter = m_chapter;
					n.text = content.substring( content.indexOf("]")+2 ); // to account for the space after
					
					if ( content.contains("[") && content.contains("]") ) {
						n.grading = TextUtils.extractInside(content, "[", "]");
					}

					++i;
				} else {
					processBody(n,e);
				}
			} else if ( ShamelaUtils.isTextNode(e) || ShamelaUtils.isLineBreak(e) ) {
				processBody(n,e);
			}
		}

		ShamelaUtils.appendIfValid(n, m_narrations);
	}

	private void processBody(Narration n, Node e)
	{
		String commentary = ShamelaUtils.extractText(e);

		if (n != null) {
			n.text += commentary;
		} else if ( !m_narrations.isEmpty() ) {
			m_narrations.get( m_narrations.size()-1 ).text += commentary;
		}
	}
	
	
	private boolean isHadithNumberValid(int current) {
		return m_narrations.isEmpty() || ( m_narrations.get( m_narrations.size()-1 ).id <= current );
	}
	

	private boolean processHeading(int i, String content, int number)
	{
		if ( content.startsWith("(الترغيب في") || content.startsWith("الترغيب في") || content.startsWith("فصل في") || content.startsWith("الترهيب من") || content.startsWith("(الترهيب من") )
		{
			content = content.replaceAll("[\\.\\(\\)]+", "").trim();

			m_chapter = new Chapter(content, number);
			return true;
		} else if ( content.startsWith(BOOK_PREFIX) ) {
			m_book = new Book();
			m_book.name = content;
			m_book.id = number;
			return true;
		}

		return false;
	}


	@Override
	public boolean preprocess(JSONObject json) {
		m_typos.process(json);
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