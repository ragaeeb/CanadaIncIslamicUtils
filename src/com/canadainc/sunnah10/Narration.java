package com.canadainc.sunnah10;

public class Narration
{
	public int arabicId;
	public String babName;
	public int babNumber;
	public int bookId;
	public String bookName;
	public String hadithNumber;
	public int id;
	public int inBookNumber;
	public String text;
	public String translator;

	@Override
	public int hashCode() {
		return id;
		/*
		final int prime = 31;
		int result = 1;
		result = prime * result + arabicId;
		result = prime * result + ((babName == null) ? 0 : babName.hashCode());
		result = prime * result + babNumber;
		result = prime * result + bookId;
		result = prime * result + ((bookName == null) ? 0 : bookName.hashCode());
		result = prime * result + ((hadithNumber == null) ? 0 : hadithNumber.hashCode());
		result = prime * result + id;
		result = prime * result + inBookNumber;
		result = prime * result + ((text == null) ? 0 : text.hashCode());
		return result; */
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
		if (arabicId != other.arabicId)
			return false;
		if (babName == null) {
			if (other.babName != null)
				return false;
		} else if (!babName.equals(other.babName))
			return false;
		if (babNumber != other.babNumber)
			return false;
		if (bookId != other.bookId)
			return false;
		if (bookName == null) {
			if (other.bookName != null)
				return false;
		} else if (!bookName.equals(other.bookName))
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
		if (text == null) {
			if (other.text != null)
				return false;
		} else if (!text.equals(other.text))
			return false;
		return true;
	}
	
	
	
	public Narration() {
		super();
	}
	
	
	public Narration(int arabicId, String babName, int babNumber, int bookId, String bookName, String hadithNumber,
			int id, int inBookNumber, String text) {
		super();
		this.arabicId = arabicId;
		this.babName = babName;
		this.babNumber = babNumber;
		this.bookId = bookId;
		this.bookName = bookName;
		this.hadithNumber = hadithNumber;
		this.id = id;
		this.inBookNumber = inBookNumber;
		this.text = text;
	}
}