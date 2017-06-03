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
	public String commentary = new String();
	public int pageNumber;

	@Override
	public int hashCode() {
		return id;
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
	
	
	public Narration setHadithNumber(String hadithNumber) {
		this.hadithNumber = hadithNumber;
		return this;
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

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		return super.equals(obj);
	}
}