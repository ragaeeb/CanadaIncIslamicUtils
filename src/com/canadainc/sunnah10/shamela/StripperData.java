package com.canadainc.sunnah10.shamela;

public class StripperData
{
	public int pageNumber;
	public String startMatcher;
	public String endMatcher;
	public String inner;
	public String replacement;
	public boolean forward;
	public StripperData(int pageNumber, String startMatcher, String endMatcher, String inner, String replacement,
			boolean forward)
	{
		super();
		this.pageNumber = pageNumber;
		this.startMatcher = startMatcher;
		this.endMatcher = endMatcher;
		this.inner = inner;
		this.replacement = replacement;
		this.forward = forward;
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
		result = prime * result + pageNumber;
		result = prime * result + ((replacement == null) ? 0 : replacement.hashCode());
		result = prime * result + ((startMatcher == null) ? 0 : startMatcher.hashCode());
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
		StripperData other = (StripperData) obj;
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
		if (pageNumber != other.pageNumber)
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
		return true;
	}
}