package com.canadainc.sunnah10.processors.shamela.albaani;

import java.util.List;

import org.json.simple.JSONObject;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import com.canadainc.common.text.TextUtils;
import com.canadainc.sunnah10.Narration;
import com.canadainc.sunnah10.processors.shamela.AbstractShamelaProcessor;
import com.canadainc.sunnah10.processors.shamela.ShamelaUtils;

public class ShamelaJaamiDaifProcessor extends AbstractShamelaProcessor
{
	public ShamelaJaamiDaifProcessor()
	{
		m_typos.add(2458, "6461", "2461");
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
			} else if ( ShamelaUtils.isClassSpanNode(e, "footnote") && (n != null) ) {
				String signature = ShamelaUtils.extractText( e.childNode(0) );

				if ( signature.equals("[حكم الألباني]") ) {
					n.grading = TextUtils.extractInsideBrackets( e.childNode(2).toString().trim() );
				}
			}
		}

		ShamelaUtils.appendIfValid(n, m_narrations);
	}
}
