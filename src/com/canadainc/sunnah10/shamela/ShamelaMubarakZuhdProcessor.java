package com.canadainc.sunnah10.shamela;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.simple.JSONObject;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import com.canadainc.sunnah10.Narration;

public class ShamelaMubarakZuhdProcessor implements ShamelaProcessor
{
	private ArrayList<Narration> m_narrations;
	private TypoProcessor m_typos;
	
	public ShamelaMubarakZuhdProcessor()
	{
		m_narrations = new ArrayList<>();
		m_typos = new TypoProcessor();
		
		HashMap<Integer,Integer> idInsertion = new HashMap<>();
		idInsertion.put(183, 177);
		idInsertion.put(411, 391);
		idInsertion.put(570, 553);
		idInsertion.put(578, 560);
		idInsertion.put(662, 642);
		idInsertion.put(679, 657);
		idInsertion.put(798, 772);
		idInsertion.put(804, 778);
		idInsertion.put(862, 833);
		idInsertion.put(892, 861);
		idInsertion.put(893, 862);
		idInsertion.put(1470, 1445);
		idInsertion.put(1486, 1461);
		idInsertion.put(1487, 1462);
		
		for (Integer page: idInsertion.keySet()) {
			m_typos.add(page,"أَخْبَرَكُمْ أَبُو", "<span class=\"red\">"+idInsertion.get(page)+" - </span>أَخْبَرَكُمْ أَبُو");
		}
		
		idInsertion.clear();
		idInsertion.put(782, 758);
		idInsertion.put(803, 778);
		idInsertion.put(1485, 1461);
		
		for (Integer page: idInsertion.keySet())
		{
			int matchedIndex = idInsertion.get(page);
			m_typos.add(page,matchedIndex+" - ",(matchedIndex-1)+" - ");
		}
		
		m_typos.add(784,"قَرَأَ الشَّيْخُ أَبُو", "<span class=\"red\">758 - </span>قَرَأَ الشَّيْخُ أَبُو");
		m_typos.add(1521,"أَخْبَرَكُمْ أَبُو عُمَرَ بْنُ حَيَوَيْهِ، حَدَّثَنَا يَحْيَى، حَدَّثَنَا الْحُسَيْنُ", "<span class=\"red\">1495 - </span>أَخْبَرَكُمْ أَبُو عُمَرَ بْنُ حَيَوَيْهِ، حَدَّثَنَا يَحْيَى، حَدَّثَنَا الْحُسَيْنُ");
	}

	@Override
	public void process(List<Node> nodes, JSONObject json)
	{
		Narration n = null;

		for (Node e: nodes)
		{
			if ( ShamelaUtils.isHadithNumberNode(e) )
			{
				if (n != null) { // 2 narrations in 1
					m_narrations.add(n);
				}
				
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
		if ( Integer.parseInt( json.get("pid").toString() ) > 1652 )
		{
			String content = json.get("content").toString();
			
			if ( !content.startsWith("<span") ) // chapter names
			{
				int id = m_narrations.get( m_narrations.size()-1 ).id+1;
				content = "<span class=\"red\">"+id+" - </span>"+content;
				json.replace("content", content);
			}
		} else {
			m_typos.process(json);
		}
	}
}