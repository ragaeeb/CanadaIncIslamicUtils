package com.canadainc.sunnah10;

import java.util.Collection;

public interface Collector
{
	void process(Collection<Narration> narrations, boolean arabic, String collection);
	
	void setDictionary(Dictionary d);
}