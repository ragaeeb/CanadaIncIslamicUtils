package com.canadainc.sunnah10.processors.shamela;

public class ShamelaMusnadAhmadIndexedProcessor extends ShamelaStandardProcessor
{
	public ShamelaMusnadAhmadIndexedProcessor()
	{
		int i = 15;
		m_typos.prependHadithNumber(19,++i);
		m_typos.prependHadithNumber(21,++i);
		m_typos.prependHadithNumber(23,++i);

		for (int j = 26; j < 41; j++) {
			m_typos.prependHadithNumber(26,++i);
			m_typos.prependHadithNumber(27,++i);
			m_typos.prependHadithNumber(28,++i);
			m_typos.prependHadithNumber(29,++i);
			m_typos.prependHadithNumber(30,++i);
			m_typos.prependHadithNumber(31,++i);
			m_typos.prependHadithNumber(32,++i);
			m_typos.prependHadithNumber(33,++i);
			m_typos.prependHadithNumber(34,++i);
		}

		m_typos.add(3205, "3276 م -", ShamelaTypoProcessor.decorateContent("3276"));
		m_typos.prependHadithNumber(3206,3277);
		m_typos.prependHadithNumber(5505,5632);
		m_typos.add(3205, "«7343 -", ShamelaTypoProcessor.decorateContent("7343"));
		m_typos.prependHadithNumber(8415,8571);
		m_typos.prependHadithNumber(8974,9126);
		m_typos.add(9988, "«10152 -", ShamelaTypoProcessor.decorateContent("10152"));
		m_typos.prependHadithNumber(10840,11020);
		m_typos.prependHadithNumber(10894,11073);
		m_typos.prependHadithNumber(10895,11073);
		m_typos.prependHadithNumber(11044,11230);
		m_typos.prependHadithNumber(11419,11593);
		m_typos.prependHadithNumber(11420,11593);
		m_typos.prependHadithNumber(11615,11781);
		m_typos.prependHadithNumber(11616,11781);
		m_typos.prependHadithNumber(13348,13549);
		m_typos.prependHadithNumber(15104,15329);
		m_typos.prependHadithNumber(15183,15403);
		m_typos.prependHadithNumber(15196,15414);
		m_typos.prependHadithNumber(15197,15414);
		m_typos.prependHadithNumber(15302,15482);
		m_typos.prependHadithNumber(15540,15666);
		m_typos.prependHadithNumber(15785,15863);
		m_typos.prependHadithNumber(15786,15863);
		m_typos.prependHadithNumber(16101,16100);
		m_typos.prependHadithNumber(16371,16346);
		m_typos.prependHadithNumber(16563,16513);
		m_typos.prependHadithNumber(16564,16513);
		m_typos.prependHadithNumber(16613,16557);
		m_typos.prependHadithNumber(16614,16557);
		m_typos.prependHadithNumber(18325,18028);
		m_typos.add(18895, "18561", ShamelaTypoProcessor.decorateContent("18560"));
		m_typos.prependHadithNumber(18896,18561);
		m_typos.add(20673,"0<br /><br />", "");
		m_typos.prependHadithNumber(23213,22793);
		m_typos.prependHadithNumber(23418,23000);
		m_typos.prependConditionalIndex(24504, 24625, 24009, "حَدَّثَنَا");
		m_typos.highlight(25075,24459);
		m_typos.prependHadithNumber(26356,25754);
		m_typos.prependHadithNumber(28209,27517);
	}
}