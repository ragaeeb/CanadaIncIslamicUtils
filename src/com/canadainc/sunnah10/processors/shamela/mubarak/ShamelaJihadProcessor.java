package com.canadainc.sunnah10.processors.shamela.mubarak;

import java.util.List;

import org.json.simple.JSONObject;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import com.canadainc.sunnah10.Narration;
import com.canadainc.sunnah10.processors.shamela.AbstractShamelaProcessor;
import com.canadainc.sunnah10.processors.shamela.ShamelaUtils;

public class ShamelaJihadProcessor extends AbstractShamelaProcessor
{
	public void process(List<Node> nodes, JSONObject json)
	{
		Narration n = null;

		for (Node e: nodes)
		{
			if ( ShamelaUtils.isHadithNumberNode(e) ) {
				n = ShamelaUtils.createNewNarration(n, e, m_narrations);
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
		
		ShamelaUtils.appendIfValid(n, m_narrations);
	}
}