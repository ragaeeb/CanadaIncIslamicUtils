package com.canadainc.sunnah10.processors.sunnah.com;

import com.canadainc.sunnah10.Narration;

public class BukhariProcessor extends AbstractSunnahDotComProcessor
{
	public BukhariProcessor()
	{
		m_typos.ignore(53580,149181,54160,149792);
	}

	/* (non-Javadoc)
	 * @see com.canadainc.sunnah10.processors.sunnah.com.AbstractSunnahDotComProcessor#process(com.canadainc.sunnah10.Narration)
	 */
	@Override
	public void process(Narration n)
	{
		if ( n.hadithNumber.contains(",") ) {
			n.hadithNumber = n.hadithNumber.split(",")[0];
		}

		m_narrations.add(n);
	}
}