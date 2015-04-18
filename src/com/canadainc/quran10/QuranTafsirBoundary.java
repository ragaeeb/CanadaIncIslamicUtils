package com.canadainc.quran10;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class QuranTafsirBoundary
{
	private Connection m_connection;
	
	public QuranTafsirBoundary(String dbPath) throws SQLException, ClassNotFoundException
	{
		Class.forName("org.sqlite.JDBC"); // load the sqlite-JDBC driver using the current class loader
		m_connection = DriverManager.getConnection("jdbc:sqlite:"+dbPath);
	}
	
	
	public void createIndices() throws SQLException
	{
		String[] statements = {
				"CREATE INDEX IF NOT EXISTS suites_index ON suites(author,translator,explainer);",
				"CREATE INDEX IF NOT EXISTS suite_pages_index ON suite_pages(suite_id);",
				"CREATE INDEX IF NOT EXISTS quotes_index ON quotes(author);",
				"CREATE INDEX IF NOT EXISTS explanations_index ON explanations(surah_id,from_verse_number,to_verse_number,suite_page_id);",
				"CREATE INDEX IF NOT EXISTS individuals_index ON individuals(birth,death,female,location);",
				"CREATE INDEX IF NOT EXISTS mentions_index ON mentions(from_id,target,points);",
				"CREATE INDEX IF NOT EXISTS websites_index ON websites(individual);",
				"CREATE INDEX IF NOT EXISTS teachers_index ON teachers(individual, teacher);"
		};

		execute(statements);
	}
	
	
	public void createTable() throws SQLException
	{
		String[] statements = {
				"CREATE TABLE individuals (id INTEGER PRIMARY KEY, prefix TEXT, name TEXT, kunya TEXT, hidden INTEGER, birth INTEGER, death INTEGER, female INTEGER, displayName TEXT, location INTEGER REFERENCES locations(id) ON DELETE SET NULL ON UPDATE CASCADE);",
				"CREATE TABLE suites (id INTEGER PRIMARY KEY, author INTEGER REFERENCES individuals(id) ON DELETE CASCADE ON UPDATE CASCADE, translator INTEGER REFERENCES individuals(id) ON DELETE CASCADE ON UPDATE CASCADE, explainer INTEGER REFERENCES individuals(id) ON DELETE CASCADE ON UPDATE CASCADE, title TEXT NOT NULL, description TEXT, reference TEXT);",
				"CREATE TABLE suite_pages (id INTEGER PRIMARY KEY, suite_id INTEGER REFERENCES suites(id) ON DELETE CASCADE, body TEXT NOT NULL, heading TEXT, reference TEXT);",
				"CREATE TABLE explanations (id INTEGER PRIMARY KEY, surah_id INTEGER NOT NULL, from_verse_number INTEGER, to_verse_number INTEGER, suite_page_id INTEGER NOT NULL REFERENCES suite_pages(id) ON DELETE CASCADE ON UPDATE CASCADE, UNIQUE(surah_id, from_verse_number, suite_page_id) ON CONFLICT REPLACE);",
				"CREATE TABLE quotes (id INTEGER PRIMARY KEY, author INTEGER REFERENCES individuals(id) ON DELETE CASCADE ON UPDATE CASCADE, body TEXT, reference TEXT);",
				"CREATE TABLE companions (id INTEGER PRIMARY KEY REFERENCES individuals(id) ON DELETE CASCADE ON UPDATE CASCADE);",
				"CREATE TABLE mentions (id INTEGER PRIMARY KEY, from_id INTEGER REFERENCES individuals(id) ON DELETE CASCADE ON UPDATE CASCADE, target INTEGER REFERENCES individuals(id) ON DELETE CASCADE ON UPDATE CASCADE, body TEXT, reference TEXT, points INTEGER);",
				"CREATE TABLE websites (id INTEGER PRIMARY KEY, individual INTEGER REFERENCES individuals(id) ON DELETE CASCADE ON UPDATE CASCADE, uri TEXT, UNIQUE(individual, uri) ON CONFLICT IGNORE );",
				"CREATE TABLE teachers (individual INTEGER REFERENCES individuals(id) ON DELETE CASCADE, teacher INTEGER REFERENCES individuals(id) ON DELETE CASCADE, UNIQUE(individual,teacher) ON CONFLICT IGNORE );",
				"CREATE TABLE locations (id INTEGER PRIMARY KEY, city TEXT NOT NULL UNIQUE ON CONFLICT IGNORE, latitude REAL NOT NULL, longitude REAL NOT NULL);"
		};

		execute(statements);
	}
	
	
	public void copyFromMaster(String master) throws SQLException
	{
		String[] statements = {
				"ATTACH DATABASE '"+master+"' AS master;",
				"INSERT INTO individuals SELECT * FROM master.individuals WHERE id IN (SELECT id FROM master.individuals WHERE id NOT IN (SELECT id FROM individuals));",
				"INSERT OR IGNORE INTO companions SELECT * FROM master.companions;",
				"INSERT OR IGNORE INTO teachers SELECT * FROM master.teachers;",
				"INSERT OR IGNORE INTO websites SELECT * FROM master.websites;",
				"INSERT OR IGNORE INTO locations SELECT * FROM master.locations;",
				"DETACH DATABASE master;"
		};
		
		execute(statements);
	}
	
	
	public void execute(String statement) throws SQLException
	{
		String[] statements = {
				statement
		};
		
		execute(statements);
	}
	
	
	public void close() throws SQLException
	{
		m_connection.close();
	}
	
	
	public void execute(String[] statements) throws SQLException
	{
		for (String s: statements)
		{
			PreparedStatement ps = m_connection.prepareStatement(s);
			ps.execute();
			ps.close();
		}
	}
}