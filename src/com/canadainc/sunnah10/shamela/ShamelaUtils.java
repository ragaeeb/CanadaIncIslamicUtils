package com.canadainc.sunnah10.shamela;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import com.canadainc.sunnah10.Narration;

public class ShamelaUtils
{
	private static final String HADITH_NUM_DELIMITER = "[-\\s]+";
	private static final String HADITH_NUM_REGEX = "\\(\\d+\\)$";


	/**
	 * Returns if this node is hadith number
	 * @param e
	 * @return
	 */
	public static final boolean isHadithNumberNode(Node e)
	{
		if ( isTextSpanNode(e, "red") )
		{
			TextNode tn = (TextNode)e.childNode(0);
			String hadithNum = tn.text().split("-")[0].trim();

			return hadithNum.matches("\\d+$");
		}

		return false;
	}
	
	
	public static final String[] sortLongestToShortest(String...input)
	{
		Arrays.sort(input, new Comparator<String>()
		{
			@Override
			public int compare(String s1, String s2) {
				return s1.length() < s2.length() ? 1 : -1;
			}
		});
		
		return input;
	}
	
	
	public static final Narration createNewNarration(Narration n, Node e, List<Narration> narrations)
	{
		if ( n != null && !n.text.isEmpty() ) { // 2 narrations in 1
			narrations.add(n);
		}
		
		n = new Narration();
		n.id = parseHadithNumber(e);
		n.text = "";
		
		return n;
	}
	


	/**
	 * Returns if this node is hadith number
	 * @param e
	 * @return
	 */
	public static final boolean isHadithRangeNode(Node e)
	{
		if ( isTextSpanNode(e, "red") )
		{
			TextNode tn = (TextNode)e.childNode(0);
			return tn.text().matches("\\d+ - \\d+$");
		}

		return false;
	}


	/**
	 * Returns if this node is hadith number
	 * @param e
	 * @return
	 */
	public static final boolean isRoundHadithNumNode(Node e)
	{
		if ( isTitleSpan(e) )
		{
			TextNode tn = (TextNode)e.childNode(0);
			String hadithNum = tn.text().split(HADITH_NUM_DELIMITER)[0].trim();
			return hadithNum.matches(HADITH_NUM_REGEX);
		} else if ( isTitleSpanNode(e, "title") && (e.childNodeSize() > 0) && isTitleSpan( e.childNode(0) ) ) {
			return isRoundHadithNumNode( e.childNode(0) );
		}

		return false;
	}


	public static boolean isRoundHadithNumText(Node e)
	{
		TextNode tn = getTextNode(e);
		String hadithNum = tn.text().split("-")[0].trim();
		return hadithNum.matches(HADITH_NUM_REGEX);
	}


	public static final boolean isTitleSpanNode(Node e, String attribute) {
		return e.nodeName().equals("span") && e.attr("class").equals(attribute);
	}


	public static final boolean isTextSpanNode(Node e, String attribute) {
		return isTitleSpanNode(e, attribute) && ( e.childNode(0) instanceof TextNode );
	}


	/**
	 * 
	 * @param e
	 * @return
	 */
	public static final boolean isTextNode(Node e) {
		return e.nodeName().equals("#text");
	}


	public static final boolean isTitleSpan(Node e) {
		return isTextSpanNode(e, "title");
	}


	public static final boolean isFootnote(Node e) {
		return isTextSpanNode(e, "footnote");
	}


	public static final String extractText(Node e)
	{
		StringBuilder sb = new StringBuilder();
		
		for (int i = 0; i < e.childNodeSize(); i++)
		{
			Node n = e.childNode(i);

			while (!(n instanceof TextNode)) {
				n = e.childNode(0);
			}

			TextNode tn = (TextNode)n;
			sb.append( tn.text() );
		}

		return sb.toString();
	}


	public static final int parseHadithNumber(Node e)
	{
		String hadithNum = getTextNode(e).text().split(HADITH_NUM_DELIMITER)[0].trim();
		return Integer.parseInt(hadithNum);
	}
	
	
	public static final boolean isHadithNumberValid(Node e, List<Narration> narrations)
	{
		int current = ShamelaUtils.parseHadithNumber(e);
		return narrations.isEmpty() || ( narrations.get( narrations.size()-1 ).id <= current );
	}


	public static final int parseRoundHadithNumber(Node e)
	{
		String hadithNum = getTextNode(e).text().split(HADITH_NUM_DELIMITER)[0].trim().replaceAll("[\\(\\)]+", "");
		return Integer.parseInt(hadithNum);
	}


	public static final String extractRoundHadith(Node e)
	{
		String body = getTextNode(e).text().trim();
		return body.substring( body.indexOf("-")+1 ).trim();
	}


	private static TextNode getTextNode(Node e)
	{
		while (!(e instanceof TextNode)) {
			e = e.childNode(0);
		}

		TextNode tn = (TextNode)e;
		return tn;
	}


	public static final void assertSequential(List<Narration> narrations, boolean id)
	{
		int n = narrations.size();

		if (n > 1)
		{
			Narration secondLast = narrations.get(n-2);
			Narration last = narrations.get(n-1);

			if ( id && (last.id-secondLast.id != 1) ) {
				throw new RuntimeException("IdDiff(secondLast, last): "+secondLast.id+", "+last.id);
			} else if ( !id && (last.inBookNumber-secondLast.inBookNumber != 1) ) {
				throw new RuntimeException("IdDiff(secondLast, last): "+secondLast.inBookNumber+", "+last.inBookNumber);
			} else if (last.grading == null) {
				throw new RuntimeException("NoGrade(last): "+last.id);
			}
		}
	}
}