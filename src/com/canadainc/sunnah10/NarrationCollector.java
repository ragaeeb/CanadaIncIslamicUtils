package com.canadainc.sunnah10;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NarrationCollector implements Collector
{
	/** (Key: Language, Value: [<Key: Collection, Value: Translator>]) */
	private Map< String, Map<String, List<Integer>> > m_translations;

	private Map<String, Collection<Narration>> m_narrations;
	
	public NarrationCollector()
	{
		m_narrations = new HashMap<String, Collection<Narration>>(8);

		Map<String, List<Integer>> english = new HashMap<String, List<Integer>>();
		english.put("bukhari", Arrays.asList(1988)); // Dr. M. Muhsin Khan
		english.put("muslim", Arrays.asList(1989)); // Abdul Hamid Siddiqui
		english.put("abudawud", Arrays.asList(1990)); // Ahmad Hasan
		english.put(SunnahConstants.COLLECTION_BULUGH_MARAM, Arrays.asList(SunnahConstants.BULUGH_MARAM_TRANSLATOR)); // Nancy Eweiss
		english.put("malik", Arrays.asList(1991,1992)); // `A'isha `Abdarahman at-Tarjumana and Ya`qub Johnson

		m_translations = new HashMap<String, Map<String, List<Integer>> >(1);
		m_translations.put("english", english);
	}

	@Override
	/*
	 * (non-Javadoc)
	 * @see com.canadainc.sunnah10.Collector#process(java.util.Collection, boolean, java.lang.String)
	 */
	public void process(Collection<Narration> narrations, String language, String collection)
	{
		String lastBabName = null;
		int lastBabNum = 0;

		Collection<Narration> contents = m_narrations.get(collection);

		if (contents == null) {
			contents = new ArrayList<Narration>();
		}

		int translator = 0;
		Map<String, List<Integer>> translators = m_translations.get(language);

		if ( translators != null && translators.containsKey(collection) )
		{
			List<Integer> available = translators.get(collection);
			translator = available.get( (int)Math.random()*available.size() ); // assume random value from collections with multiple translators
		}

		for (Narration n: narrations)
		{
			if ( n.chapter.title == null || n.chapter.title.isEmpty() ) {
				n.chapter.title = lastBabName;
				n.chapter.number = lastBabNum;
			}

			n.translator = translator;
			lastBabName = n.chapter.title;
			lastBabNum = n.chapter.number;

			correctHadithNumber(n);
			correctHadithBody(n, language.equals(SunnahConstants.LANGUAGE_ARABIC));
		}

		contents.addAll(narrations);
		m_narrations.put(collection, contents);
	}


	public void correctHadithBody(Narration n, boolean arabic)
	{
		String toConvert = n.text;

		if ( toConvert.endsWith("</b>") ) {
			toConvert = toConvert.substring( 0, toConvert.lastIndexOf("</b>") );
		}

		if (!arabic)
		{
			toConvert = toConvert.replaceAll("\\(S\\)|\\[SAW\\]|\\([sS]\\.[aA]\\.[wW]\\)|\\(SAW0{0,1}\\)|\\(saws\\)|SAW0|\\({0,1}SWAS\\){0,1}|\\(saW\\)|\\(saas\\)|[pP]\\.[bB]\\.[uU]\\.[hH]\\.{0,1}|pbuh", "ﷺ");

			toConvert = toConvert.replaceAll("\\s+", " ");
			//toConvert = toConvert.replaceAll("\\s{2,}", "");

			// TODO: " : "
			// TODO: ":   "
			// TODO: sal Allaahu alayhi wa sallam
			// TODO: ( sal Allaahu alayhi wa sallam)
			// TODO: STARTS WITH: "1450; ) Jabir said: The Messenger of Allah (ﷺ) sent us on an expedition and made Abu ‘Ubaidah b. al-Jarrah our leader. We had to meet a caravan of the Quraish. He gave us a bag of dates as a light meal during the journey. We had nothing except that. Abu ‘Ubaidah would give each of us one date. We used to suck them as a child sucks, and drink water after that and it sufficed us that day till night. We used to beat leaves off the trees with our sticks (for food), wetted them with water and ate them. We then went to the coast of the sea. There appeared to us a body like a great mound. When we came to it, we found that it was an animal called al-anbar. Abu ‘Ubaidah said: It is a carrion, and it is not lawful for us. He then said: No, we are the Messengers of the Apostel of Allah (ﷺ) and we are in the path of Allah. If you are forced by necessity (to eat it), then eat it. We stayed feeding on it for one mouth, till we became fat, and we were three hundred in number. When we came to the Messenger of Allah (ﷺ), we mentioned it to him. He said : It is a provision which Allah has brought forth for you, and give us some to eat if you have any meat of it with you. So we sent some of it to the Messenger of Allah (ﷺ) and he ate (it)."
			// 14; It was narrated that Ans said: "The Messenger of Allah sacrificed two Amlah rams."
		}

		toConvert = toConvert.replaceAll("<[^>]*>", "");

		if ( toConvert.startsWith("\"") && toConvert.endsWith("\"") ) {
			toConvert = toConvert.substring( 1, toConvert.length()-1 );
		}

		if ( toConvert.startsWith("\"") ) {
			toConvert = toConvert.substring(1);
		}

		//TODO: STARTSWITH: " Abdullah b. Umar

		n.text = toConvert.trim();
	}


	public void correctHadithNumber(Narration n)
	{
		String result = n.hadithNumber.trim();

		if ( result.startsWith("Introduction ") )
		{
			result = result.substring( "Introduction ".length() );
			n.hadithNumber = result;
		}
	}
	
	
	public void swapHadithNumbersWith(String collection, Collection<Narration> theirs)
	{
		Map<Integer,Narration> idToNarration = new HashMap<>();
		
		for ( Narration n: m_narrations.get(collection) ) {
			idToNarration.put(n.id, n);
		}
		
		for (Narration n: theirs) {
			System.out.println(n.id);
			Narration ours = idToNarration.get(n.id);
			String hadithNumber = ours.hadithNumber;
			ours.hadithNumber = n.hadithNumber;
			n.hadithNumber = hadithNumber;
		}
	}


	public Map<String, Collection<Narration>> getCollected() {
		return m_narrations;
	}
}