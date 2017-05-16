package com.canadainc.sunnah10.processors.shamela;

import java.util.List;

import org.json.simple.JSONObject;
import org.jsoup.nodes.Node;

import com.canadainc.sunnah10.processors.Processor;

public interface ShamelaProcessor extends Processor
{
	/**
	 * Processes this Shamela document.
	 * @param nodes The title, and text HTML nodes in the document.
	 * @param json The original JSON document. This might be useful to get
	 * certain metadata in certain circumstances.
	 */
	public void process(List<Node> nodes, JSONObject json);
}