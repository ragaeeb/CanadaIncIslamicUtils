package com.canadainc.sunnah10.shamela;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import com.canadainc.sunnah10.Narration;

public class ShamelaDawudZuhdProcessor implements ShamelaProcessor
{
	private ArrayList<Narration> m_narrations;

	public ShamelaDawudZuhdProcessor()
	{
		m_narrations = new ArrayList<>();
	}

	@Override
	public void process(List<Node> nodes, JSONObject json)
	{
		Narration n = null;

		for (Node e: nodes)
		{
			if ( ShamelaUtils.isHadithNumberNode(e) )
			{
				if (n != null) {
					m_narrations.add(n);
				}

				n = new Narration();
				n.id = ShamelaUtils.parseHadithNumber(e);
			} else if ( ShamelaUtils.isTextNode(e) ) {
				String body = ((TextNode)e).text();

				if (n != null) {
					n.text += body;
				}
			} else if ( ShamelaUtils.isTitleSpan(e) && (n != null) ) {
				n.text += ShamelaUtils.extractText(e);
			}
		}

		if (n != null) {
			m_narrations.add(n);
		}
	}

	@Override
	public List<Narration> getNarrations() {
		return m_narrations;
	}

	@Override
	public void preprocess(JSONObject json)
	{
	}
}