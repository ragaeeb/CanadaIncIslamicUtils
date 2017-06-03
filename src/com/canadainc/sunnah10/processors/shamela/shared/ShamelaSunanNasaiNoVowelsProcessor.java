package com.canadainc.sunnah10.processors.shamela.shared;

import java.util.List;

import org.json.simple.JSONObject;
import org.jsoup.nodes.Node;

import com.canadainc.sunnah10.processors.shamela.AbstractShamelaProcessor;
import com.canadainc.sunnah10.processors.shamela.ShamelaUtils;

public class ShamelaSunanNasaiNoVowelsProcessor extends AbstractShamelaProcessor
{
	@Override
	public void process(List<Node> nodes, JSONObject json) {
		ShamelaUtils.addIfSignatureMatches(nodes, "(سنن النسائي)", m_narrations);
	}
}