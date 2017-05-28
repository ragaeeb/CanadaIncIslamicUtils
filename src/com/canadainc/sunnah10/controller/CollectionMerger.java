package com.canadainc.sunnah10.controller;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.canadainc.common.text.TextUtils;
import com.canadainc.sunnah10.Narration;

import info.debatty.java.stringsimilarity.Levenshtein;

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
			public int compare(Narration n1, Narration n2)
			{
				n1.text = TextUtils.normalize(n1.text);
				n2.text = TextUtils.normalize(n2.text);

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
		Levenshtein l = new Levenshtein();

		for (int i = 0; i < minSize; i++)
		{
			Narration a = vowelled.get(i);
			Narration b = commentaries.get(i);

			if (a.id != b.id) {
				System.err.println(i+": "+a.id+" vs. "+b.id);
			}
			/*System.out.println("a");
			System.out.println(a.text);
			System.out.println("b");
			System.out.println(b.text);
			System.out.println( l.distance(a.text, b.text) );
			break; */
		}
	}
}