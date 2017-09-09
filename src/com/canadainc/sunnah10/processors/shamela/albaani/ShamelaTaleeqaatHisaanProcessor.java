package com.canadainc.sunnah10.processors.shamela.albaani;

import java.util.List;

import org.json.simple.JSONObject;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import com.canadainc.sunnah10.Narration;
import com.canadainc.sunnah10.processors.shamela.AbstractShamelaProcessor;
import com.canadainc.sunnah10.processors.shamela.ShamelaTypoProcessor;
import com.canadainc.sunnah10.processors.shamela.ShamelaUtils;

public class ShamelaTaleeqaatHisaanProcessor extends AbstractShamelaProcessor
{
	private static final String GRADE_SIGNATURE = "[تعليق الشيخ الألباني]";

	public ShamelaTaleeqaatHisaanProcessor()
	{
		m_typos.ignore(1107,4311,5988,7604,9187,10748,12386,13801);
		m_typos.highlight(2036, 948);
		m_typos.highlight(4155, 2021);
		m_typos.highlight(4240, 2063);
		m_typos.highlight(4611, 2255);
		m_typos.highlight(4613, 2256);

		int i = 2282;
		m_typos.highlight(4664, ++i);
		m_typos.highlight(4666, ++i);
		m_typos.highlight(4668, ++i);
		m_typos.highlight(4669, ++i);
		m_typos.highlight(4671, ++i);
		m_typos.highlight(4673, ++i);
		m_typos.highlight(4675, ++i);
		m_typos.highlight(4677, ++i);

		m_typos.highlight(4681, 2292);
		m_typos.highlight(6015, 2256);

		i = 2955;
		m_typos.highlight(6015, ++i);
		m_typos.highlight(6017, ++i);
		m_typos.highlight(6019, ++i);
		m_typos.highlight(6021, ++i);

		m_typos.highlight(6064, 2980);
		m_typos.highlight(6067, 2981);

		m_typos.highlight(6101, 2997);
		m_typos.highlight(6285, 3090);
		m_typos.highlight(6368, 3132);
		m_typos.highlight(6370, 3133);
		m_typos.highlight(6412, 3155);
		m_typos.highlight(6532, 3212);
		m_typos.highlight(6534, 3213);
		m_typos.highlight(6730, 3309);
		m_typos.highlight(6845, 3366);
		m_typos.highlight(6846, 3367);
		m_typos.highlight(6908, 3399);
		m_typos.highlight(7361, 3625);
		m_typos.add(8713, "[4295/م]", ShamelaTypoProcessor.decorateContent("4295"));
		m_typos.highlight(8714, 4296);
		m_typos.highlight(8717, 4298);
		m_typos.highlight(8721, 4300);
		m_typos.add(8713, "[4299/ م]", ShamelaTypoProcessor.decorateContent("4299"));
		m_typos.highlight(10582, 5203);
	}


	/* (non-Javadoc)
	 * @see com.canadainc.sunnah10.processors.shamela.AbstractShamelaProcessor#hasGrade(int)
	 */
	@Override
	public boolean hasGrade(int id) {
		return true;
	}



	@Override
	public void process(List<Node> nodes, JSONObject json)
	{
		Narration n = null;

		for (int i = 0; i < nodes.size(); i++)
		{
			Node e = nodes.get(i);

			if ( ShamelaUtils.isHadithNumberNode(e) && !ShamelaUtils.isTitleSpan( e.nextSibling() ) ) {
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
					boolean foundGrade = false;
					
					while ( i < nodes.size()-1 && !foundGrade )
					{
						++i;
						e = nodes.get(i);

						if ( ShamelaUtils.isTextNode(e) && !ShamelaUtils.extractText(e).trim().isEmpty() ) {
							n.grading = ((TextNode)e).text().trim();
							foundGrade = true;
						}
					}
				}
			}
		}

		ShamelaUtils.appendIfValid(n, m_narrations);
	}
}
