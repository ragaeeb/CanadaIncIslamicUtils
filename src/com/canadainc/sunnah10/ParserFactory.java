package com.canadainc.sunnah10;

import java.util.HashMap;
import java.util.Map;

import com.canadainc.sunnah10.processors.Processor;
import com.canadainc.sunnah10.processors.SunnahDotComProcessor;
import com.canadainc.sunnah10.processors.shamela.ShamelaAwaanahProcessor;
import com.canadainc.sunnah10.processors.shamela.ShamelaBazzaarProcessor;
import com.canadainc.sunnah10.processors.shamela.ShamelaDarimiProcessor;
import com.canadainc.sunnah10.processors.shamela.ShamelaDawudZuhdProcessor;
import com.canadainc.sunnah10.processors.shamela.ShamelaIbaanahProcessor;
import com.canadainc.sunnah10.processors.shamela.ShamelaIbnMajahNoVowelsProcessor;
import com.canadainc.sunnah10.processors.shamela.ShamelaMustadrakProcessor;
import com.canadainc.sunnah10.processors.shamela.ShamelaSunanNasaiNoVowelsProcessor;
import com.canadainc.sunnah10.processors.shamela.ShamelaSunanNasaiVowelledProcessor;
import com.canadainc.sunnah10.processors.shamela.ShamelaTirmidhiNoVowelsProcessor;
import com.canadainc.sunnah10.processors.shamela.albaani.ShamelaIrwaProcessor;
import com.canadainc.sunnah10.processors.shamela.albaani.ShamelaJaamiProcessor;
import com.canadainc.sunnah10.processors.shamela.albaani.ShamelaSilsilaDaifProcessor;
import com.canadainc.sunnah10.processors.shamela.albaani.ShamelaTargheebProcessor;
import com.canadainc.sunnah10.processors.shamela.mubarak.ShamelaJihadProcessor;
import com.canadainc.sunnah10.processors.shamela.mubarak.ShamelaMubarakZuhdProcessor;

public class ParserFactory
{
	private Map<String,Class<? extends Processor>> m_keyToProcessor;

	public ParserFactory()
	{
		m_keyToProcessor = new HashMap<String, Class<? extends Processor>>();
		m_keyToProcessor.put("awaanah", ShamelaAwaanahProcessor.class);
		m_keyToProcessor.put("bazzaar", ShamelaBazzaarProcessor.class);
		m_keyToProcessor.put("darimi", ShamelaDarimiProcessor.class);
		m_keyToProcessor.put("zuhd_dawud", ShamelaDawudZuhdProcessor.class);
		m_keyToProcessor.put("ibaanah", ShamelaIbaanahProcessor.class);
		m_keyToProcessor.put("ibnmajah_no_vowels", ShamelaIbnMajahNoVowelsProcessor.class);
		m_keyToProcessor.put("mustadrak", ShamelaMustadrakProcessor.class);
		m_keyToProcessor.put("nasai_no_vowels", ShamelaSunanNasaiNoVowelsProcessor.class);
		m_keyToProcessor.put("nasai_vowels", ShamelaSunanNasaiVowelledProcessor.class);
		m_keyToProcessor.put("tirmidhi_no_vowels", ShamelaTirmidhiNoVowelsProcessor.class);
		m_keyToProcessor.put("irwa", ShamelaIrwaProcessor.class);
		m_keyToProcessor.put("jaami", ShamelaJaamiProcessor.class);
		m_keyToProcessor.put("silsila_daif", ShamelaSilsilaDaifProcessor.class);
		m_keyToProcessor.put("sunnah.com", SunnahDotComProcessor.class);
		m_keyToProcessor.put("targheeb", ShamelaTargheebProcessor.class);
		m_keyToProcessor.put("jihad", ShamelaJihadProcessor.class);
		m_keyToProcessor.put("zuhd_mubarak", ShamelaMubarakZuhdProcessor.class);
	}


	public Processor getProcessor(String key) throws InstantiationException, IllegalAccessException {
		return m_keyToProcessor.get(key).newInstance();
	}
}
