package com.canadainc.sunnah10.processors.shamela;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.canadainc.sunnah10.Narration;
import com.canadainc.sunnah10.processors.TypoProcessor;

public abstract class AbstractShamelaProcessor implements ShamelaProcessor
{
	protected final ArrayList<Narration> m_narrations = new ArrayList<>();
	protected final TypoProcessor m_typos = new TypoProcessor();

	protected AbstractShamelaProcessor()
	{
	}

	@Override
	public final boolean preprocess(JSONObject json)
	{
		int page = getPageNumber(json);
		String content = json.get("content").toString();
		content = preprocess(page, content);

		if (content == null) {
			return false;
		}

		content = m_typos.process(page, content);
		json.put("content", content);

		return true;
	}


	@Override
	public final void process(JSONObject json)
	{
		Document d = Jsoup.parse( json.get("content").toString() );
		process( d.body().childNodes(), json );
	}


	@Override
	public final List<Narration> getNarrations() {
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
}