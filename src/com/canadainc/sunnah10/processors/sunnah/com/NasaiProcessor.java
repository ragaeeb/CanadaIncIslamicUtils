package com.canadainc.sunnah10.processors.sunnah.com;

import com.canadainc.sunnah10.Narration;

public class NasaiProcessor extends AbstractSunnahDotComProcessor
{
	public NasaiProcessor()
	{
		m_typos.ignore(1002090, 1004370, 1037350);
		
		/* Sunan an-Nasai */
		m_typos.fixHadithNumber(1002160, 215);
		m_typos.fixHadithNumber(1076740, 2497);
		m_typos.fixHadithNumber(1078600, 2687);
		m_typos.fixHadithNumber(1072920, 2111);
		m_typos.fixHadithNumber(1073140, 2134);

		// should this be 3052 or 3055?
		m_typos.fixHadithNumber(1082165, 3055);

		m_typos.fixHadithNumber(1084745, 4422);
		m_typos.fixHadithNumber(1085350, 4545);
		m_typos.fixHadithNumber(1086075, 4689);
		m_typos.fixHadithNumber(1086330, 4740);
		m_typos.fixHadithNumber(1086660, 4806);
		m_typos.fixHadithNumber(1086915, 4859);
		
		m_typos.fixRange(1002100, 1003540, -1);

		// BIG RANGE
		m_typos.fixRange(1004380, 1070770, -1, 1068370);
		m_typos.fixRange(1070810, 1071100, -1);
		m_typos.fixRange(1034000, 1034330, -29);
		m_typos.fixRange(1034340, 1037340, -30);
		m_typos.fixRange(1037348, 1039330, -31);
		m_typos.fixRange(1039350, 1039530, -32);
		m_typos.fixRange(1039540, 1039800, 548);
		m_typos.fixRange(1071120, 1076980, -2);
		m_typos.fixRange(1076985, 1079380, -1);
		m_typos.fixRange(1079390, 1079880, -2);
		m_typos.fixRange(1079900, 1082165, -3);
		m_typos.fixRange(1082170, 1082480, -2);
		m_typos.fixRange(1030960, 1033990, -2);
		m_typos.fixRange(1082500, 1085145, -5);
		m_typos.fixRange(1085148, 1087315, -4);
		m_typos.fixRange(1087325, 1088725, -3);
		m_typos.fixRange(1088730, 1090435, -2);
		m_typos.fixRange(1090445, 1091410, -3);

		m_typos.decompose(1087320, 4938, "And he (the narrator)");

		m_typos.merge(1003860);
		m_typos.merge(1005135);
		m_typos.merge(1035440);
		m_typos.merge(1035450);
		m_typos.merge(1064940);
		m_typos.merge(1064960);
		m_typos.merge(1065000);
		m_typos.merge(1090440, 5564);
		m_typos.merge(1039340, 3919);
		m_typos.merge(1071110, 1928);
		m_typos.merge(1079890, 2821);
		m_typos.merge(1090440, 5564);

		// merge all lettered
		m_typos.merge(1039340, 3919);
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