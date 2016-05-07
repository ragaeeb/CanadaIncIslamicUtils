package com.canadainc.sunnah10;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.text.WordUtils;

public class ChapterCollector implements Collector
{
	private Set<Chapter> m_chapters;
	private Pattern chapterPattern;
	private Dictionary m_dictionary;
	
	public ChapterCollector()
	{
		m_chapters =  new HashSet<Chapter>();
		chapterPattern = Pattern.compile("^Chapter \\d+\\.*\\s*\\w", Pattern.CASE_INSENSITIVE);
	}

	@Override
	public void process(Collection<Narration> narrations, String language, String collection)
	{
		for (Narration n: narrations)
		{
			correctChapters(n);
			m_chapters.add(n.chapter);
		}
	}
	
	public void correctChapters(Narration n)
	{
		String babName = n.chapter.title;

		if ( babName != null && !babName.trim().isEmpty() )
		{
			int babNumber = n.chapter.number;

			if ( babName.startsWith(".") || babName.startsWith(":") ) {
				babName = babName.substring(1);
			} else if ( babName.startsWith("B.") ) {
				babName = babName.substring(2);
			}

			babName = WordUtils.capitalizeFully(babName);

			if (babNumber == 0)
			{
				Matcher matcher = chapterPattern.matcher(babName);

				if ( matcher.find() )
				{
					String match = babName.substring( matcher.start(), matcher.end() ).trim();
					babNumber = Integer.parseInt( match.replaceAll("\\D+","") );

					for (int k = matcher.end()-1; k < babName.length(); k++)
					{
						char current = babName.charAt(k);

						if ( Character.isAlphabetic(current) ) {
							babName = babName.substring(k);
							break;
						}
					}
				}
			}

			babName = babName.replaceAll("<[^>]*>", "").trim();

			if ( babName.isEmpty() ) {
				babName = null;
			} else if ( babName.endsWith(" ?") ) { // Returning Salam While Urinating ?
				babName = babName.substring( 0, babName.length()-2 )+"?";
			}

			if (m_dictionary != null) {
				babName = m_dictionary.correctTypos(babName);
			}

			n.chapter.number = babNumber;
		} else {
			babName = null;
		}
		
		if (babName != null)
		{
			babName = babName.trim();
			
			if ( babName.equals("باب") || babName.equals("باب ‏)‏‏)‏") ) {
				babName = null;
			}
		}

		n.chapter.title = babName;
	}
	
	public Collection<Chapter> getCollected() {
		return m_chapters;
	}

	@Override
	public void setDictionary(Dictionary d) {
		m_dictionary = d;
	}
}