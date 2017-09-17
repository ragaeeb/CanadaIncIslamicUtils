package com.canadainc.sunnah10.processors.shamela;

public class ShamelaJaamiFadlProcessor extends AbstractShamelaProcessor
{
	public ShamelaJaamiFadlProcessor()
	{
		m_typos.add(80, "يَةَ.<br /><br /><span class=\"red\">88 - </span>", "يَةَ.<br /><br /><span class=\"red\">89 - </span>");
		m_typos.add(432, "<span class=\"red\">538 - </span>وَرُوِّينَا عَنِ الْخَلِيلِ", "<span class=\"red\">541 - </span>وَرُوِّينَا عَنِ الْخَلِيلِ");
		m_typos.add(599, "774", "773");
		m_typos.add(799, "1126 - ", ShamelaTypoProcessor.decorateContent("1126 -"));
		m_typos.add(1399, "<span class=\"red\">2092 - </span>وَلَقَدْ", "<span class=\"red\">2093 - </span>وَلَقَدْ");
		m_typos.add(1476, "<span class=\"red\">2283 - </span>أَخْبَرَنَا عَبْدُ", "<span class=\"red\">2282 - </span>أَخْبَرَنَا عَبْدُ");
		m_typos.add(1476, "<span class=\"red\">2293 - </span>وَذَكَرَ", "<span class=\"red\">2294 - </span>وَذَكَرَ");
	}
}