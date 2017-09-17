package com.canadainc.sunnah10.processors.shamela.dunya;

import com.canadainc.sunnah10.processors.shamela.ShamelaContinuedProcessor;

public class DunyaIyaalProcessor extends ShamelaContinuedProcessor
{
	public DunyaIyaalProcessor()
	{
		m_typos.prependHadithNumber(2,1);
		m_typos.prependHadithNumber(402,390);
		m_typos.prependHadithNumber(437,423);
		m_typos.prependHadithNumber(457,443);
		m_typos.prependHadithNumber(490,475);
		m_typos.prependHadithNumber(530,514);
		m_typos.prependHadithNumber(660,636);
		m_typos.clearAfter(701, "الْغُلَامِ");
	}
}