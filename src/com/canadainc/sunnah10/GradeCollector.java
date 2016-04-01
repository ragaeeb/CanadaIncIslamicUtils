package com.canadainc.sunnah10;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class GradeCollector implements Collector
{
	private Map<Integer, Grade> m_grade;
	private Map<String, Integer> m_collectionToAuthor;

	public GradeCollector()
	{
		super();
		m_grade = new HashMap<Integer, Grade>();
		m_collectionToAuthor = new HashMap<String, Integer>();
		
		m_collectionToAuthor.put("abudawud", 11); // Al-Albaanee
		m_collectionToAuthor.put("adab", 11); // Al-Albaanee
		m_collectionToAuthor.put("nasai", -1); // Darussalam: Hafiz Zubair `Ali Za`i
		m_collectionToAuthor.put("tirmidhi", -1); // Darussalam: Hafiz Zubair `Ali Za`i
		m_collectionToAuthor.put("ibnmajah", -1); // Darussalam: Hafiz Zubair `Ali Za`i
	}

	
	/*
	 * (non-Javadoc)
	 * @see com.canadainc.sunnah10.Collector#process(java.util.Collection, boolean, java.lang.String)
	 */
	public void process(Collection<Narration> narrations, String language, String collection)
	{
		if ( m_collectionToAuthor.containsKey(collection) )
		{
			for (Narration n: narrations)
			{
				if ( n.grading != null && !n.grading.isEmpty() )
				{
					Grade g = new Grade();
					g.body = n.grading;
					g.author = m_collectionToAuthor.get(collection).intValue();
					m_grade.put(n.id, g);
				}
			}
		}
	}

	public Map<Integer, Grade> getCollected() {
		return m_grade;
	}


	@Override
	public void setDictionary(Dictionary d)
	{
	}
}