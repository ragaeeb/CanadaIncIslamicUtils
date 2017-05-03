package com.canadainc.sunnah10.shamela;

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


	public static final String extractText(Node e) {
		return getTextNode(e).text();
	}


	public static final int parseHadithNumber(Node e)
	{
		String hadithNum = getTextNode(e).text().split(HADITH_NUM_DELIMITER)[0].trim();
		return Integer.parseInt(hadithNum);
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
			}
		}
	}
}