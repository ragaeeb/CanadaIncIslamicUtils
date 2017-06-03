package com.canadainc.sunnah10.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.canadainc.sunnah10.Narration;

public class SunnahUtils
{
	public static List<Narration> sort(List<Narration> narrations, boolean idBased)
	{
		List<Narration> result = new ArrayList<Narration>( narrations.size() );
		result.addAll(narrations);

		Collections.sort(result, new Comparator<Narration>()
		{
			@Override
			public int compare(Narration n1, Narration n2) {
				return idBased ? n1.id-n2.id : Integer.parseInt(n1.hadithNumber)-Integer.parseInt(n2.hadithNumber);
			}
		});

		return result;
	}
}