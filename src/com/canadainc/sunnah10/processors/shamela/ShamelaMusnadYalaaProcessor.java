package com.canadainc.sunnah10.processors.shamela;

import java.util.List;

import org.json.simple.JSONObject;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import com.canadainc.sunnah10.Narration;

public class ShamelaMusnadYalaaProcessor extends AbstractShamelaProcessor
{
	public ShamelaMusnadYalaaProcessor()
	{
		m_typos.prependHadithNumber(32,35);
		m_typos.prependHadithNumber(7548,7464);
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
			} else if ( ShamelaUtils.isTextNode(e) ) {
				String body = ((TextNode)e).text();

				if (n != null) {
					n.text += body;
				}
			} else if ( ShamelaUtils.isTitleSpan(e) && (n != null) ) {
				String text = ShamelaUtils.extractText(e);

				if ( text.contains("حكم حسين سليم أسد") ) {
					i += 2;
				} else {
					n.text += text;
				}
			}
		}

		ShamelaUtils.appendIfValid(n, m_narrations);
	}
}