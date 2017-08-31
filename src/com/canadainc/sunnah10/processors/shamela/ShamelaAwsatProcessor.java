package com.canadainc.sunnah10.processors.shamela;

public class ShamelaAwsatProcessor extends ShamelaStandardProcessor
{
	public ShamelaAwsatProcessor()
	{
		m_typos.prependHadithNumber(403,398);
		m_typos.prependHadithNumber(404,399);
		m_typos.prependHadithNumber(501,496);
		m_typos.prependHadithNumber(502,497);
		m_typos.prependHadithNumber(503,498);
		m_typos.prependHadithNumber(504,499);
		m_typos.prependHadithNumber(990,979);
		m_typos.prependHadithNumber(1643,1619);
		m_typos.add(2384, "2308", ShamelaTypoProcessor.decorateContent("2308"));
		m_typos.prependHadithNumber(2575,2489);
		m_typos.add(2758, "2670", ShamelaTypoProcessor.decorateContent("2670"));
		m_typos.prependHadithNumber(2872,2779);
		m_typos.prependHadithNumber(3579,3417);
		m_typos.prependHadithNumber(3621,3459);
		m_typos.add(5668, "5377", ShamelaTypoProcessor.decorateContent("5377"));
		m_typos.prependHadithNumber(8205,7853);
		m_typos.prependHadithNumber(8209,7857);
	}
}