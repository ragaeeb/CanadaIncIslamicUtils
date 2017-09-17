package com.canadainc.sunnah10.utils;

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

		if (idBased) {
			Collections.sort(result);
		} else {
			Collections.sort(result, new Comparator<Narration>()
			{
				@Override
				public int compare(Narration n1, Narration n2) {
					return parseHadithNumber(n1)-parseHadithNumber(n2);
				}
			});
		}

		return result;
	}
	
	
	public static int parseHadithNumber(Narration n) {
		return Integer.parseInt(n.hadithNumber.replaceAll("[\\sa-z,]+", ""));
	}
}