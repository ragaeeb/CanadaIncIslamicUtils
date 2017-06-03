package com.canadainc.sunnah10.processors.shamela.shared;

import java.util.List;

import org.json.simple.JSONObject;
import org.jsoup.nodes.Node;

import com.canadainc.sunnah10.processors.shamela.AbstractShamelaProcessor;
import com.canadainc.sunnah10.processors.shamela.ShamelaUtils;

public class ShamelaTirmidhiNoVowelsProcessor extends AbstractShamelaProcessor
{
	public ShamelaTirmidhiNoVowelsProcessor() {
		m_typos.add(1333, "1332", "1333");
		m_typos.add(343, "243", "343");
	}

	@Override
	public void process(List<Node> nodes, JSONObject json) {
		ShamelaUtils.addIfSignatureMatches(nodes, "(سنن الترمذي)", m_narrations);
	}
}