package com.canadainc.sunnah10;

import java.util.Collection;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.canadainc.sunnah10.utils.Dictionary;

public class NarratorCollector implements Collector
{
	private Pattern m_narratedByPattern;
	private Pattern m_narratedThatPattern;
	private Pattern m_narratedFatherPattern;
	private Pattern m_reportedPattern;
	private Collection<String> m_collected;

	public NarratorCollector()
	{
		m_narratedByPattern = Pattern.compile("^Narrated [^:]*", Pattern.CASE_INSENSITIVE);
		m_narratedThatPattern = Pattern.compile("^[^:]* narrated ", Pattern.CASE_INSENSITIVE);
		m_narratedFatherPattern = Pattern.compile("^[^:]* narrated from his father", Pattern.CASE_INSENSITIVE);
		m_reportedPattern = Pattern.compile("^[^:]* reported:", Pattern.CASE_INSENSITIVE);
		m_collected = new HashSet<String>();
	}


	/* (non-Javadoc)
	 * @see com.canadainc.sunnah10.Collector#process(java.util.Collection, boolean, java.lang.String)
	 */
	@Override
	public void process(Collection<Narration> narrations, String language, String collection)
	{
		int narratedLength = "Narrated: ".length();

		for (Narration n: narrations)
		{
			Matcher matcher = m_narratedByPattern.matcher(n.text);

			if ( matcher.find() )
			{
				//m_collected.add( n.text.substring( matcher.start()+narratedLength-1, matcher.end() ).trim() );
				continue;
			}

			matcher = m_reportedPattern.matcher(n.text);

			if ( matcher.find() )
			{
				//m_collected.add( n.text.substring( 0, n.text.indexOf(" reported:") ).trim() );
				continue;
			}

			matcher = m_narratedThatPattern.matcher(n.text);

			if ( matcher.find() )
			{
				//m_collected.add( n.text.substring( 0, n.text.indexOf(" narrated that:") ).trim() );
				continue;
			}

			matcher = m_narratedFatherPattern.matcher(n.text);

			if ( matcher.find() )
			{
				//m_collected.add( n.text.substring( 0, n.text.indexOf(" narrated") ).trim() );
				continue;
			}

			m_collected.add(n.text);
		}
	}


	public Collection<String> getCollected() {
		return m_collected;
	}


	@Override
	public void setDictionary(Dictionary d)
	{
	}
}