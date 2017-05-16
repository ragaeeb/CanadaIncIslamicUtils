package com.canadainc.sunnah10.processors.shamela.albaani;

import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.json.simple.JSONObject;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import com.canadainc.sunnah10.Narration;
import com.canadainc.sunnah10.processors.shamela.AbstractShamelaProcessor;
import com.canadainc.sunnah10.processors.shamela.ShamelaUtils;

public class ShamelaJaamiProcessor extends AbstractShamelaProcessor
{
	private static final String[] GRADES = ShamelaUtils.sortLongestToShortest("صحيح", "حسن", "ضعيف", "أقرب للضعف", "الصحيح");
	private static final int[] IGNORED_PAGES = new int[]{608};

	public ShamelaJaamiProcessor()
	{
		m_typos.add(5, "صحيح1", "صحيح");
		m_typos.add(98, "569_", "569 ");
		m_typos.add(182, "- 5071079", "1079 - 507");
		m_typos.add(398, "23201", "2320");
		m_typos.add(1189, "7562*", "7562");
		m_typos.add(801, "(ك) عن سهل بن سعد", "(صحيح). (ك) عن سهل بن سعد");
		m_typos.add(1017, "عباس.", "عباس.<br />(صحيح)");
	}

	@Override
	public void process(List<Node> nodes, JSONObject json)
	{
		Narration n = null;

		for (Node e: nodes)
		{
			if ( ShamelaUtils.isHadithNumberNode(e) ) {
				n = ShamelaUtils.createNewNarration(n, e, m_narrations);
			} else if ( ShamelaUtils.isTextNode(e) ) {
				String commentary = ((TextNode)e).text();

				if ( getValidHadithNumber(commentary) != -1 )
				{
					if ( n != null && !n.text.isEmpty() ) { // 2 narrations in 1
						m_narrations.add(n);
					}

					int firstSpace = commentary.indexOf(" ");
					firstSpace = commentary.indexOf(" ", firstSpace);

					n = new Narration();
					n.id = getValidHadithNumber(commentary);
					n.text = commentary.substring(firstSpace+1).trim();
				} else if (n != null) {
					attachCommentary(commentary, n);
				} else if ( !m_narrations.isEmpty() ) {
					attachCommentaryToPrev(commentary);
				}
			}
		}

		ShamelaUtils.appendIfValid(n, m_narrations);
	}


	private int getValidHadithNumber(String commentary)
	{
		String[] tokens = commentary.split(" ");

		if (tokens.length > 0)
		{
			commentary =  commentary.split(" ")[0].trim();

			if ( commentary.matches("\\d+$") )
			{
				int current = Integer.parseInt(commentary);
				return m_narrations.isEmpty() || ( m_narrations.get( m_narrations.size()-1 ).id <= current ) ? current : -1;
			}
		}

		return -1;
	}


	private void attachCommentaryToPrev(String body)
	{
		Narration n = m_narrations.get( m_narrations.size()-1 );
		attachCommentary(body, n);
	}


	private String bodyStartsWithGrade(String body)
	{
		for (String grade: GRADES)
		{
			if ( body.startsWith("("+grade) || body.trim().equals(grade) || body.startsWith(grade) || body.startsWith("\""+grade) || body.startsWith("["+grade) ) {
				return grade;
			}
		}

		return null;
	}


	private void attachCommentary(String body, Narration n)
	{
		if (n.grading == null && ( bodyStartsWithGrade( body.trim() ) != null ) ) {
			n.grading = bodyStartsWithGrade( body.trim() );

			if ( n.grading.endsWith(".") ) {
				n.grading = n.grading.substring(0, n.grading.length()-1);
			}

			n.commentary += body.substring( body.indexOf("")+1 ).trim();
		} else if (n.grading != null) {
			n.commentary += body;
		} else {
			n.text += body;
		}
	}


	@Override
	public String preprocess(int page, String content)
	{
		if ( ArrayUtils.indexOf(IGNORED_PAGES, page) != -1 ) {
			return null;
		}

		return super.preprocess(page, content);
	}

	@Override
	public boolean hasGrade(int id) {
		return true;
	}
}
