package com.canadainc.sunnah10.shamela;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import com.canadainc.sunnah10.Narration;

public class ShamelaAwaanahProcessor implements ShamelaProcessor
{
	private ArrayList<Narration> m_narrations = new ArrayList<>();
	private TypoProcessor m_typos = new TypoProcessor();

	public ShamelaAwaanahProcessor()
	{
		m_typos.prependHadithNumber(10,10);
		m_typos.prependHadithNumber(49,58);
		m_typos.prependHadithNumber(51,60);
		m_typos.prependHadithNumber(245,295);
		m_typos.prependHadithNumber(446,522);
		m_typos.prependHadithNumber(464,538);
		m_typos.prependHadithNumber(906,1008);
		m_typos.prependHadithNumber(1030,1141);
		m_typos.prependHadithNumber(1045,1154);
		m_typos.prependHadithNumber(1275,1397);
		m_typos.prependHadithNumber(1358,1480);
		m_typos.prependHadithNumber(1461,1591);
		m_typos.prependHadithNumber(1466,1595);
		m_typos.prependHadithNumber(1563,1710);
		m_typos.prependHadithNumber(1676,1838);
		m_typos.prependHadithNumber(1677,1829);
		m_typos.prependHadithNumber(1678,1830);
		m_typos.prependHadithNumber(1679,1831);
		m_typos.prependHadithNumber(1703,1862);
		m_typos.prependHadithNumber(1706,1865);
		m_typos.prependHadithNumber(2382,2540);
		m_typos.prependHadithNumber(2442,2599);
		m_typos.prependHadithNumber(2613,2794);
		m_typos.prependHadithNumber(2652,2834);
		m_typos.prependHadithNumber(2779,2967);
		m_typos.prependHadithNumber(2873,3068);
		m_typos.prependHadithNumber(2941,3139);
		m_typos.prependHadithNumber(3205,3404);
		m_typos.prependHadithNumber(3371,3557);
		m_typos.prependHadithNumber(3432,3624);
		m_typos.prependHadithNumber(3497,3692);
		m_typos.prependHadithNumber(3631,3844);
		m_typos.prependHadithNumber(3773,4002);
		m_typos.prependHadithNumber(4947,5243);
		m_typos.prependHadithNumber(4948,5244);
		m_typos.prependHadithNumber(5116,5431);
		m_typos.prependHadithNumber(5627,6006);
		m_typos.prependHadithNumber(5711,6092);
		m_typos.prependHadithNumber(5760,6157);
		m_typos.prependHadithNumber(5761,6158);
		m_typos.prependHadithNumber(5763,6159);
		m_typos.prependHadithNumber(5764,6160);
		m_typos.prependHadithNumber(5765,6161);
		m_typos.prependHadithNumber(5766,6162);
		m_typos.prependHadithNumber(5767,6163);
		m_typos.prependHadithNumber(5768,6164);
		m_typos.prependHadithNumber(5770,6165);
		m_typos.prependHadithNumber(5771,6166);
		m_typos.prependHadithNumber(5772,6167);
		m_typos.prependHadithNumber(5773,6168);
		m_typos.prependHadithNumber(5774,6169);
		m_typos.prependHadithNumber(5952,6375);
		m_typos.prependHadithNumber(5953,6376);
		m_typos.prependHadithNumber(5992,6416);
		m_typos.prependHadithNumber(6287,6770);
		m_typos.prependHadithNumber(6323,6810);
		m_typos.prependHadithNumber(6324,6811);
		m_typos.prependHadithNumber(6335,6821);
		m_typos.prependHadithNumber(6482,6966);
		m_typos.prependHadithNumber(6599,7088);
		m_typos.prependHadithNumber(6710,7214);
		m_typos.prependHadithNumber(6784,7288);
		m_typos.prependHadithNumber(6888,7398);
		m_typos.prependHadithNumber(7150,8094);
		m_typos.prependHadithNumber(7331,7888);
		m_typos.prependHadithNumber(7577,8169);
		m_typos.prependHadithNumber(7921,8543);
	}

	@Override
	public void process(List<Node> nodes, JSONObject json)
	{
		Narration n = null;

		for (Node e: nodes)
		{
			if ( ShamelaUtils.isHadithNumberNode(e) )
			{
				n = ShamelaUtils.createNewNarration(n, e, m_narrations);
			} else if ( ShamelaUtils.isTextNode(e) && (n != null) ) {
				String body = ((TextNode)e).text();
				n.text += body;
			} else if ( ShamelaUtils.isTitleSpan(e) && (n != null) ) {
				n.text += ShamelaUtils.extractText(e);
			} 
		}
		
		if (n != null) {
			m_narrations.add(n);
		}
	}

	@Override
	public boolean preprocess(JSONObject json)
	{
		m_typos.process(json);
		return true;
	}

	@Override
	public List<Narration> getNarrations() {
		return m_narrations;
	}

	@Override
	public boolean hasGrade(int id) {
		return false;
	}
}