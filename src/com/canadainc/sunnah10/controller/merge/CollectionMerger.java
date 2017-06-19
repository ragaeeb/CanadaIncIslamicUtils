package com.canadainc.sunnah10.controller.merge;

import java.util.List;
import java.util.Map;

import com.canadainc.sunnah10.Narration;

public interface CollectionMerger
{
	public List<Narration> getArabic();

	public List<Narration> getTranslations();

	public void merge();

	public void setArabic(List<Narration> arabic);

	public void setCommentary(List<Narration> arabic);

	public void setTranslation(List<Narration> translation, Map<Integer, Integer> idToTranslation);
	
	public void setVowelled(List<Narration> arabic);
}