package com.canadainc.sunnah10.controller;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.canadainc.sunnah10.Narration;

public class CollectionMerger
{
	public CollectionMerger()
	{
	}
	
	
	public void merge(Map<Integer,Integer> arabic, Map<Integer,Integer> english)
	{
	}


	public void merge(List<Narration> vowelled, List<Narration> commentaries)
	{
		Collections.sort(vowelled, new Comparator<Narration>()
		{
			@Override
			public int compare(Narration n1, Narration n2) {
				return n1.id-n2.id;
			}
		});

		Collections.sort(commentaries, new Comparator<Narration>()
		{
			@Override
			public int compare(Narration n1, Narration n2) {
				return n1.id-n2.id;
			}
		});

		int minSize = Math.min(vowelled.size(), commentaries.size());

		for (int i = 0; i < minSize; i++)
		{
			int a = vowelled.get(i).id;
			int b = commentaries.get(i).id;

			if (a != b) {
				System.err.println(i+": "+a+" vs. "+b);
			}
		}
	}
}