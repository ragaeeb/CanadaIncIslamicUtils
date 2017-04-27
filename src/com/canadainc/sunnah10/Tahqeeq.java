package com.canadainc.sunnah10;

public class Tahqeeq
{
	public String collectionSignature;
	public String gradeFootnoteSignature;
	public int muhaddith;

	public Tahqeeq(String collectionSignature, String gradeFootnoteSignature, int muhaddith)
	{
		super();
		this.collectionSignature = collectionSignature;
		this.gradeFootnoteSignature = gradeFootnoteSignature;
		this.muhaddith = muhaddith;
	}
	
	
	public Tahqeeq(String collectionSignature, String gradeFootnoteSignature) {
		this(collectionSignature, gradeFootnoteSignature, 0);
	}
}