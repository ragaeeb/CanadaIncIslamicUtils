package com.canadainc.sunnah10;

public class SunnahCollection
{
	public int id;
	public int author;
	public String name;
	public String description;
	public SunnahCollection(int id, int author, String name) {
		super();
		this.id = id;
		this.author = author;
		this.name = name;
	}
	@Override
	public int hashCode() {
		return id;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SunnahCollection other = (SunnahCollection) obj;
		if (author != other.author)
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (id != other.id)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "SunnahCollection [id=" + id + ", author=" + author + ", name=" + name
				+ ", description=" + description + "]";
	}
}