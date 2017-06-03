package com.canadainc.sunnah10.processors.sunnah.com;

import org.apache.commons.lang3.ArrayUtils;
import org.json.simple.JSONObject;

import com.canadainc.sunnah10.Narration;

public class IbnMajahProcessor extends AbstractSunnahDotComProcessor
{
	public IbnMajahProcessor()
	{
		m_typos.ignore(1258635,1262090,1293310);
		
		m_typos.fixHadithNumber(1251710, 172);
		m_typos.fixHadithNumber(1256410, 642);
		m_typos.fixHadithNumber(1259530, 3808);
		m_typos.fixHadithNumber(1262050, 1887);
		m_typos.fixHadithNumber(1262060, 1888);
		m_typos.fixHadithNumber(1262070, 1889);
		m_typos.fixHadithNumber(1262080, 1890);
		m_typos.fixHadithNumber(1262480, 1929);
		m_typos.fixHadithNumber(1262840, 1965);
		m_typos.fixHadithNumber(1263830, 2064);
		m_typos.fixHadithNumber(1263950, 2076);
		m_typos.fixHadithNumber(1263960, 2077);
		m_typos.fixHadithNumber(1266400, 2322);
		m_typos.fixHadithNumber(1293390, 4236);
		
		// ibn majah
		m_typos.decompose(1265570, 2238, "It was narrated from Abu Hurairah");
		
		m_typos.merge(1290300);
		m_typos.merge(1291885);
	}
	
	
	@Override
	public void process(Narration n)
	{
		m_typos.track( Integer.parseInt(n.hadithNumber), m_narrations.size() );
		m_narrations.add(n);
	}
	
	
	@Override
	public boolean hasGrade(int id) {
		return true;
	}
}