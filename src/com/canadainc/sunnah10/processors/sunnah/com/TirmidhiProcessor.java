package com.canadainc.sunnah10.processors.sunnah.com;

import com.canadainc.sunnah10.Narration;

public class TirmidhiProcessor extends AbstractSunnahDotComProcessor
{
	public TirmidhiProcessor()
	{
		m_typos.fixHadithNumber(664570, 459);
		m_typos.fixHadithNumber(664581, 460);
		m_typos.fixHadithNumber(643100, 3273);
		m_typos.fixHadithNumber(664380, 439);
		m_typos.fixHadithNumber(667330, 734);
		m_typos.fixHadithNumber(616610, 1571);
		m_typos.fixHadithNumber(673020, 1999);
		m_typos.fixHadithNumber(673400, 2037);
		m_typos.fixHadithNumber(673560, 2053);
		m_typos.fixHadithNumber(673760, 2073);
		m_typos.fixHadithNumber(674020, 2099);
		m_typos.fixHadithNumber(674050, 2102);
		m_typos.fixHadithNumber(674860, 2182);
		m_typos.fixHadithNumber(677290, 2424);
		m_typos.fixHadithNumber(677390, 2434);
		m_typos.fixHadithNumber(639350, 2953);
		m_typos.fixHadithNumber(639370, 2953);
		m_typos.fixHadithNumber(639380, 2954);
		m_typos.fixHadithNumber(639650, 2973);
		m_typos.fixHadithNumber(637860, 3898);
		m_typos.fixHadithNumber(636090, 3730);
		m_typos.fixHadithNumber(636100, 3731);
		m_typos.fixHadithNumber(680790, 3368);
		
		m_typos.decompose(673911, "Al-Hasan said");
	}


	/* (non-Javadoc)
	 * @see com.canadainc.sunnah10.processors.sunnah.com.AbstractSunnahDotComProcessor#process(com.canadainc.sunnah10.Narration)
	 */
	@Override
	public void process(Narration n)
	{
		int hadithNumber = Integer.parseInt(n.hadithNumber);
		
		if (hadithNumber > 0)
		{
			int index = m_typos.getIndex(hadithNumber);
			
			if (index >= 0) { // it already exists, merge it
				Narration other = m_narrations.get(index);
				other.text += " "+n.text;
			} else {
				m_typos.track(hadithNumber, m_narrations.size());
				super.process(n);
			}
		}
	}


	@Override
	public boolean hasGrade(int id) {
		return true;
	}
}