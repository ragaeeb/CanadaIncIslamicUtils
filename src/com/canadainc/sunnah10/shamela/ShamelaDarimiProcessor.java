package com.canadainc.sunnah10.shamela;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import com.canadainc.sunnah10.Narration;

public class ShamelaDarimiProcessor implements ShamelaProcessor
{
	private ArrayList<Narration> m_narrations = new ArrayList<>();
	private static final String GRADE_SIGNATURE = "[تعليق المحقق]";

	public ShamelaDarimiProcessor()
	{
	}


	@Override
	public void process(List<Node> nodes, JSONObject json)
	{
		Narration n = null;

		for (int i = 0; i < nodes.size(); i++)
		{
			Node e = nodes.get(i);
			
			if ( ShamelaUtils.isHadithNumberNode(e) && ShamelaUtils.isHadithNumberValid(e, m_narrations) )
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
		
		if (n != null) {
			m_narrations.add(n);
		}
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
		return true;
	}
}