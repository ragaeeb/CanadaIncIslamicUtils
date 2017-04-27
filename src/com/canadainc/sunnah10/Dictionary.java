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
		typos.put("raice", "raising");
		typos.put("I p", "A"); // b1018
		typos.put("Ab Huraira", "Abu Huraira"); // m1468b
		typos.put("A'Asha", "A'isha");
		typos.put("Apostel", "Apostle");
		typos.put("untol", "until"); // i3673
		typos.put("b.rryirg", "burying"); // i2089
		typos.put("youabsolute", "you absolute"); // t2382
		typos.put("Shesaid:", "She said:"); // m1479a
		typos.put("youhave", "you have"); // m1479a
		typos.put("Ans bin Malik", "Anas bin Malik");
		typos.put("verso", "version"); // d1494
		typos.put("Mersenger", "Messenger");
		typos.put("A'isba", "A'isha"); // m1504b
		typos.put("Mes- senger", "Messenger"); // m2716d
		typos.put("coame", "came"); // n2906, n3044
		typos.put("evil is genuine", "evil eye is genuine"); // d3879
		typos.put("pro- nounced", "pronounced");
		typos.put("pro- nounces", "pronounces");
		typos.put("narraterd", "narrated");
		typos.put("YHouse", "house"); // n2917
		typos.put("do not6 eat", "do not eat");
		typos.put("Wh is this (man) ?", "Who is this (man)?");
		typos.put("yon can put off", "you can put off");
		//typos.put("(ra)", "(radi Allahu anhu)");
		//typos.put("(RAA)", "(radi Allahu anhu)");
		
		// d718, a575: bitch
		// r1080: Shaqiq bin 'Abdullah should be 'Abdullah bin Shaqeeq: i checked the fahaaris of Tirmidhi in a hard copy i dont have online Tirmidhi. theres no Shaqeeq bin Abdullah in Tirmidhi. i also checked the explanation of al-arba'oon of sheikh Uthaymeen on the hadeeth of Jibreel on the fawaa-id(the benefits of the hadeeth) on the 10th benefit the sheikh bring the same athar as an athar of Abdullah bin Shaqeeq and not Shaqeeq bin Abdullah. baarakallahu feekum
		// TODO: take a look at html of: 128330
		
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