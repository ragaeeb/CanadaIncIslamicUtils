package com.canadainc.sunnah10;

import java.sql.SQLException;
import java.util.Collection;

/**
 * Populates a table in the database.
 * @author rhaq
 *
 * @param <E>
 */
public interface SunnahPrimaryTable<E> extends SunnahTable
{
	public int getIdFor(E x);
	public void process(Collection<E> elements) throws SQLException;
}