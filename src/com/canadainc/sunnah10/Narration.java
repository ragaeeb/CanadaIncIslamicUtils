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
	public String grading;
	public String commentary = new String();
	public int pageNumber;
	public int translator;

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
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


	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Narration clone()
	{
		Narration n = new Narration();
		n.id = this.id;
		n.commentary = this.commentary;
		n.grading = this.grading;
		n.hadithNumber = this.hadithNumber;
		n.inBookNumber = this.inBookNumber;
		n.pageNumber = this.pageNumber;
		n.text = this.text;

		if (this.part != null) {
			n.part = new Book(this.part);
		}

		if (this.book != null) {
			n.book = new Book(this.book);
		}

		if (this.chapter != null) {
			n.chapter = new Chapter(this.chapter);
		}

		return n;
	}


	public Narration setHadithNumber(String hadithNumber) {
		this.hadithNumber = hadithNumber;
		return this;
	}

	public Narration setText(String text) {
		this.text = text;
		return this;
	}

	public Narration setGrade(String grade) {
		this.grading = grade;
		return this;
	}

	public Narration setCommentary(String comment) {
		this.commentary = comment;
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
	}

	@Override
	public String toString() {
		return "Narration [id=" + id + ", hadithNumber="
				+ hadithNumber/* + ", inBookNumber=" + inBookNumber + ", text=" + text + ", grading=" + grading + "]"*/;
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
		if (this == obj)
		{
			return true;
		}
		if (obj == null)
		{
			return false;
		}
		if (!(obj instanceof Narration))
		{
			return false;
		}
		Narration other = (Narration) obj;
		if (book == null)
		{
			if (other.book != null)
			{
				return false;
			}
		} else if (!book.equals(other.book))
		{
			return false;
		}
		if (chapter == null)
		{
			if (other.chapter != null)
			{
				return false;
			}
		} else if (!chapter.equals(other.chapter))
		{
			return false;
		}
		if (commentary == null)
		{
			if (other.commentary != null)
			{
				return false;
			}
		} else if (!commentary.equals(other.commentary))
		{
			return false;
		}
		if (grading == null)
		{
			if (other.grading != null)
			{
				return false;
			}
		} else if (!grading.equals(other.grading))
		{
			return false;
		}
		if (hadithNumber == null)
		{
			if (other.hadithNumber != null)
			{
				return false;
			}
		} else if (!hadithNumber.equals(other.hadithNumber))
		{
			return false;
		}
		if (id != other.id)
		{
			return false;
		}
		if (inBookNumber != other.inBookNumber)
		{
			return false;
		}
		if (pageNumber != other.pageNumber)
		{
			return false;
		}
		if (part == null)
		{
			if (other.part != null)
			{
				return false;
			}
		} else if (!part.equals(other.part))
		{
			return false;
		}
		if (text == null)
		{
			if (other.text != null)
			{
				return false;
			}
		} else if (!text.equals(other.text))
		{
			return false;
		}
		if (translator != other.translator)
		{
			return false;
		}
		return true;
	}
}