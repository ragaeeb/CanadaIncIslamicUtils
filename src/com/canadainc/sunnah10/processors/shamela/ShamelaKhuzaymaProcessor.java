package com.canadainc.sunnah10.processors.shamela;

import java.util.List;

import org.json.simple.JSONObject;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import com.canadainc.sunnah10.Narration;

public class ShamelaKhuzaymaProcessor extends AbstractShamelaProcessor
{
	public ShamelaKhuzaymaProcessor()
	{
		m_typos.add(1703, "1068 -<br /><br />", "");
		m_typos.prependHadithNumber(3745,2264);
		m_typos.prependHadithNumber(4136,2497);
		m_typos.add(2770, "1693 قال الأعظمي: إسناده صحيح", "");
	}

	@Override
	public void process(List<Node> nodes, JSONObject json)
	{
		Narration n = null;

		for (int i = 0; i < nodes.size(); i++)
		{
			Node e = nodes.get(i);

			if ( ShamelaUtils.isHadithNumberNode(e) && ShamelaUtils.isHadithNumberValid(e, m_narrations, n) ) {
				n = ShamelaUtils.createNewNarration(n, e, m_narrations);
			} else if ( ShamelaUtils.isTextNode(e) ) {
				String body = ((TextNode)e).text();

				if (n != null) {
					n.text += body;
				}
			} else if ( ShamelaUtils.isTitleSpan(e) && (n != null) ) {
				String signature = ShamelaUtils.extractText(e);

				if ( signature.equals("[التعليق]") ) {
					i += 2;
				}
			}
		}

		ShamelaUtils.appendIfValid(n, m_narrations);
	}
}