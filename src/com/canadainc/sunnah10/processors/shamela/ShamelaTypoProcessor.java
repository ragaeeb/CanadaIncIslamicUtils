package com.canadainc.sunnah10.processors.shamela;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;

public class ShamelaTypoProcessor
{
	private Map<Integer, Collection<Typo>> m_typos;
	private int[] m_ignored;

	public ShamelaTypoProcessor() {
		m_typos = new HashMap<>();
	}


	public void add(int pageNumber, String value, String replacement) {
		addTypo(pageNumber, new Typo(value, replacement) );
	}
	
	public void add(int pageNumber, int foundPage, int actualPage) {
		add(pageNumber, String.valueOf(foundPage), String.valueOf(actualPage));
	}

	public void highlight(int pageNumber, int index) {
		add(pageNumber, String.valueOf(index), decorateContent( String.valueOf(index) ));
	}


	public void removeBlank(int pageNumber, int index) {
		add(pageNumber, String.valueOf(index)+" -<br /><br />", "");
	}


	public void add(int pageNumber, String value) {
		add(pageNumber, value, String.valueOf(pageNumber));
	}


	public void addTypo(int pageNumber, Typo t)
	{
		Collection<Typo> typos = m_typos.get(pageNumber);

		if (typos == null) {
			typos = new ArrayList<>();
		}

		typos.add(t);
		m_typos.put(pageNumber, typos);
	}


	public void ignore(int ...pages) {
		m_ignored = pages;
	}


	public void addRegexed(int pageNumber, String value, String replacement) {
		addTypo(pageNumber, new Typo(value, replacement, TypoType.Regex));
	}


	public void addNumericListStripper(int pageNumber, String startMatcher, String endMatcher, boolean forward) {
		addContentStripper(pageNumber, startMatcher, endMatcher, "[1-9]{1,2} - ", "", forward);
	}


	public void addNumericListStripper(int pageNumber, String startMatcher, String endMatcher) {
		addNumericListStripper(pageNumber, startMatcher, endMatcher, true);
	}


	public void addContentStripper(int pageNumber, String startMatcher, String endMatcher, String inner, String replacement, boolean forward) {
		addTypo(pageNumber, new Typo(startMatcher, endMatcher, inner, replacement, forward));
	}


	public void prependHadithNumber(int pageNumber, int hadithNumber) {
		addTypo( pageNumber, new Typo(decorateContent( String.valueOf(hadithNumber) ), null, TypoType.Prepend) );
	}
	
	public void prependRange(int fromPage, int toPage)
	{
		for (int i = fromPage; i <= toPage; i++) {
			prependHadithNumber(i,i);
		}
	}

	public void clearAfter(int pageNumber, String match) {
		addTypo(pageNumber, new Typo(match, null, TypoType.ClearAfter));
	}

	public void prependConditionalIndex(int fromPage, int toPage, int hadithNumber, String match)
	{
		for (int i = fromPage; i <= toPage; i++) {
			addTypo( i, new Typo(match, null, TypoType.ConditionalIndex) );
		}
	}


	public static final String decorateContent(String inner) {
		return decorate(inner+" - ");
	}

	public static final String decorate(String inner) {
		return "<span class=\"red\">"+inner+"</span>";
	}


	private String process(String content, String value, String replacement, boolean regex) {
		return regex ? content.replaceAll(value, replacement) : content.replace(value, replacement);
	}


	public String process(int pageNumber, String content)
	{
		if ( m_ignored != null && ArrayUtils.contains(m_ignored, pageNumber) ) {
			return null;
		}

		Collection<Typo> typos = m_typos.get(pageNumber);

		if (typos != null)
		{
			for (Typo typo: typos)
			{
				if (typo.type == null) {
					//System.out.println(content);
					content = process(content, typo.value, typo.replacement, false);
					//System.out.println(content);
				} else if (typo.type == TypoType.Regex) {
					content = process(content, typo.value, typo.replacement, true);
				} else if (typo.type == TypoType.Prepend) {
					content = typo.value+content;
				} else if (typo.type == TypoType.Stripper) {
					int start = typo.forward ? content.indexOf(typo.startMatcher) : content.lastIndexOf(typo.startMatcher);
					int end = typo.forward ? content.indexOf(typo.endMatcher) : content.lastIndexOf(typo.endMatcher);
					String beginning = content.substring(0, start);
					String hadeeth = content.substring(start, end).replaceAll("<br />", "").replaceAll( decorate(typo.inner), typo.replacement );
					String ending = content.substring(end);
					content = beginning+hadeeth+ending;
				} else if (typo.type == TypoType.ConditionalIndex && content.startsWith(typo.value)) {
					content = typo.value+content;
				} else if (typo.type == TypoType.ClearAfter) {
					int start = content.indexOf(typo.value);
					
					if (start >= 0) {
						content = content.substring( 0, start+typo.value.length() ).trim();
					}
				}
			}
		}

		return content;
	}


	public class Typo
	{
		public String value;
		public String replacement;
		public TypoType type;
		public String startMatcher;
		public String endMatcher;
		public String inner;
		public boolean forward;

		public Typo(String value, String replacement) {
			this(value, replacement, null);
		}

		public Typo(String value, String replacement, TypoType type)
		{
			super();
			this.value = value;
			this.replacement = replacement;
			this.type = type;
		}


		public Typo(String startMatcher, String endMatcher, String inner, String replacement,
				boolean forward)
		{
			super();
			this.startMatcher = startMatcher;
			this.endMatcher = endMatcher;
			this.inner = inner;
			this.replacement = replacement;
			this.forward = forward;
			this.type = TypoType.Stripper;
		}



		/* (non-Javadoc)
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode()
		{
			final int prime = 31;
			int result = 1;
			result = prime * result + ((endMatcher == null) ? 0 : endMatcher.hashCode());
			result = prime * result + (forward ? 1231 : 1237);
			result = prime * result + ((inner == null) ? 0 : inner.hashCode());
			result = prime * result + ((replacement == null) ? 0 : replacement.hashCode());
			result = prime * result + ((startMatcher == null) ? 0 : startMatcher.hashCode());
			result = prime * result + ((type == null) ? 0 : type.hashCode());
			result = prime * result + ((value == null) ? 0 : value.hashCode());
			return result;
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj)
		{
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Typo other = (Typo) obj;
			if (endMatcher == null)
			{
				if (other.endMatcher != null)
					return false;
			} else if (!endMatcher.equals(other.endMatcher))
				return false;
			if (forward != other.forward)
				return false;
			if (inner == null)
			{
				if (other.inner != null)
					return false;
			} else if (!inner.equals(other.inner))
				return false;
			if (replacement == null)
			{
				if (other.replacement != null)
					return false;
			} else if (!replacement.equals(other.replacement))
				return false;
			if (startMatcher == null)
			{
				if (other.startMatcher != null)
					return false;
			} else if (!startMatcher.equals(other.startMatcher))
				return false;
			if (type != other.type)
				return false;
			if (value == null)
			{
				if (other.value != null)
					return false;
			} else if (!value.equals(other.value))
				return false;
			return true;
		}



		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString()
		{
			return "Typo [value=" + value + ", replacement=" + replacement + ", type=" + type + ", startMatcher="
					+ startMatcher + ", endMatcher=" + endMatcher + ", inner=" + inner + ", forward=" + forward + "]";
		}
	}

	public enum TypoType {
		ClearAfter, ConditionalIndex, Prepend, Regex, Stripper
	}
}