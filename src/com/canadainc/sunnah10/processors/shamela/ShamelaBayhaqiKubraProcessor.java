package com.canadainc.sunnah10.processors.shamela;

import java.util.List;

import org.json.simple.JSONObject;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import com.canadainc.sunnah10.Narration;

public class ShamelaBayhaqiKubraProcessor extends AbstractShamelaProcessor
{
	public ShamelaBayhaqiKubraProcessor()
	{
		m_typos.prependHadithNumber(7025, 6146);
		m_typos.prependHadithNumber(6657, 5850);
		m_typos.add(18804, "16486", "16468");
		m_typos.prependHadithNumber(20891, 18375);
		m_typos.add(24399, "21552 - </span>وَعَنْ ع", "21553 - </span>وَعَنْ ع");
	}

	@Override
	public void process(List<Node> nodes, JSONObject json)
	{
		Narration n = null;

		for (Node e: nodes)
		{
			if ( ShamelaUtils.isHadithNumberNode(e) ) {
				Node next = e.nextSibling();
				
				if ( !ShamelaUtils.isTitleSpan(next) && !next.toString().startsWith("بَاب") ) {
					n = ShamelaUtils.createNewNarration(n, e, m_narrations);
				}
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