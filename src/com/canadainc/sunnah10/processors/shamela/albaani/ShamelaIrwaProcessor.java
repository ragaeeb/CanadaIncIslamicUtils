package com.canadainc.sunnah10.processors.shamela.albaani;

import java.util.List;

import org.json.simple.JSONObject;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import com.canadainc.sunnah10.Narration;
import com.canadainc.sunnah10.processors.shamela.AbstractShamelaProcessor;
import com.canadainc.sunnah10.processors.shamela.ShamelaUtils;

public class ShamelaIrwaProcessor extends AbstractShamelaProcessor
{
	public ShamelaIrwaProcessor()
	{
		m_typos.add(1742,"1414) - ", "(1414) - ");
		m_typos.add(1942,"(1566) - ", "(1621) - ");
		m_typos.add(2316,"(2015) - ", "(2014) - ");
		m_typos.add(2395,"(2072) - ", "(2073) - ");
		m_typos.add(2327,"02023) -","(2023) -");
		m_typos.add(2549,"(2555) - ", "(2255) - ");
		m_typos.add(2680,"(2317) - ", "(2417) - ");
		m_typos.add(2208,"قال عمر رضى الله عنه:", "(1913) - قال عمر رضى الله عنه:");
		m_typos.add(248,"(338) - ", "(238) - ");
		m_typos.add(406,"364) - ", "(364) - ");
		m_typos.add(2664,"قلت: وهذا سند", "* ضعيف<br />قلت: وهذا سند");
		m_typos.add(2577,"هو فى", "* معضل<br />هو فى");
	}

	@Override
	public void process(List<Node> nodes, JSONObject json)
	{
		Narration n = null;
		int nodeIndex = -1;

		for (Node e: nodes)
		{
			++nodeIndex;
			if ( e.toString().startsWith("باب") && nodeIndex == 0 ) { // skip chapter headings
				continue;
			}

			if ( ShamelaUtils.isRoundHadithNumNode(e) || ( ShamelaUtils.isTextNode(e) && ShamelaUtils.isRoundHadithNumText(e) && ( isNextAfterPrev(e) || isNextAfter(e,n) ) ) )
			{
				if (n != null) {
					m_narrations.add(n);
				}

				n = new Narration();
				n.id = ShamelaUtils.parseRoundHadithNumber(e);
				n.text = ShamelaUtils.extractRoundHadith(e);

				int starIndex = n.text.lastIndexOf("*");
				int endIndex = n.text.length();
				if ( (starIndex != -1) && starIndex > endIndex-10 ) { // near the end
					n.grading = n.text.substring(starIndex+1, n.text.endsWith(".") ? endIndex -1 : endIndex).trim();
					n.text = n.text.substring(0, starIndex-1).trim();
				}
			} else if (n != null) {
				if ( ShamelaUtils.isTextNode(e) ) {
					String body = ((TextNode)e).text();
					addGradeOrComment(n, body);
				} else if ( ShamelaUtils.isTitleSpan(e) ) {
					n.text += ShamelaUtils.extractText(e);
				} else if ( ShamelaUtils.isFootnote(e) ) {
					n.commentary += ShamelaUtils.extractText(e);
				}
			} else if ( shouldGoInPrev(n) && ShamelaUtils.isTextNode(e) ) {
				attachCommentaryToPrev( ((TextNode)e).text() );
			} else if ( shouldGoInPrev(n) && ShamelaUtils.isFootnote(e) ) {
				attachCommentaryToPrev( ShamelaUtils.extractText(e) );
			}
		}

		ShamelaUtils.appendIfValid(n, m_narrations);
	}

	private void addGradeOrComment(Narration n, String body)
	{
		if ( body.startsWith("*") || body.startsWith("حسن") || body.startsWith("صحيح") ) {
			int endIndex = body.length();
			n.grading = body.substring(1, body.endsWith(".") ? endIndex-1 : endIndex).trim(); // remove the period
		} else if (n.grading != null) {
			n.commentary += body;
		} else {
			n.text += body;
		}
	}

	private boolean isNextAfterPrev(Node e) {
		return !m_narrations.isEmpty() && ( m_narrations.get( m_narrations.size()-1 ).id == ShamelaUtils.parseRoundHadithNumber(e)-1 );
	}


	private boolean isNextAfter(Node e, Narration n) {
		return (n != null) && (n.id == ShamelaUtils.parseRoundHadithNumber(e)-1);
	}


	private void attachCommentaryToPrev(String commentary)
	{
		Narration narration = m_narrations.get( m_narrations.size()-1 );
		addGradeOrComment(narration, commentary);
	}


	private boolean shouldGoInPrev(Narration n) {
		return (n == null) && !m_narrations.isEmpty();
	}

	@Override
	public boolean hasGrade(int id) {
		return true;
	}
}