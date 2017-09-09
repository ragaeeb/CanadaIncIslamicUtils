package com.canadainc.sunnah10.processors.shamela;

import java.util.List;

import org.json.simple.JSONObject;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import com.canadainc.sunnah10.Chapter;
import com.canadainc.sunnah10.Narration;

public class ShamelaAasimProcessor extends AbstractShamelaProcessor
{
	private Chapter m_chapter;

	public ShamelaAasimProcessor()
	{
		m_typos.prependHadithNumber(13,11);
		m_typos.prependHadithNumber(44,39);
		m_typos.removeBlank(76,66);
		m_typos.removeBlank(77,68);
		m_typos.prependHadithNumber(114,102);
		m_typos.prependHadithNumber(130,116);
		m_typos.removeBlank(160,165);
		m_typos.prependHadithNumber(240,241);
		m_typos.removeBlank(241,243);
		m_typos.removeBlank(258,259);
		m_typos.removeBlank(267,266);
		m_typos.prependHadithNumber(306,311);
		m_typos.prependHadithNumber(314,319);
		m_typos.prependHadithNumber(325,328);
		m_typos.prependHadithNumber(332,332);
		m_typos.removeBlank(361,354);
		m_typos.prependHadithNumber(368,359);
		m_typos.prependHadithNumber(488,473);
		m_typos.prependHadithNumber(502,489);
		m_typos.prependHadithNumber(587,572);
		m_typos.prependHadithNumber(615,594);
		m_typos.removeBlank(656,623);
		m_typos.prependHadithNumber(678,647);
		m_typos.add(710, "وَ685 -", ShamelaTypoProcessor.decorateContent("685"));
		m_typos.add(738, "وَ715", ShamelaTypoProcessor.decorateContent("715"));
		m_typos.removeBlank(782,755);
		m_typos.removeBlank(793,764);
		m_typos.removeBlank(872,843);
		m_typos.prependHadithNumber(876,847);
		m_typos.prependHadithNumber(1246,1244);
		m_typos.prependHadithNumber(1255,1252);
		m_typos.removeBlank(1270,1270);
		m_typos.removeBlank(1292,1294);
		m_typos.prependHadithNumber(1390,1391);
		m_typos.prependHadithNumber(1413,1410);
		m_typos.prependHadithNumber(1472,1473);
	}

	@Override
	public void process(List<Node> nodes, JSONObject json)
	{
		Narration n = null;

		for (Node e: nodes)
		{
			if ( ShamelaUtils.isHadithNumberNode(e) ) {
				Node next = e.nextSibling();

				if ( ShamelaUtils.isTitleSpan(next) ) {
					m_chapter = new Chapter(ShamelaUtils.extractText(next), m_chapter == null ? 1 : m_chapter.number+1);
				} else {
					n = ShamelaUtils.createNewNarration(n, e, m_narrations);
					n.chapter = m_chapter;
				}
			} else if ( ShamelaUtils.isTextNode(e) ) {
				String body = ((TextNode)e).text();
				appendBody(n, body);
			}
		}

		ShamelaUtils.appendIfValid(n, m_narrations);
	}


	private void appendBody(Narration n, String body)
	{
		if (n != null) {
			n.text += body;
		} else if ( !m_narrations.isEmpty() ) {
			m_narrations.get( m_narrations.size()-1 ).text += body;
		}
	}
}