package com.canadainc.sunnah10.processors.shamela;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import com.canadainc.sunnah10.Narration;

public class ShamelaUtils
{
	public static final String HADITH_NUM_DELIMITER = "[-\\s]+";
	private static final String HADITH_NUM_REGEX = "\\(\\d+\\)$";


	/**
	 * Returns if this node is hadith number
	 * @param e
	 * @return
	 */
	public static final boolean isHadithNumberNode(Node e) {
		return isRedRegexNode(e, "\\d+$");
	}


	/**
	 * Returns if this node is hadith number
	 * @param e
	 * @return
	 */
	public static final boolean isRedRegexNode(Node e, String regex)
	{
		if ( isTextSpanNode(e, "red") )
		{
			TextNode tn = (TextNode)e.childNode(0);
			String hadithNum = tn.text().split("-")[0].trim();
			return hadithNum.matches(regex);
		}

		return false;
	}


	public static boolean isArabicText(String text) {
		for (char charac : text.toCharArray()) {
			if (Character.UnicodeBlock.of(charac) == Character.UnicodeBlock.ARABIC) {
				return true;
			}
		}
		return false;
	}


	public static final boolean isLineBreak(Node e) {
		return e.nodeName().equals("br");
	}


	public static final boolean isAllText(List<Node> nodes)
	{
		for (Node e: nodes)
		{
			if ( !isTextNode(e) && !isLineBreak(e) ) {
				return false;
			}
		}

		return true;
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


	public static final Narration createNewNarration(Narration n, Node e, List<Narration> narrations) {
		return createNewNarration(n, parseHadithNumber(e), narrations);
	}


	public static final Narration createNewNarration(Narration n, int hadithNumber, List<Narration> narrations)
	{
		appendIfValid(n, narrations);

		n = new Narration();
		n.id = hadithNumber;
		n.text = "";

		return n;
	}


	public static final boolean appendIfValid(Narration n, List<Narration> narrations)
	{
		if ( n != null && !n.text.isEmpty() && isArabicText(n.text) ) { // 2 narrations in 1
			narrations.add(n);
			return true;
		}

		return false;
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
		} else if ( isClassSpanNode(e, "title") && (e.childNodeSize() > 0) && isTitleSpan( e.childNode(0) ) ) {
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


	public static final boolean isClassSpanNode(Node e, String attribute) {
		return e.nodeName().equals("span") && e.attr("class").equals(attribute);
	}


	public static final boolean isTextSpanNode(Node e, String attribute) {
		return isClassSpanNode(e, attribute) && ( e.childNode(0) instanceof TextNode );
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

		if ( isTextNode(e) )
		{
			TextNode tn = (TextNode)e;
			sb.append( tn.text() );
		} else if ( isLineBreak(e) ) {
			sb.append(" ");
		}

		for (int i = 0; i < e.childNodeSize(); i++)
		{
			Node n = e.childNode(i);

			while ( !(n instanceof TextNode) && n.childNodeSize() > 0 ) {
				n = n.childNode(0);
			}

			if (n instanceof TextNode)
			{
				TextNode tn = (TextNode)n;
				sb.append( tn.text() );
			}
		}

		return sb.toString();
	}


	public static final int parseHadithNumber(Node e)
	{
		String hadithNum = getTextNode(e).text().split(HADITH_NUM_DELIMITER)[0].trim();
		return Integer.parseInt(hadithNum);
	}


	public static final boolean isHadithNumberValid(Node e, List<Narration> narrations, Narration n)
	{
		int current = ShamelaUtils.parseHadithNumber(e);

		if (n != null) {
			return n.id <= current;
		}

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


	public static final void addIfSignatureMatches(List<Node> nodes, String collectionTitle, List<Narration> narrations)
	{
		for (Node e: nodes)
		{
			if ( isTextNode(e) )
			{
				String body = ((TextNode)e).text();

				if ( body.trim().equals(collectionTitle) )
				{
					e = e.nextSibling().nextSibling();

					if ( isTextNode(e) )
					{
						body = ((TextNode)e).text().trim();

						int index = body.indexOf(" ");
						String hadithNum = body.substring(0, index);
						Narration n = new Narration( Integer.parseInt(hadithNum) );
						n.text = body.substring(index+1).trim();

						try {
							e = e.nextSibling().nextSibling().nextSibling().nextSibling().nextSibling();

							if (e != null) {
								n.grading = ((TextNode)e).text().trim();

								while ( e.nextSibling() != null )
								{
									e = e.nextSibling();

									if ( isClassSpanNode(e, "red") ) {
										n.grading += extractText(e);
									} else if ( ShamelaUtils.isTextNode(e) ) {
										n.grading += ((TextNode)e).text().trim();
									}
								}
							}
						} catch (NullPointerException ex) {}

						appendIfValid(n, narrations);
					}
				}
			} 
		}
	}


	public static void processSimple(List<Node> nodes, JSONObject json, List<Narration> narrations)
	{
		Narration n = null;

		for (Node e: nodes)
		{
			if ( isHadithNumberNode(e) ) {
				n = createNewNarration(n, e, narrations);
			} else if ( ShamelaUtils.isTextNode(e) ) {
				String body = ((TextNode)e).text();

				if (n != null) {
					n.text += body;
				}
			} else if ( isTitleSpan(e) && (n != null) ) {
				n.text += extractText(e);
			}
		}

		appendIfValid(n, narrations);
	}
	
	
	public static void appendWithIndex(Narration n, Map<Integer,Integer> hadithNumToIndex, List<Narration> narrations)
	{
		if ( n != null && !n.text.isEmpty() && ShamelaUtils.isArabicText(n.text) )
		{
			Integer index = hadithNumToIndex.get(n.id);

			if (index == null)
			{
				hadithNumToIndex.put(n.id, narrations.size());
				narrations.add(n);
			} else {
				Narration prev = narrations.get(index);
				prev.text += " "+n.text;
				
				if (n.grading != null) {
					prev.grading += "; "+n.grading;
				}
			}
		}
	}


	private static TextNode getTextNode(Node e)
	{
		while (!(e instanceof TextNode)) {
			e = e.childNode(0);
		}

		TextNode tn = (TextNode)e;
		return tn;
	}
}