package com.canadainc.sunnah10.processors.shamela;

public class ShamelaKhallalProcessor extends ShamelaContinuedProcessor
{
	public ShamelaKhallalProcessor()
	{
		m_typos.prependHadithNumber(61,56);
		m_typos.add(280, "250 -<br /><br />251 - ", "251 - ");
		m_typos.prependHadithNumber(288,258);
		m_typos.prependHadithNumber(296,266);
		m_typos.prependHadithNumber(299,269);
		m_typos.add(300, "269 -<br /><br />270 -", "270 -");
		m_typos.prependHadithNumber(362,339);
		m_typos.prependHadithNumber(460,433);
		m_typos.prependHadithNumber(461,434);
		m_typos.prependHadithNumber(912,872);
		m_typos.prependHadithNumber(1086,1030);
	}
}
