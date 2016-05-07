package com.canadainc.quran10.ibnkatheer;

import java.io.File;
import java.io.IOException;

import com.canadainc.common.io.IOUtils;


public class FileAnalyzer
{
	private static final String TITLE_HEAD_HTML = "class=TitleHead>";
	private static final String TAG_CLOSE_HEADER = "</td>";
	private static final String TITLE_BODY_HTML = "<p><p>";
	private static final String TAG_CLOSE_BODY = "</td>";

	public VerseExplanation analyze(File f) throws IOException
	{
		String content = IOUtils.readFileUtf8(f);

		int index = content.indexOf(TITLE_HEAD_HTML);
		int end = content.indexOf(TAG_CLOSE_HEADER, index);

		String title = content.substring( index+TITLE_HEAD_HTML.length() , end );
		
		index = content.indexOf(TITLE_BODY_HTML);
		
		if (index != -1) {
			end = content.indexOf(TAG_CLOSE_BODY, index);
			String body = content.substring( index+TITLE_BODY_HTML.length(), end );
			body = body.replaceAll("<p dir=rtl><font face=\"Traditional Arabic\" class=AyahArabic>.+?</font><p>", "").trim();
			body = body.replaceAll("  ", " ");
			
			if ( body.endsWith("<p><br>") ) {
				body = body.substring( 0, body.lastIndexOf("<p>") );
			} else if ( body.endsWith("<p>") ) {
				body = body.substring( 0, body.lastIndexOf("<p>") );
			}
			
			body = body.replaceAll("<br>", "\n");
			body = body.replaceAll("<p>", "\n");
			body = body.replaceAll("&#64831;", ")");
			body = body.replaceAll("&#64830;", "(");
			
			int specialDash = 65533;
			String specialDashChar = "" + (char)specialDash;
			body = body.replaceAll(specialDashChar, "-");

			VerseExplanation exp = new VerseExplanation();
			exp.title = title;
			exp.body = body;
			
			return exp;
		} else {
			return null;
		}
	}
}