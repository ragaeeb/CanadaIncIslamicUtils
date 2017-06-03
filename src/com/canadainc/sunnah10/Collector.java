package com.canadainc.sunnah10;

import java.util.Collection;

import com.canadainc.sunnah10.utils.Dictionary;

public interface Collector
{
	void process(Collection<Narration> narrations, String language, String collection);
	
	void setDictionary(Dictionary d);
}