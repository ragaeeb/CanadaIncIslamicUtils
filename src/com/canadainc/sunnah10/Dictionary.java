package com.canadainc.sunnah10;

import java.util.HashMap;
import java.util.Map;

public class Dictionary
{
	private Map<String, String> typos;

	public Dictionary()
	{
		typos = new HashMap<String, String>();
		typos.put("narated", "narrated");
		typos.put("transrmitters", "transmitters");
		typos.put("Mesenger", "Messenger");
		typos.put("Propet", "Prophet");
		typos.put("comitted", "committed");
		typos.put("(s.a.w)", "ﷺ");
		typos.put("Coindition", "Condition");
		typos.put("Ofjudgement", "Of Judgment");
		//typos.put("(ra)", "(radi Allahu anhu)");
		//typos.put("(RAA)", "(radi Allahu anhu)");
	}


	public String correctTypos(String body)
	{
		for ( String key: typos.keySet() ) {
			body = body.replace( key, typos.get(key) );
		}

		return body;
	}
}