package com.canadainc.sunnah10.processors.shamela.mubarak;

import java.util.HashMap;
import java.util.List;

import org.json.simple.JSONObject;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import com.canadainc.sunnah10.Narration;
import com.canadainc.sunnah10.processors.TypoProcessor;
import com.canadainc.sunnah10.processors.shamela.AbstractShamelaProcessor;
import com.canadainc.sunnah10.processors.shamela.ShamelaUtils;

public class ShamelaMubarakZuhdProcessor extends AbstractShamelaProcessor
{
	public ShamelaMubarakZuhdProcessor()
	{
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
			} else if ( ShamelaUtils.isTitleSpan(e) && (n != null) ) {
				n.text += ShamelaUtils.extractText(e);
			}
		}

		ShamelaUtils.appendIfValid(n, m_narrations);
	}

	@Override
	public String preprocess(int page, String content)
	{
		if (page > 1652 && !content.startsWith("<span"))
		{
			int id = m_narrations.get( m_narrations.size()-1 ).id+1;
			content = TypoProcessor.decorateContent( String.valueOf(id) )+content;
			return content;
		}

		return super.preprocess(page, content);
	}
}