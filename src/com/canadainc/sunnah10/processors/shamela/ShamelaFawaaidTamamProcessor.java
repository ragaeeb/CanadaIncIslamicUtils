package com.canadainc.sunnah10.processors.shamela;

public class ShamelaFawaaidTamamProcessor extends ShamelaStandardProcessor
{
	public ShamelaFawaaidTamamProcessor()
	{
		m_typos.prependHadithNumber(260,273);
		m_typos.prependHadithNumber(271,285);
		m_typos.prependHadithNumber(272,286);
		m_typos.prependHadithNumber(275,289);
		m_typos.prependHadithNumber(365,386);
		m_typos.prependHadithNumber(378,409);
		m_typos.prependHadithNumber(419,451);
		m_typos.prependHadithNumber(484,526);
		m_typos.prependHadithNumber(742,794);
		m_typos.add(894, "966", ShamelaTypoProcessor.decorateContent("966"));
		m_typos.prependHadithNumber(935,1013);
		m_typos.prependHadithNumber(1020,1103);
		m_typos.prependHadithNumber(1118,1207);
		m_typos.prependHadithNumber(1127,1220);
		m_typos.add(1336, "1440", "1439");
		m_typos.prependHadithNumber(1337,1440);
		m_typos.prependHadithNumber(1401,1512);
		m_typos.prependHadithNumber(1402,1513);
		m_typos.prependHadithNumber(1519,1632);
		m_typos.prependHadithNumber(1546,1661);
	}
}
