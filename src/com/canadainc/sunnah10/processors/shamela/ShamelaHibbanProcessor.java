package com.canadainc.sunnah10.processors.shamela;

import java.util.List;

import org.json.simple.JSONObject;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import com.canadainc.sunnah10.Narration;

public class ShamelaHibbanProcessor extends AbstractShamelaProcessor
{
	private static final String GRADE_SIGNATURE = "[تعليق الألباني]";
	
	public ShamelaHibbanProcessor()
	{
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
			} else if ( ShamelaUtils.isClassSpanNode(e, "footnote") && (n != null) ) {
				String signature = ShamelaUtils.extractText( e.childNode(0) );

				if ( signature.equals(GRADE_SIGNATURE) && !signature.equals("[تنبيه!! ]") ) {
					n.grading = e.childNode(3).toString();
				}
			} else if ( ShamelaUtils.isTitleSpan(e) && ShamelaUtils.extractText(e).startsWith("رقم طبعة با وزير") ) {
				//++i;
				//n.inBookNumber = Integer.parseInt( TextUtils.extractInsideBrackets( nodes.get(i).toString().trim() ) );
			}
		}

		ShamelaUtils.appendIfValid(n, m_narrations);
	}
}