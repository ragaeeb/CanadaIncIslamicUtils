package com.canadainc.sunnah10.processors.shamela;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;

import com.canadainc.sunnah10.Narration;

public class AbstractShamelaProcessor implements ShamelaProcessor
{
	protected final ArrayList<Narration> m_narrations = new ArrayList<>();
	protected final ShamelaTypoProcessor m_typos = new ShamelaTypoProcessor();

	@Override
	public final boolean preprocess(JSONObject json)
	{
		if ( json.containsKey("data") ) { // index file for book titles and chapters
			return false;
		}
		
		int page = getPageNumber(json);
		String content = json.get("content").toString();
		content = preprocess(page, content);

		if (content == null) {
			return false;
		}

		content = m_typos.process(page, content);

		if (content == null) {
			return false;
		}

		json.put("content", content);

		return true;
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
				String body = e.toString();

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
	public final void process(JSONObject json)
	{
		Document d = Jsoup.parse( json.get("content").toString() );
		process( d.body().childNodes(), json );
	}


	@Override
	public List<Narration> getNarrations() {
		return m_narrations;
	}


	protected String preprocess(int page, String content) {
		return content;
	}

	@Override
	public boolean hasGrade(int id) {
		return false;
	}

	@Override
	public int getPageNumber(JSONObject json) {
		return Integer.parseInt( json.get("pid").toString() );
	}


	protected Narration getPrev() {
		return m_narrations.get( m_narrations.size()-1 );
	}


	/* (non-Javadoc)
	 * @see com.canadainc.sunnah10.processors.Processor#postProcess()
	 */
	@Override
	public void postProcess()
	{
	}
}