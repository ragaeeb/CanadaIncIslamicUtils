package com.canadainc.sunnah10.processors.shamela;

public class ShamelaBayhaqiEemaanProcessor extends AbstractShamelaProcessor
{
	public ShamelaBayhaqiEemaanProcessor()
	{
		m_typos.prependHadithNumber(96,79);
		m_typos.prependHadithNumber(482,363);
		m_typos.prependHadithNumber(499,372);
		m_typos.add(562, "223", "423");
		m_typos.add(562, "224", "424");
		m_typos.add(1496, "1219", "1319");
		m_typos.add(1460, "<br /><br /><span class=\"red\">1283", "<br /><br /><span class=\"red\">1284");
		m_typos.add(1512, "1336", "1335");
		m_typos.add(1513, "1337", "1336");
		m_typos.prependHadithNumber(1514,1337);
		m_typos.add(1553, "1372", "1371");
		m_typos.prependHadithNumber(1555,1372);
		m_typos.add(1699, "1490", "1489");
		m_typos.prependHadithNumber(1700,1490);
		m_typos.prependHadithNumber(2005,1762);
		m_typos.prependHadithNumber(2214,1936);
		m_typos.prependHadithNumber(3931,3541);
		m_typos.add(3939, "3550", "3549");
		m_typos.prependHadithNumber(4204,3784);
		m_typos.add(4321, "3999", "3899");
		m_typos.add(4384, "5961", "3961");
		m_typos.prependHadithNumber(4617,4180);
		m_typos.add(5893, "5431", ShamelaTypoProcessor.decorateContent("5431"));
		m_typos.prependHadithNumber(5951,5481);
		m_typos.add(5951, "5481", "5482");
		m_typos.add(6030, "5553", "5552");
		m_typos.prependHadithNumber(6031,5553);
		m_typos.add(6239, "5744", "5743");
		m_typos.prependHadithNumber(6240,5744);
		m_typos.add(7029, "6486", "6487");
		m_typos.add(7034, "6491", "6493");
		m_typos.prependHadithNumber(7652,7099);
		m_typos.add(7793, "7236", "7237");
		m_typos.prependHadithNumber(8394,7840);
		m_typos.add(8615, "<span class=\"red\">8063 - </span>أَخْبَرَنَا أَبُو عُثْمَانَ", "<span class=\"red\">8061 - </span>أَخْبَرَنَا أَبُو عُثْمَانَ");
		m_typos.add(9024, "8466وَهَذَا", ShamelaTypoProcessor.decorateContent("8466")+"وَهَذَا");
		m_typos.prependHadithNumber(9034,8474);
		m_typos.add(9035, "8474", "8475");
		m_typos.prependHadithNumber(9234,8649);
		m_typos.add(9581, "8976", "8977");
		m_typos.add(10340, "9735", "9736");
		m_typos.add(10341, "9736", "9737");
		m_typos.prependHadithNumber(10622,10046);
	}
}