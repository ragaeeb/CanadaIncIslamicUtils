package com.canadainc.sunnah10;

public class Narration implements Comparable<Narration>
{
	public Chapter chapter;
	public Book book;
	public Book part;
	public String hadithNumber;
	public int id;
	public int inBookNumber;
	public String text;
	public int translator;
	public String grading;
	public String commentary;

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
		Narration other = (Narration) obj;
		if (chapter == null) {
			if (other.chapter != null)
				return false;
		} else if (!chapter.equals(other.chapter))
			return false;
		if ( !book.equals(other.book) )
			return false;
		if (hadithNumber == null) {
			if (other.hadithNumber != null)
				return false;
		} else if (!hadithNumber.equals(other.hadithNumber))
			return false;
		if (id != other.id)
			return false;
		if (inBookNumber != other.inBookNumber)
			return false;
		if (translator != other.translator)
			return false;
		if (grading == null) {
			if (other.grading != null)
				return false;
		} else if (!grading.equals(other.grading))
			return false;
		if (text == null) {
			if (other.text != null)
				return false;
		} else if (!text.equals(other.text))
			return false;
		return true;
	}


	public Narration() {
		super();
		this.book = new Book();
		this.text = new String();
	}
	
	public Narration(int id)
	{
		this();
		this.id = id;
	}

	public Narration(int id, String babName, int babNumber, int bookId, String bookName, String hadithNumber,
			int inBookNumber, String text) {
		super();
		this.chapter = new Chapter(babName, babNumber);
		this.book = new Book(bookId, bookName);
		this.hadithNumber = hadithNumber;
		this.id = id;
		this.inBookNumber = inBookNumber;
		this.text = text;
	}
	
	public Narration(int id, String babName, int babNumber, int bookId, String bookName, String hadithNumber,
			int inBookNumber, String text, int translator) {
		this(id, babName, babNumber, bookId, bookName, hadithNumber, inBookNumber, text);
		this.translator = translator;
	}

	@Override
	public String toString() {
		return "Narration [id=" + id + ", chapter=" + chapter + ", book=" + book + ", part=" + part + ", hadithNumber="
				+ hadithNumber + ", inBookNumber=" + inBookNumber + ", text=" + text + ", translator="
				+ translator + ", grading=" + grading + "]";
	}

	@Override
	public int compareTo(Narration o)
	{
		if (id == o.id) {
			return 0;
		} else if (id < o.id) {
			return -1;
		} else {
			return 1;
		}
	}
}