package com.canadainc.sunnah10;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class GradeCollector implements Collector
{
	private Map<Integer, Grade> m_grade;
	private Map<String, Integer> m_collectionToAuthor;
	private Map<CollectionNarration, Integer> m_pairToNarration;

	public GradeCollector()
	{
		//TODO: Need to break this up to be more maintainable. So the ones that are excluded is at a later step.
		
		super();
		m_grade = new HashMap<>();
		m_collectionToAuthor = new HashMap<>();
		m_pairToNarration = new HashMap<>();
		
		m_collectionToAuthor.put("abudawud", SunnahConstants.SHAYKH_AL_ALBAANI_ID); // Al-Albaanee
		m_collectionToAuthor.put("adab", SunnahConstants.SHAYKH_AL_ALBAANI_ID); // Al-Albaanee
		//m_collectionToAuthor.put("nasai", -1); // Darussalam: Hafiz Zubair `Ali Za`i
		//m_collectionToAuthor.put("tirmidhi", -1); // Darussalam: Hafiz Zubair `Ali Za`i
		//m_collectionToAuthor.put("ibnmajah", -1); // Darussalam: Hafiz Zubair `Ali Za`i
	}
	
	
	public void applyGrades(String collection, int muhaddith, Collection<Narration> narrations)
	{
		for (Narration n: narrations)
		{
			CollectionNarration cn = new CollectionNarration(collection, n.hadithNumber);
			
			if ( m_pairToNarration.containsKey(cn) )
			{
				int narrationId = m_pairToNarration.get(cn);
				Grade g = m_grade.get(narrationId);
				
				if (g == null) {
					g = new Grade(muhaddith, n.grading);
					m_grade.put(narrationId, g);
				} else {
					g.author = muhaddith;
					g.body = n.grading;
				}
			} else {
				System.err.println("UntrackedGrading: "+cn.collection+"; "+cn.hadithNumber);
			}
		}
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
		
		for (Narration n: narrations)
		{
			CollectionNarration cn = new CollectionNarration(collection, n.hadithNumber);
			m_pairToNarration.put(cn, n.id);
		}
	}

	public Map<Integer, Grade> getCollected() {
		return m_grade;
	}


	@Override
	public void setDictionary(Dictionary d)
	{
	}
	
	
	private class CollectionNarration
	{
		public String collection;
		public String hadithNumber;

		public CollectionNarration(String collection, String hadithNumber) {
			this.collection = collection;
			this.hadithNumber = hadithNumber;
		}
		
		@Override
		public int hashCode()
		{
			final int prime = 31;
			int result = 1;
			result = prime * result + ((collection == null) ? 0 : collection.hashCode());
			result = prime * result + ((hadithNumber == null) ? 0 : hadithNumber.hashCode());
			return result;
		}
		@Override
		public boolean equals(Object obj)
		{
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			CollectionNarration other = (CollectionNarration) obj;
			if (collection == null)
			{
				if (other.collection != null)
					return false;
			} else if (!collection.equals(other.collection))
				return false;
			if (hadithNumber == null)
			{
				if (other.hadithNumber != null)
					return false;
			} else if (!hadithNumber.equals(other.hadithNumber))
				return false;
			return true;
		}
	}
}