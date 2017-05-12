package com.canadainc.sunnah10.shamela.mubarak;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import com.canadainc.sunnah10.Narration;
import com.canadainc.sunnah10.shamela.ShamelaProcessor;
import com.canadainc.sunnah10.shamela.ShamelaUtils;

public class ShamelaJihadProcessor implements ShamelaProcessor
{
	private ArrayList<Narration> m_narrations = new ArrayList<>();

	public ShamelaJihadProcessor()
	{
	}

	public void process(List<Node> nodes, JSONObject json)
	{
		Narration n = null;

		for (Node e: nodes)
		{
			if ( ShamelaUtils.isHadithNumberNode(e) )
			{
				ShamelaUtils.appendIfValid(n, m_narrations);
				
				n = new Narration();
				n.id = ShamelaUtils.parseHadithNumber(e);
				n.text = "";
			} else if ( ShamelaUtils.isTextNode(e) ) {
				String body = ((TextNode)e).text();
				
				if ( ( body.startsWith("أَخْبَرَنَا") || body.startsWith("حَدَّثَنَا") ) && (n == null) )
				{
					Narration prev = m_narrations.get( m_narrations.size()-1 );
					n = new Narration();
					n.id = prev.id+1;
					n.text = "";
				} else if ( ( body.startsWith("قَالَ") || body.startsWith("فَقَالَ") ) && (n == null) ) {
					Narration prev = m_narrations.get( m_narrations.size()-1 );
					prev.text += "\n\n"+body;
				}
				
				if (n != null) {
					n.text += body;
				}
			} else if ( ShamelaUtils.isTitleSpan(e) ) {
				n.text += ShamelaUtils.extractText(e);
			} 
		}
		
		if (n != null) {
			m_narrations.add(n);
		}
	}

	/**
	 * @return the narrations
	 */
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
		return false;
	}
}