package com.canadainc.sunnah10.controller.merge;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.canadainc.sunnah10.Narration;
import com.canadainc.sunnah10.utils.SunnahUtils;

public class CommentaryBasedMerger implements CollectionMerger
{
	private List<Narration> m_commentaries;

	private List<Narration> m_translations;

	private List<Narration> m_arabic;
	
	private List<Narration> m_vowelled;

	public CommentaryBasedMerger()
	{
	}

	@Override
	public List<Narration> getArabic() {
		return m_arabic;
	}

	@Override
	public List<Narration> getTranslations() {
		return m_translations;
	}

	private static void fill(Map<Integer,Narration> numToHadith, List<Narration> narrations)
	{
		if (narrations != null)
		{
			for (Narration n: narrations)
			{
				Narration matching = numToHadith.get(n.id);

				if (matching == null)
				{
					matching = n.clone();
					numToHadith.put(n.id, matching);
				}

				if (matching.grading == null) {
					matching.grading = n.grading;
				} else if ( !matching.grading.equals(n.grading) ) {
					matching.commentary = n.grading;
				}
			}
		}
	}


	@Override
	public void merge()
	{
		Map<Integer,Narration> numToHadith = new TreeMap<>();

		fill(numToHadith, m_arabic);
		fill(numToHadith, m_commentaries);

		m_arabic = new ArrayList<>();

		for (Integer id: numToHadith.keySet()) {
			m_arabic.add( numToHadith.get(id) );
		}

		for (Narration n: m_translations) {
			n.id = Integer.parseInt(n.hadithNumber);
		}

		m_translations = SunnahUtils.sort(m_translations, true);
	}

	@Override
	public void setArabic(List<Narration> arabic) {
		m_arabic = arabic;
	}

	@Override
	public void setCommentary(List<Narration> commentary) {
		m_commentaries = commentary;
	}

	@Override
	public void setTranslation(List<Narration> translation, Map<Integer, Integer> idToTranslation) {
		m_translations = translation;
	}

	@Override
	public void setVowelled(List<Narration> arabic) {
		m_vowelled = arabic;
	}
}