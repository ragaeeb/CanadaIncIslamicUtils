package com.canadainc.sunnah10.shamela;

public class Typo
{
	public int pageNumber;
	public String value;
	public String replacement;
	public Typo(int pageNumber, String value, String replacement)
	{
		super();
		this.pageNumber = pageNumber;
		this.value = value;
		this.replacement = replacement;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "Typo [pageNumber=" + pageNumber + ", value=" + value + ", replacement=" + replacement + "]";
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + pageNumber;
		result = prime * result + ((replacement == null) ? 0 : replacement.hashCode());
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
		if (pageNumber != other.pageNumber)
			return false;
		if (replacement == null)
		{
			if (other.replacement != null)
				return false;
		} else if (!replacement.equals(other.replacement))
			return false;
		if (value == null)
		{
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}
}