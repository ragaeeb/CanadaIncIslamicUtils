package com.canadainc.sunnah10.shamela;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.simple.JSONObject;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import com.canadainc.sunnah10.Narration;

public class ShamelaMustadrakProcessor implements ShamelaProcessor
{
	private static final String GRADE_SIGNATURE = "[التعليق - من تلخيص الذهبي]";
	private ArrayList<Narration> m_narrations;
	private HashMap<Integer,Integer> m_narrationToIndex;

	public ShamelaMustadrakProcessor()
	{
		m_narrations = new ArrayList<>();
		m_narrationToIndex = new HashMap<>();
	}

	@Override
	public void process(List<Node> nodes, JSONObject json)
	{
		Narration n = null;

		for (int i = 0; i < nodes.size(); i++)
		{
			Node e = nodes.get(i);

			if ( ShamelaUtils.isHadithNumberNode(e) )
			{
				addNarration(n);

				n = new Narration();
				n.id = ShamelaUtils.parseHadithNumber(e);
				addNarration(n);
			} else if ( ShamelaUtils.isTextNode(e) ) {
				String body = ((TextNode)e).text();

				if ( body.trim().matches("\\d+$") && ( i+1 < nodes.size() ) && ShamelaUtils.isTitleSpan( nodes.get(i+1) ) && ShamelaUtils.extractText( nodes.get(i+1) ).startsWith("[ص:") )
				{
					addNarration(n);

					n = new Narration();
					n.id = Integer.parseInt( body.trim() );
					addNarration(n);
				} else if (n != null) {
					n.text += body;
				}
			} else if ( ShamelaUtils.isTitleSpan(e) && (n != null) ) {
				String signature = ShamelaUtils.extractText(e);

				if ( signature.equals(GRADE_SIGNATURE) )
				{
					i += 2;
					
					if ( i < nodes.size() )
					{
						e = nodes.get(i);
						int gradingHadithNumber = ShamelaUtils.parseHadithNumber(e);
						
						if ( m_narrationToIndex.containsKey(gradingHadithNumber) )
						{
							++i;
							e = nodes.get(i);
							
							if ( ShamelaUtils.isTextNode(e) )
							{
								Narration matching = m_narrations.get( m_narrationToIndex.get(gradingHadithNumber) );
								matching.grading = ((TextNode)e).text().trim();
							}
						}
					}
				} else {
					n.text += signature;
				}
			}
		}
	}
	
	
	private void addNarration(Narration n)
	{
		if ( n != null && !m_narrationToIndex.containsKey(n.id) )
		{
			m_narrationToIndex.put(n.id, m_narrations.size());
			m_narrations.add(n);
		}
	}
	

	@Override
	public List<Narration> getNarrations() {
		return m_narrations;
	}

	@Override
	public boolean preprocess(JSONObject json)
	{
		return true;
	}

	@Override
	public boolean hasGrade(int id)
	{
		return true;
	}
}