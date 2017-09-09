package com.canadainc.sunnah10;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.canadainc.sunnah10.processors.Processor;
import com.canadainc.sunnah10.processors.shamela.ShamelaAasimProcessor;
import com.canadainc.sunnah10.processors.shamela.ShamelaAbdurrazzaaqProcessor;
import com.canadainc.sunnah10.processors.shamela.ShamelaAbiShaybahProcessor;
import com.canadainc.sunnah10.processors.shamela.ShamelaAwaanahProcessor;
import com.canadainc.sunnah10.processors.shamela.ShamelaAwsatProcessor;
import com.canadainc.sunnah10.processors.shamela.ShamelaBaghdadiAkhlaaqProcessor;
import com.canadainc.sunnah10.processors.shamela.ShamelaBayhaqiEemaanProcessor;
import com.canadainc.sunnah10.processors.shamela.ShamelaBayhaqiKubraProcessor;
import com.canadainc.sunnah10.processors.shamela.ShamelaBazzaarProcessor;
import com.canadainc.sunnah10.processors.shamela.ShamelaContinuedProcessor;
import com.canadainc.sunnah10.processors.shamela.ShamelaDarimiProcessor;
import com.canadainc.sunnah10.processors.shamela.ShamelaDunyaGheebaProcessor;
import com.canadainc.sunnah10.processors.shamela.ShamelaDunyaNaarProcessor;
import com.canadainc.sunnah10.processors.shamela.ShamelaDunyaSamtProcessor;
import com.canadainc.sunnah10.processors.shamela.ShamelaFawaaidTamamProcessor;
import com.canadainc.sunnah10.processors.shamela.ShamelaHibbanProcessor;
import com.canadainc.sunnah10.processors.shamela.ShamelaIbaanahProcessor;
import com.canadainc.sunnah10.processors.shamela.ShamelaIlmAmlProcessor;
import com.canadainc.sunnah10.processors.shamela.ShamelaJaamiFadlProcessor;
import com.canadainc.sunnah10.processors.shamela.ShamelaKhallalProcessor;
import com.canadainc.sunnah10.processors.shamela.ShamelaKhuzaymaProcessor;
import com.canadainc.sunnah10.processors.shamela.ShamelaLaalikaaeeProcessor;
import com.canadainc.sunnah10.processors.shamela.ShamelaMishkaatMasaabihProcessor;
import com.canadainc.sunnah10.processors.shamela.ShamelaMusnadAhmadIndexedProcessor;
import com.canadainc.sunnah10.processors.shamela.ShamelaMusnadYalaaProcessor;
import com.canadainc.sunnah10.processors.shamela.ShamelaMustadrakProcessor;
import com.canadainc.sunnah10.processors.shamela.ShamelaNasaiKubraProcessor;
import com.canadainc.sunnah10.processors.shamela.ShamelaQasrAmrProcessor;
import com.canadainc.sunnah10.processors.shamela.ShamelaShihaabProcessor;
import com.canadainc.sunnah10.processors.shamela.ShamelaStandardProcessor;
import com.canadainc.sunnah10.processors.shamela.ShamelaSunnahHanbalProcessor;
import com.canadainc.sunnah10.processors.shamela.ShamelaTaarikhBaghdadiProcessor;
import com.canadainc.sunnah10.processors.shamela.ShamelaTabaraniProcessor;
import com.canadainc.sunnah10.processors.shamela.albaani.ShamelaIrwaProcessor;
import com.canadainc.sunnah10.processors.shamela.albaani.ShamelaJaamiDaifProcessor;
import com.canadainc.sunnah10.processors.shamela.albaani.ShamelaJaamiProcessor;
import com.canadainc.sunnah10.processors.shamela.albaani.ShamelaSilsilaDaifProcessor;
import com.canadainc.sunnah10.processors.shamela.albaani.ShamelaTaleeqaatHisaanProcessor;
import com.canadainc.sunnah10.processors.shamela.albaani.ShamelaTargheebProcessor;
import com.canadainc.sunnah10.processors.shamela.mubarak.ShamelaJihadProcessor;
import com.canadainc.sunnah10.processors.shamela.mubarak.ShamelaMubarakZuhdProcessor;
import com.canadainc.sunnah10.processors.shamela.shared.ShamelaAbuDawudNoVowelsProcessor;
import com.canadainc.sunnah10.processors.shamela.shared.ShamelaIbnMajahNoVowelsProcessor;
import com.canadainc.sunnah10.processors.shamela.shared.ShamelaIbnMajahVowelledProcessor;
import com.canadainc.sunnah10.processors.shamela.shared.ShamelaSunanNasaiNoVowelsProcessor;
import com.canadainc.sunnah10.processors.shamela.shared.ShamelaSunanNasaiVowelledProcessor;
import com.canadainc.sunnah10.processors.shamela.shared.ShamelaTirmidhiNoVowelsProcessor;
import com.canadainc.sunnah10.processors.shamela.shared.ShamelaTirmidhiVowelledProcessor;
import com.canadainc.sunnah10.processors.sunnah.com.AbstractSunnahDotComProcessor;
import com.canadainc.sunnah10.processors.sunnah.com.IbnMajahProcessor;
import com.canadainc.sunnah10.processors.sunnah.com.NasaiProcessor;
import com.canadainc.sunnah10.processors.sunnah.com.TirmidhiProcessor;

