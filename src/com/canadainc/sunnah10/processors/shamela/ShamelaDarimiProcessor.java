package com.canadainc.sunnah10.processors.shamela;

import java.util.List;

import org.json.simple.JSONObject;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import com.canadainc.sunnah10.Narration;

public class ShamelaDarimiProcessor extends AbstractShamelaProcessor
{
	private static final String GRADE_SIGNATURE = "[تعليق المحقق]";


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

				if ( signature.equals(GRADE_SIGNATURE) )
				{
					++i;

					if ( i < nodes.size() )
					{
						e = nodes.get(i);

						if ( ShamelaUtils.isTextNode(e) ) {
							n.grading = ((TextNode)e).text().trim();
						}
					}
				}
			}
		}

		ShamelaUtils.appendIfValid(n, m_narrations);
	}


	@Override
	public boolean hasGrade(int id) {
		return true;
	}
}