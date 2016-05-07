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
		typos.put("(mail peace he upon him)", "ﷺ");
		typos.put("(masy peace be upon him)", "ﷺ");
		typos.put("(may peace be upon him)", "ﷺ");
		typos.put("(May peace be upon him)", "ﷺ");
		typos.put("(may. peace be upon him)", "ﷺ");
		typos.put("Coindition", "Condition");
		typos.put("Ofjudgement", "Of Judgment");
		typos.put("tliree tinies", "three times");
		typos.put("Abu Hurarirah", "Abu Hurairah");
		typos.put("Abu Huraire", "Abu Hurairah");
		typos.put("Abu Safyan", "Abu Sufyan");
		typos.put("Abu Sfyan", "Abu Sufyan");
		typos.put("he hid finished", "he had finished");
		typos.put("Um Sulaim", "Umm Sulaim");
		typos.put("Um-Sulaim", "Umm Sulaim");
		typos.put("Um Salama", "Umm Salama");
		typos.put("Abu Talba", "Abu Talha");
		typos.put("sacr ficed", "sacrificed");
		typos.put("Ans bin Malik", "Anas bin Malik");
		typos.put("pro- nounced", "pronounced");
		typos.put("pro- nounces", "pronounces");
		typos.put("do not6 eat", "do not eat");
		typos.put("Wh is this (man) ?", "Who is this (man)?");
		typos.put("yon can put off", "you can put off");
		//typos.put("(ra)", "(radi Allahu anhu)");
		//typos.put("(RAA)", "(radi Allahu anhu)");
		
		//TODO: Turn these into regex
	}


	public String correctTypos(String body)
	{
		for ( String key: typos.keySet() ) {
			body = body.replace( key, typos.get(key) );
		}

		return body;
	}
}