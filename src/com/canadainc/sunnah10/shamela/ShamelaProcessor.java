package com.canadainc.sunnah10.shamela;

import java.util.List;

import org.json.simple.JSONObject;
import org.jsoup.nodes.Node;

import com.canadainc.sunnah10.Narration;

public interface ShamelaProcessor
{
	/**
	 * Processes this Shamela document.
	 * @param nodes The title, and text HTML nodes in the document.
	 * @param json The original JSON document. This might be useful to get
	 * certain metadata in certain circumstances.
	 */
	public void process(List<Node> nodes, JSONObject json);
	
	
	/**
	 * All the narrations that were collected this far.
	 * @return All the narrations that were processed thus far.
	 */
	public List<Narration> getNarrations();
}
