package com.canadainc.sunnah10.processors.shamela.shared;

import java.util.List;

import org.json.simple.JSONObject;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import com.canadainc.sunnah10.Narration;
import com.canadainc.sunnah10.processors.shamela.AbstractShamelaProcessor;
import com.canadainc.sunnah10.processors.shamela.ShamelaUtils;

public class ShamelaAbuDawudNoVowelsProcessor extends AbstractShamelaProcessor
{
	public ShamelaAbuDawudNoVowelsProcessor()
	{
		m_typos.ignore(238,316);
		
		m_typos.add(1602, "1601");
		m_typos.add(2132, "2131");
		m_typos.add(3217, "3216");
		m_typos.add(4317, "4316");
		m_typos.add(4318, "4317");
	}


	/* (non-Javadoc)
	 * @see com.canadainc.sunnah10.processors.shamela.AbstractShamelaProcessor#preprocess(int, java.lang.String)
	 */
	@Override
	protected String preprocess(int page, String content)
	{
		content = content.replaceAll("<span class=\"title\"><span class=\"title\">", "<span class=\"title\">");
		content = content.replaceAll("</span></span>", "</span>");

		return super.preprocess(page, content);
	}


	@Override
	public void process(List<Node> nodes, JSONObject json)
	{
		Narration n = null;

		for (Node e: nodes)
		{
			if ( ShamelaUtils.isTitleSpan(e) && e.childNodeSize() > 0 )
			{
				String value = ShamelaUtils.extractText( e.childNode(0) );

				if ( value.matches("\\[\\d{1,4}\\]$") )
				{
					value = value.replaceAll("[\\[\\]]+", "");

					n = ShamelaUtils.createNewNarration(n, Integer.parseInt(value), m_narrations);
					n.text = ShamelaUtils.extractText( e.nextSibling() );
				}
			} else if ( ShamelaUtils.isFootnote(e) ) {
				TextNode tn = (TextNode)e.childNode(2);
				n.grading = tn.text();
			}
		}

		ShamelaUtils.appendIfValid(n, m_narrations);
	}
}