package com.canadainc.sunnah10.processors.shamela.dunya;

import com.canadainc.sunnah10.processors.shamela.ShamelaContinuedProcessor;

public class DunyaWaraProcessor extends ShamelaContinuedProcessor
{
	public DunyaWaraProcessor()
	{
		m_typos.prependHadithNumber(13,143);
		m_typos.add(13, "46", "13");

		for (int i = 14; i < 47; i++) {
			m_typos.prependHadithNumber(i,i);
		}
		
		m_typos.prependHadithNumber(78,77);
	}
}