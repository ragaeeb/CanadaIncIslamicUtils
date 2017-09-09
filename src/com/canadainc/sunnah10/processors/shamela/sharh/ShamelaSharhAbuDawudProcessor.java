package com.canadainc.sunnah10.processors.shamela.sharh;

import java.util.List;

import org.json.simple.JSONObject;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import com.canadainc.sunnah10.Narration;
import com.canadainc.sunnah10.processors.shamela.AbstractShamelaProcessor;
import com.canadainc.sunnah10.processors.shamela.ShamelaUtils;

public class ShamelaSharhAbuDawudProcessor extends AbstractShamelaProcessor
{

	public ShamelaSharhAbuDawudProcessor()
	{
	}

	@Override
	public void process(List<Node> nodes, JSONObject json)
	{
		Narration n = null;

		for (Node e: nodes)
		{
			System.out.println(e);
			if ( ShamelaUtils.isTitleSpan(e) && e.childNodeSize() > 0 && ShamelaUtils.extractText(e).startsWith("شرح حديث:") ) {
				n = new Narration();
				n.id = getNarrations().isEmpty() ? 1 : getPrev().id+1;
			} else if ( ShamelaUtils.isTextNode(e) ) {
				String body = ((TextNode)e).text();

				if (n != null) {
					n.text += body;
				}
			}
		}

		ShamelaUtils.appendIfValid(n, m_narrations);
	}
}