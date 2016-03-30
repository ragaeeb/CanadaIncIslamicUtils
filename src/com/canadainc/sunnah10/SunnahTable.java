package com.canadainc.sunnah10;

import java.sql.Connection;

public interface SunnahTable {

	void setConnection(Connection c);

	String getTableName();

	void setLanguage(String language);

}