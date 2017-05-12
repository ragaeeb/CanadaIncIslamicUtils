package com.canadainc.sunnah10.shamela;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import com.canadainc.sunnah10.Narration;

public class ShamelaDawudZuhdProcessor implements ShamelaProcessor
{
	private ArrayList<Narration> m_narrations = new ArrayList<>();

	public ShamelaDawudZuhdProcessor()
	{
	}

	@Override
	public void process(List<Node> nodes, JSONObject json)
	{
		Narration n = null;

		for (Node e: nodes)
		{
			if ( ShamelaUtils.isHadithNumberNode(e) ) {
				n = ShamelaUtils.createNewNarration(n, e, m_narrations);
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

	@Override
	public List<Narration> getNarrations() {
		return m_narrations;
	}

	@Override
	public boolean preprocess(JSONObject json) {
		return true;
	}

	@Override
	public boolean hasGrade(int id)
	{
		return false;
	}
}