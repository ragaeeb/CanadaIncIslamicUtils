package com.canadainc.sunnah10;

public class Chapter implements Comparable<Chapter>
{
	public String title;
	public int number;

	public Chapter() {
		super();
	}

	public Chapter(String name, int number)
	{
		super();
		this.title = name;
		this.number = number;
	}
	
	
	public Chapter(Chapter c)
	{
		this.number = c.number;
		this.title = c.title;
	}
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		result = prime * result + number;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Chapter other = (Chapter) obj;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		if (number != other.number)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return number+" "+title;
	}

	@Override
	public int compareTo(Chapter o)
	{
		if (number == o.number) {
			return 0;
		} else if (number < o.number) {
			return -1;
		} else {
			return 1;
		}
	}
}