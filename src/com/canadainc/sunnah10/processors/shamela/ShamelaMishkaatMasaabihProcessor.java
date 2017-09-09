package com.canadainc.sunnah10.processors.shamela;

import java.util.List;

import org.json.simple.JSONObject;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import com.canadainc.common.text.TextUtils;
import com.canadainc.sunnah10.Narration;

public class ShamelaMishkaatMasaabihProcessor extends AbstractShamelaProcessor
{
	public ShamelaMishkaatMasaabihProcessor()
	{
		m_typos.ignore(7398,7399);
	}

	@Override
	public void process(List<Node> nodes, JSONObject json)
	{
		Narration n = null;

		for (int i = 0; i < nodes.size(); i++)
		{
			Node e = nodes.get(i);

			if ( ShamelaUtils.isHadithNumberNode(e) ) {
				n = ShamelaUtils.createNewNarration(n, e, m_narrations);

				String grading = e.nextSibling().toString();

				if ( grading.contains("(") && grading.contains(")") ) {
					n.grading = TextUtils.extractInside(grading, "(", ")");
					++i;
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
