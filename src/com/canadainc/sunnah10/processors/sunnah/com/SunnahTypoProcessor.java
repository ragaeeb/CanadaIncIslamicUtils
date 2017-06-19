package com.canadainc.sunnah10.processors.sunnah.com;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.json.simple.JSONObject;

import com.canadainc.sunnah10.Narration;
import com.canadainc.sunnah10.processors.Processor;

public class SunnahTypoProcessor
{
	private final Map<Integer,Integer> m_idToFixedHadithNum = new HashMap<>();
	private final Map<Integer,Integer> m_idToDiff = new HashMap<>();
	private final Map<Integer,Decomposition> m_idToBreakData = new HashMap<>();
	private final Map<Integer,Integer> m_hadithNumToIndex = new HashMap<>();
	private int[] m_ignored;

	/** Narrations to merge with existing ones. If the value is 0, then we will look for a hadeeth with the same number. */
	private final Map<Integer,Integer> m_toMerge = new HashMap<>();

	private static final int MERGE_FLAG_SELF_PRE = -1;


	/**
	 * Replaces the narration's hadeeth number with the fixed one.
	 * @param id
	 * @param realNumber
	 */
	public void fixHadithNumber(int id, int realNumber) {
		m_idToFixedHadithNum.put(id, realNumber);
	}


	public void track(int hadithNumber, int index) {
		m_hadithNumToIndex.put(hadithNumber, index);
	}


	public void ignore(int ...pages) {
		m_ignored = pages;
	}


	/**
	 * Merges the narration with the one with the specified hadeeth number.
	 * post Must call <code>#track</code> function before processing.
	 * @param id
	 * @param withHadithNumber
	 */
	public void merge(int id, int withHadithNumber) {
		m_toMerge.put(id, withHadithNumber);
	}


	/**
	 * Looks up the hadith number for this narration, and merges the text with it. 
	 * @param id
	 */
	public void merge(int id) {
		merge(id, MERGE_FLAG_SELF_PRE);
	}


	/**
	 * Fixes the range of narrations by adjusting its hadith number by the <code>diff</code> value.
	 * @param from
	 * @param to
	 * @param diff For example +1 if the hadith number should be adjusted by 1, or -1 if it should be reduced by 1.
	 */
	public void fixRange(int fromId, int toId, int diff, int ...exclusions)
	{
		if (fromId >= toId) {
			throw new IllegalArgumentException("Invalid range specified: "+fromId+"; "+toId+"; "+diff);
		}

		for (int i = fromId; i <= toId; i++)
		{
			if ( !ArrayUtils.contains(exclusions, i) ) {
				m_idToDiff.put(i, diff);
			}
		}
	}


	/**
	 * 
	 * @param narrationId
	 * @param hadithNumber
	 * @param breakPoint
	 */
	public void decompose(int narrationId, int hadithNumber, String breakPoint) {
		m_idToBreakData.put(narrationId, new Decomposition(hadithNumber, breakPoint));
	}


	public void decompose(int narrationId, String breakPoint) {
		decompose(narrationId, MERGE_FLAG_SELF_PRE, breakPoint);
	}


	private static int parseHadithNumber(JSONObject json)
	{
		String hadithNumStr = (String)json.get("hadithNumber");
		int initHadithNumber = hadithNumStr != null && hadithNumStr.matches("\\d+$") ? Integer.parseInt(hadithNumStr) : 0;

		return initHadithNumber;
	}


	public boolean process(JSONObject json, Processor p)
	{
		String urn = (String)json.get("englishURN");

		if (urn == null) {
			urn = (String)json.get("arabicURN");
		}

		int id = urn == null ? 0 : Integer.parseInt(urn);
		int initHadithNumber = parseHadithNumber(json);

		if ( m_ignored != null && ArrayUtils.contains(m_ignored, id) ) {
			return false;
		}

		if ( m_idToFixedHadithNum.containsKey(id) ) {
			initHadithNumber = m_idToFixedHadithNum.get(id);
			fillHadithNumber(json, initHadithNumber);
		}

		if ( m_idToDiff.containsKey(id) ) {
			int hadithNum = initHadithNumber;
			hadithNum += m_idToDiff.get(id);
			fillHadithNumber(json, hadithNum);
		}

		if ( m_idToBreakData.containsKey(id) ) {
			Decomposition d = m_idToBreakData.get(id);

			String body = json.get("hadithText").toString();
			int start = body.indexOf(d.breakPoint);

			if (start >= 0)
			{
				if (d.hadithNumber == MERGE_FLAG_SELF_PRE) {
					d.hadithNumber = initHadithNumber;
				}

				fillHadithData(json, body.substring(start).trim(), d.hadithNumber+1);

				// TODO: MUST +1 on the narration ID
				JSONObject prev = new JSONObject(json);
				fillHadithData(prev, body.substring(0,start).trim(), d.hadithNumber);
				p.process(prev);
			} else {
				System.err.println("BreakPoint not found: "+id);
			}
		}

		if ( m_toMerge.containsKey(id) )
		{
			int hadithNum = m_toMerge.get(id);

			if (hadithNum == MERGE_FLAG_SELF_PRE) {
				hadithNum = initHadithNumber;
			}

			int index = getIndex(hadithNum);

			if (index >= 0) {
				Narration n = p.getNarrations().get(index);
				n.text += " "+json.get("hadithText").toString();
			}

			return false;
		}

		return true;
	}


	public int getIndex(int hadithNumber)
	{
		Integer index = m_hadithNumToIndex.get(hadithNumber);
		return index == null ? -1 : index;
	}



	private static final void fillHadithNumber(JSONObject json, int hadithNumber) {
		json.put("hadithNumber", String.valueOf(hadithNumber) );
	}

	private static final void fillHadithData(JSONObject json, String body, int hadithNumber)
	{
		json.put("hadithText", body);
		fillHadithNumber(json, hadithNumber);
	}


	private class Decomposition
	{
		public int hadithNumber;
		public String breakPoint;

		public Decomposition(int hadithNumber, String breakPoint)
		{
			this.hadithNumber = hadithNumber;
			this.breakPoint = breakPoint;
		}
	}
}