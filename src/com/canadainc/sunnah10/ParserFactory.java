package com.canadainc.sunnah10;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.canadainc.sunnah10.processors.Processor;
import com.canadainc.sunnah10.processors.shamela.AbstractShamelaProcessor;
import com.canadainc.sunnah10.processors.shamela.ShamelaAasimProcessor;
import com.canadainc.sunnah10.processors.shamela.ShamelaBayhaqiKubraProcessor;
import com.canadainc.sunnah10.processors.shamela.ShamelaContinuedProcessor;
import com.canadainc.sunnah10.processors.shamela.ShamelaDarimiProcessor;
import com.canadainc.sunnah10.processors.shamela.ShamelaIbaanahProcessor;
import com.canadainc.sunnah10.processors.shamela.ShamelaKhallalProcessor;
import com.canadainc.sunnah10.processors.shamela.ShamelaMishkaatMasaabihProcessor;
import com.canadainc.sunnah10.processors.shamela.albaani.ShamelaIrwaProcessor;
import com.canadainc.sunnah10.processors.shamela.albaani.ShamelaJaamiDaifProcessor;
import com.canadainc.sunnah10.processors.shamela.albaani.ShamelaJaamiProcessor;
import com.canadainc.sunnah10.processors.shamela.albaani.ShamelaSilsilaDaifProcessor;
import com.canadainc.sunnah10.processors.shamela.albaani.ShamelaTaleeqaatHisaanProcessor;
import com.canadainc.sunnah10.processors.shamela.albaani.ShamelaTargheebProcessor;
import com.canadainc.sunnah10.processors.shamela.shared.ShamelaIbnMajahNoVowelsProcessor;
import com.canadainc.sunnah10.processors.shamela.shared.ShamelaIbnMajahVowelledProcessor;
import com.canadainc.sunnah10.processors.shamela.shared.ShamelaSunanNasaiNoVowelsProcessor;
import com.canadainc.sunnah10.processors.shamela.shared.ShamelaSunanNasaiVowelledProcessor;
import com.canadainc.sunnah10.processors.shamela.shared.ShamelaTirmidhiNoVowelsProcessor;
import com.canadainc.sunnah10.processors.shamela.shared.ShamelaTirmidhiVowelledProcessor;
import com.canadainc.sunnah10.processors.sunnah.com.AbstractSunnahDotComProcessor;
import com.canadainc.sunnah10.processors.sunnah.com.BukhariProcessor;
import com.canadainc.sunnah10.processors.sunnah.com.IbnMajahProcessor;
import com.canadainc.sunnah10.processors.sunnah.com.NasaiProcessor;
import com.canadainc.sunnah10.processors.sunnah.com.TirmidhiProcessor;

public class ParserFactory
{
	private Map<String,Class<? extends Processor>> m_keyToProcessor;

	public ParserFactory()
	{
		m_keyToProcessor = new HashMap<String, Class<? extends Processor>>();
		registerCollections(AbstractSunnahDotComProcessor.class, "adab", "abudawud", "malik", "muslim", "nawawi40", "qudsi40", "riyadussaliheen");
		
		m_keyToProcessor.put("bukhari", BukhariProcessor.class);
		m_keyToProcessor.put("bayhaqi_kubra", ShamelaBayhaqiKubraProcessor.class);
		m_keyToProcessor.put("jaami_sagheer_daif", ShamelaJaamiDaifProcessor.class);
		m_keyToProcessor.put("ibaanah_kubra", ShamelaIbaanahProcessor.class);
		m_keyToProcessor.put("ibnmajah", IbnMajahProcessor.class);
		m_keyToProcessor.put("ibnmajah_no_vowels", ShamelaIbnMajahNoVowelsProcessor.class);
		m_keyToProcessor.put("ibnmajah_vowels", ShamelaIbnMajahVowelledProcessor.class);
		m_keyToProcessor.put("mishkaat_masaabeeh", ShamelaMishkaatMasaabihProcessor.class);
		m_keyToProcessor.put("nasai", NasaiProcessor.class);
		m_keyToProcessor.put("nasai_no_vowels", ShamelaSunanNasaiNoVowelsProcessor.class);
		m_keyToProcessor.put("nasai_vowels", ShamelaSunanNasaiVowelledProcessor.class);
		m_keyToProcessor.put("saheeh_irwa", ShamelaIrwaProcessor.class);
		m_keyToProcessor.put("saheeh_jaami", ShamelaJaamiProcessor.class);
		m_keyToProcessor.put("saheeh_targheeb", ShamelaTargheebProcessor.class);
		m_keyToProcessor.put("silsila_daif", ShamelaSilsilaDaifProcessor.class);
		m_keyToProcessor.put("sunan_darimi", ShamelaDarimiProcessor.class);
		m_keyToProcessor.put("sunnah_khallal", ShamelaKhallalProcessor.class);
		m_keyToProcessor.put("sunnah_aasim", ShamelaAasimProcessor.class);
		m_keyToProcessor.put("taleeqat_hisaan", ShamelaTaleeqaatHisaanProcessor.class);
		m_keyToProcessor.put("tanbeeh_ghafileen", ShamelaContinuedProcessor.class);
		m_keyToProcessor.put("tirmidhi", TirmidhiProcessor.class);
		m_keyToProcessor.put("tirmidhi_no_vowels", ShamelaTirmidhiNoVowelsProcessor.class);
		m_keyToProcessor.put("tirmidhi_vowels", ShamelaTirmidhiVowelledProcessor.class);
	}
	
	private final void registerCollections(Class<? extends Processor> p, String ...collections)
	{
		for (String collection: collections) {
			m_keyToProcessor.put(collection, p);
		}
	}


	public Collection<String> getKeys() {
		return m_keyToProcessor.keySet();
	}


	public Processor getProcessor(String key) throws InstantiationException, IllegalAccessException
	{
		Class<? extends Processor> c = m_keyToProcessor.get(key);

		if (c == null) {
			System.err.println("NoParserFound: Using standard processor...");
			c = AbstractShamelaProcessor.class;
		}

		return c.newInstance();
	}
}
