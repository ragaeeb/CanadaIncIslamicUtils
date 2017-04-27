package com.canadainc.sunnah10;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import com.canadainc.common.io.IOUtils;
import com.canadainc.common.text.TextUtils;

public class SilsilaSaheehaParser
{
	public SilsilaSaheehaParser()
	{
	}
	
	public Collection<Narration> parse(File file) throws IOException
	{
		String body = IOUtils.readWin1256(file);
		String[] parts = TextUtils.extractInside(body, "</font></div><p> ", "</font></body>").split("<hr width=\"95%\" color=\"#000080\"><p> ");
		ArrayList<Narration> narrations = new ArrayList<>();
		
		for (String part: parts)
		{
			part = part.trim();
			int lineBreak = part.indexOf("<br>");
			
			if (lineBreak != -1)
			{
				String numberAndHukm = part.substring(0, lineBreak).trim();

				String[] tokens = numberAndHukm.trim().split("\\s+-\\s+");

				if (tokens.length == 1)
				{
					tokens = numberAndHukm.trim().split("\\)\\s+\\(");
				}
				
				String hadithNumber = TextUtils.extractInsideBrackets(tokens[0]);
				String hukm = TextUtils.extractInsideBrackets(tokens[1]);
				
				body = TextUtils.extractInside(part, "<br>", "</p></I></font>");
				tokens = body.split("<Font color=\"Blue\"><I><p align=\"left\">");
				String text = tokens[0].trim();
				String inBook = TextUtils.extractInsideBrackets( tokens[1].trim() );
				
				if ( hadithNumber.contains(" / ") ) {
					hadithNumber = hadithNumber.split(" / ")[0];
				}
				
				Narration n = new Narration();
				n.id = Integer.parseInt(hadithNumber);
				n.grading = hukm;
				n.text = text;
				tokens = inBook.split("/");
				n.book = new Book( Integer.parseInt(tokens[0]), null );
				n.inBookNumber = Integer.parseInt(tokens[1]);
				narrations.add(n);
			}
		}
		
		return narrations;
	}
}