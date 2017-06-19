package com.canadainc.sunnah10;

public class Book implements Comparable<Book>
{
	public int id;
	public String name;
	public String transliteration;

	public Book(int bookId, String name)
	{
		super();
		this.id = bookId;
		this.name = name;
	}
	
	
	public Book(Book b)
	{
		this.id = b.id;
		this.name = b.name;
		this.transliteration = b.transliteration;
	}
	
	
	public Book(int bookId, String name, String transliteration)
	{
		this(bookId, name);
		this.transliteration = transliteration;
	}

	public Book()
	{
	}

	@Override
	public int hashCode() {
		/*
		final int prime = 31;
		int result = 1;
		result = prime * result + bookId;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result; */
		return id;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Book other = (Book) obj;
		if (id != other.id)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (transliteration == null) {
			if (other.transliteration != null)
				return false;
		} else if (!transliteration.equals(other.transliteration))
			return false;
		return true;
	}

	@Override
	public int compareTo(Book o)
	{
		if (id == o.id) {
			return 0;
		} else if (id < o.id) {
			return -1;
		} else {
			return 1;
		}
	}

	@Override
	public String toString()
	{
		if (transliteration != null) {
			return id+": "+name+" ("+transliteration+")";
		} else {
			return id+": "+name;
		}
	}
}