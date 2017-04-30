package com.canadainc.sunnah10.shamela;

import java.util.List;

import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import com.canadainc.sunnah10.Narration;

public class ShamelaUtils
{
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
	
	
	public static final boolean isTextSpanNode(Node e, String attribute) {
		return e.nodeName().equals("span") && e.attr("class").equals(attribute) && ( e.childNode(0) instanceof TextNode );
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
	
	
	public static final String parseChildText(Node e)
	{
		TextNode tn = (TextNode)e.childNode(0);
		return tn.text();
	}
	
	
	public static final int parseHadithNumber(Node e)
	{
		TextNode tn = (TextNode)e.childNode(0);
		String hadithNum = tn.text().split("-")[0].trim();
		return Integer.parseInt(hadithNum);
	}
	
	
	public static final void assertSequential(List<Narration> narrations, boolean id)
	{
		int n = narrations.size();
		
		if (n > 1)
		{
			Narration secondLast = narrations.get(n-2);
			Narration last = narrations.get(n-1);
			
			if ( ( id && (last.id-secondLast.id != 1) ) || ( !id && (last.inBookNumber-secondLast.inBookNumber != 1) ) ) {
				throw new RuntimeException("IdDiff");
			}
		}
	}
}