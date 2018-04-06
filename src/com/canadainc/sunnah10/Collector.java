package com.canadainc.sunnah10;

import java.util.Collection;

public interface Collector
{
	void process(Collection<Narration> narrations, String language, String collection);
}