public class ParserFactory
{
	private Map<String,Class<? extends Processor>> m_keyToProcessor;

	public ParserFactory()
	{
		m_keyToProcessor = new HashMap<String, Class<? extends Processor>>();
		m_keyToProcessor.put("abudawud", AbstractSunnahDotComProcessor.class);
		m_keyToProcessor.put("abudawud_no_vowels", ShamelaAbuDawudNoVowelsProcessor.class);
		m_keyToProcessor.put("awaanah", ShamelaAwaanahProcessor.class);
		m_keyToProcessor.put("baghdadi_akhlaaq", ShamelaBaghdadiAkhlaaqProcessor.class);
		m_keyToProcessor.put("bayhaqi_kubra", ShamelaBayhaqiKubraProcessor.class);
		m_keyToProcessor.put("bayhaqi_shuab_eemaan", ShamelaBayhaqiEemaanProcessor.class);
		m_keyToProcessor.put("jaami_sagheer_daif", ShamelaJaamiDaifProcessor.class);
		m_keyToProcessor.put("dunya_gheeba", ShamelaDunyaGheebaProcessor.class);
		m_keyToProcessor.put("dunya_naar", ShamelaDunyaNaarProcessor.class);
		m_keyToProcessor.put("dunya_samt", ShamelaDunyaSamtProcessor.class);
		m_keyToProcessor.put("fawaaid_tamam", ShamelaFawaaidTamamProcessor.class);
		m_keyToProcessor.put("ibaanah_kubra", ShamelaIbaanahProcessor.class);
		m_keyToProcessor.put("ibnmajah", IbnMajahProcessor.class);
		m_keyToProcessor.put("ibnmajah_no_vowels", ShamelaIbnMajahNoVowelsProcessor.class);
		m_keyToProcessor.put("ibnmajah_vowels", ShamelaIbnMajahVowelledProcessor.class);
		m_keyToProcessor.put("ilm_aml", ShamelaIlmAmlProcessor.class);
		m_keyToProcessor.put("jaami_fadl", ShamelaJaamiFadlProcessor.class);
		m_keyToProcessor.put("jihad", ShamelaJihadProcessor.class);
		m_keyToProcessor.put("laalikaaee", ShamelaLaalikaaeeProcessor.class);
		m_keyToProcessor.put("mishkaat_masaabeeh", ShamelaMishkaatMasaabihProcessor.class);
		m_keyToProcessor.put("muntadhim", ShamelaStandardProcessor.class);
		m_keyToProcessor.put("musnad_yalaa", ShamelaMusnadYalaaProcessor.class);
		m_keyToProcessor.put("mussannaf_abdurazzaq", ShamelaAbdurrazzaaqProcessor.class);
		m_keyToProcessor.put("musannaf_shaybah", ShamelaAbiShaybahProcessor.class);
		m_keyToProcessor.put("musnad_ahmad_indexed", ShamelaMusnadAhmadIndexedProcessor.class);
		m_keyToProcessor.put("musnad_bazzaar", ShamelaBazzaarProcessor.class);
		m_keyToProcessor.put("musnad_shihaab", ShamelaShihaabProcessor.class);
		m_keyToProcessor.put("mustadrak", ShamelaMustadrakProcessor.class);
		m_keyToProcessor.put("nasai", NasaiProcessor.class);
		m_keyToProcessor.put("nasai_kubra", ShamelaNasaiKubraProcessor.class);
		m_keyToProcessor.put("nasai_no_vowels", ShamelaSunanNasaiNoVowelsProcessor.class);
		m_keyToProcessor.put("nasai_vowels", ShamelaSunanNasaiVowelledProcessor.class);
		m_keyToProcessor.put("qasr_amr", ShamelaQasrAmrProcessor.class);
		m_keyToProcessor.put("saheeh_hibbaan", ShamelaHibbanProcessor.class);
		m_keyToProcessor.put("saheeh_irwa", ShamelaIrwaProcessor.class);
		m_keyToProcessor.put("saheeh_jaami", ShamelaJaamiProcessor.class);
		m_keyToProcessor.put("saheeh_khuzayma", ShamelaKhuzaymaProcessor.class);
		m_keyToProcessor.put("saheeh_targheeb", ShamelaTargheebProcessor.class);
		m_keyToProcessor.put("saleh", ShamelaStandardProcessor.class);
		m_keyToProcessor.put("silsila_daif", ShamelaSilsilaDaifProcessor.class);
		m_keyToProcessor.put("sunan_darimi", ShamelaDarimiProcessor.class);
		m_keyToProcessor.put("sunnah_khallal", ShamelaKhallalProcessor.class);
		m_keyToProcessor.put("sunnah_aasim", ShamelaAasimProcessor.class);
		m_keyToProcessor.put("sunnah_hanbal", ShamelaSunnahHanbalProcessor.class);
		m_keyToProcessor.put("tabarani_awsat", ShamelaAwsatProcessor.class);
		m_keyToProcessor.put("tabarani_mujam_kabir", ShamelaTabaraniProcessor.class);
		m_keyToProcessor.put("taarikh_baghdadi", ShamelaTaarikhBaghdadiProcessor.class);
		m_keyToProcessor.put("taleeqat_hisaan", ShamelaTaleeqaatHisaanProcessor.class);
		m_keyToProcessor.put("tanbeeh_ghafileen", ShamelaContinuedProcessor.class);
		m_keyToProcessor.put("tirmidhi", TirmidhiProcessor.class);
		m_keyToProcessor.put("tirmidhi_no_vowels", ShamelaTirmidhiNoVowelsProcessor.class);
		m_keyToProcessor.put("tirmidhi_vowels", ShamelaTirmidhiVowelledProcessor.class);
		m_keyToProcessor.put("zuhd_ahmad", ShamelaStandardProcessor.class);
		m_keyToProcessor.put("zuhd_dawud", ShamelaStandardProcessor.class);
		m_keyToProcessor.put("zuhd_mubarak", ShamelaMubarakZuhdProcessor.class);
	}


	public Collection<String> getKeys() {
		return m_keyToProcessor.keySet();
	}


	public Processor getProcessor(String key) throws InstantiationException, IllegalAccessException
	{
		Class<? extends Processor> c = m_keyToProcessor.get(key);

		if (c == null) {
			System.err.println("NoParserFound: Using standard processor...");
			c = ShamelaStandardProcessor.class;
		}

		return c.newInstance();
	}
}
