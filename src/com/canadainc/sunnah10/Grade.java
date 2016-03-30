package com.canadainc.sunnah10;

public class Grade
{
	public int author;
	public String body;

	public Grade() {
		super();
	}

	public Grade(int author, String body) {
		super();
		this.author = author;
		this.body = body;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + author;
		result = prime * result + ((body == null) ? 0 : body.hashCode());
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
		Grade other = (Grade) obj;
		if (author != other.author)
			return false;
		if (body == null) {
			if (other.body != null)
				return false;
		} else if (!body.equals(other.body))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return author + ": " + body;
	}
}