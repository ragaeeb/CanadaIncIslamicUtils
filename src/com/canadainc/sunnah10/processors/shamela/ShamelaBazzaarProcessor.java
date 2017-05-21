package com.canadainc.sunnah10.processors.shamela;

import java.util.List;

import org.json.simple.JSONObject;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import com.canadainc.sunnah10.Narration;
import com.canadainc.sunnah10.processors.TypoProcessor;

public class ShamelaBazzaarProcessor extends AbstractShamelaProcessor
{
	private static final String COUPLET_PATTERN = "^\\d+ و\\d+.*$";
	private static final String SUB_PATTERN = "\\d+/\\d{1}$";

	public ShamelaBazzaarProcessor()
	{
		m_typos.prependHadithNumber(218,163);
		m_typos.prependHadithNumber(448,343);
		m_typos.prependHadithNumber(932,757);
		m_typos.prependHadithNumber(1431,1171);
		m_typos.prependHadithNumber(1432,1172);
		m_typos.prependHadithNumber(1434,1173);
		m_typos.prependHadithNumber(1435,1174);
		m_typos.prependHadithNumber(1437,1175);
		m_typos.prependHadithNumber(1439,1176);
		m_typos.prependHadithNumber(1440,1177);
		m_typos.prependHadithNumber(1441,1178);
		m_typos.prependHadithNumber(1443,1179);
		m_typos.prependHadithNumber(1445,1180);
		m_typos.prependHadithNumber(1447,1181);
		m_typos.prependHadithNumber(1448,1182);
		m_typos.prependHadithNumber(1450,1183);
		m_typos.prependHadithNumber(1452,1184);
		m_typos.prependHadithNumber(1455,1185);
		m_typos.prependHadithNumber(1457,1186);
		m_typos.prependHadithNumber(1459,1187);
		m_typos.prependHadithNumber(1460,1188);
		m_typos.prependHadithNumber(1462,1189);
		m_typos.prependHadithNumber(1577,1272);
		m_typos.prependHadithNumber(1684,1342);
		m_typos.prependHadithNumber(1735,1382);
		m_typos.prependHadithNumber(1916,1532);
		m_typos.prependHadithNumber(1996,1594);
		m_typos.prependHadithNumber(2607,2149);
		m_typos.prependHadithNumber(2608,2150);
		m_typos.prependHadithNumber(3628,3197);
		m_typos.prependHadithNumber(3770,3390);
		m_typos.prependHadithNumber(3790,3418);
		m_typos.prependHadithNumber(3943,3600);
		m_typos.prependHadithNumber(3944,3601);
		m_typos.prependHadithNumber(3971,3630);
		m_typos.prependHadithNumber(4208,3861);

		m_typos.add(4668,"4302،","4302-");
		m_typos.add(4674,"3410","4310");
		m_typos.add(4683,"4314","4324");
		m_typos.add(4824,"4489","4479");
		m_typos.add(4939,"4624","4614");
		m_typos.add(4953,"4638","4628");
		m_typos.add(4984,"4660",TypoProcessor.decorateContent("4660-"));
		m_typos.add(5140,"4839","4829");
		m_typos.add(5587,"5365،", TypoProcessor.decorateContent("5365-"));
		m_typos.add(5668,"5475،", TypoProcessor.decorateContent("5475-"));
		m_typos.add(5712,"5546،..،", TypoProcessor.decorateContent("5546-"));
		m_typos.add(5752,"6501","5601");
		m_typos.add(5859,"5671","5761");
		m_typos.add(6212,"6189","6198");
		m_typos.add(6425,"64450","6450");
		m_typos.add(7206,"7280","7270");
		m_typos.add(7207,"7381","7271");
		m_typos.add(7283,"7347","7348");
		m_typos.add(7386,"7481","7471");
		m_typos.add(7726,"7834م", TypoProcessor.decorateContent("7835"));
		m_typos.add(7784,"8900","7900");
		m_typos.add(7819,"7839","7939");
		m_typos.add(7862,"8984","7984");
		m_typos.add(8318,"8464","8468");
		m_typos.add(8393,"8541","8542");
		m_typos.add(8491,"8461","8641");
	}


	@Override
	public void process(List<Node> nodes, JSONObject json)
	{
		Narration n = null;

		boolean allText = ShamelaUtils.isAllText(nodes);
		boolean correctId = Integer.parseInt( json.get("pid").toString() ) > 9412;

		for (Node e: nodes)
		{
			if ( ShamelaUtils.isHadithNumberNode(e) ) {
				n = ShamelaUtils.createNewNarration(n, e, m_narrations);

				if ( correctId && !m_narrations.isEmpty() ) {
					n.hadithNumber = String.valueOf(n.id);
					n.id = m_narrations.get( m_narrations.size()-1 ).id+1;
				}
			} else if ( e.toString().trim().matches(COUPLET_PATTERN) ) {
				String body = ((TextNode)e).text();
				int id = Integer.parseInt( body.split(" ")[0] );
				String hadithNum = body.split("-")[0];
				
				ShamelaUtils.appendIfValid(n, m_narrations);
				n = new Narration(id);
				n.hadithNumber = hadithNum.trim();
				n.text = body.split("-")[1].trim();
			} else if ( ShamelaUtils.isRedRegexNode(e, SUB_PATTERN) ) {
				String body = ShamelaUtils.extractText(e);
				int id = Integer.parseInt( body.split("/")[0] );
				String hadithNum = body.split("-")[0];
				
				ShamelaUtils.appendIfValid(n, m_narrations);
				n = new Narration(id);
				n.hadithNumber = hadithNum.trim();
			} else if ( ShamelaUtils.isTextNode(e) ) {
				String body = ((TextNode)e).text();

				if (n != null) {
					n.text += body;
				} else if ( allText && !m_narrations.isEmpty() ) {
					m_narrations.get( m_narrations.size()-1 ).text += body;
				}
			}
		}

		ShamelaUtils.appendIfValid(n, m_narrations);
	}

	@Override
	public String preprocess(int page, String content)
	{
		if (page == 5890) {
			return content.substring(0, content.indexOf("(1)") );
		}

		return super.preprocess(page, content);
	}
}