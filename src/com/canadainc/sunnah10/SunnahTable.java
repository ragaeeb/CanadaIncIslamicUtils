package com.canadainc.sunnah10;

import java.sql.Connection;
import java.sql.SQLException;

public interface SunnahTable {

	void setConnection(Connection c);

	String getTableName();

	void setLanguage(String language);

	void createIndices() throws SQLException;
}