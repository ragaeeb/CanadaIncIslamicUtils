package com.canadainc.sunnah10.processors.shamela;

public class ShamelaIlmAmlProcessor extends ShamelaContinuedProcessor
{
	public ShamelaIlmAmlProcessor()
	{
		m_typos.prependHadithNumber(31,27);
		m_typos.prependHadithNumber(34,30);
		m_typos.prependHadithNumber(47,43);
		m_typos.prependHadithNumber(88,89);
		m_typos.add(81,"81","80");
		m_typos.add(81,"قَرَأْتُ", "<br>"+ShamelaTypoProcessor.decorateContent("81 - ")+"قَرَأْتُ");
		m_typos.add(183,"178","171");
		m_typos.prependHadithNumber(184,172);
		m_typos.prependHadithNumber(185,173);
		m_typos.prependHadithNumber(186,174);
		m_typos.prependHadithNumber(187,175);
		m_typos.prependHadithNumber(188,176);
		m_typos.prependHadithNumber(189,177);
		m_typos.prependHadithNumber(190,178);
	}
}