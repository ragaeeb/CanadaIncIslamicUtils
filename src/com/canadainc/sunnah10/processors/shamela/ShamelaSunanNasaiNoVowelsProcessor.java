package com.canadainc.sunnah10.processors.shamela;

import java.util.List;

import org.json.simple.JSONObject;
import org.jsoup.nodes.Node;

public class ShamelaSunanNasaiNoVowelsProcessor extends AbstractShamelaProcessor
{
	@Override
	public void process(List<Node> nodes, JSONObject json) {
		ShamelaUtils.addIfSignatureMatches(nodes, "سنن النسائي", m_narrations);
	}
}