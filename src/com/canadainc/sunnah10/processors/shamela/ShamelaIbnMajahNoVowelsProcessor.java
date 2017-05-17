package com.canadainc.sunnah10.processors.shamela;

import java.util.List;

import org.json.simple.JSONObject;
import org.jsoup.nodes.Node;

public class ShamelaIbnMajahNoVowelsProcessor extends AbstractShamelaProcessor
{
	public ShamelaIbnMajahNoVowelsProcessor()
	{
		m_typos.add(392, "321", "320");
		m_typos.add(906, "835", "834");
		m_typos.add(920, "849", "848");
	}

	@Override
	public void process(List<Node> nodes, JSONObject json) {
		ShamelaUtils.addIfSignatureMatches(nodes, "(سنن ابن ماجة)", m_narrations);
	}
}