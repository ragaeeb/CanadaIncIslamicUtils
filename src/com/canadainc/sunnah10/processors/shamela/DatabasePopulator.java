package com.canadainc.sunnah10.processors.shamela;

import java.sql.Connection;
import java.sql.SQLException;

public interface DatabasePopulator
{
	void process(Connection c) throws Exception;

	void write(Connection c) throws SQLException;
}