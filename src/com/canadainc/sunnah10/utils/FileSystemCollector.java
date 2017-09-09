package com.canadainc.sunnah10.utils;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import org.jsoup.helper.StringUtil;

import com.canadainc.common.io.DBUtils;
import com.canadainc.common.io.IOUtils;
import com.canadainc.islamicutils.io.NetworkBoundary;

public class FileSystemCollector
{
	private String m_folder;
	private File[] m_result;

	public FileSystemCollector(String folder) {
		m_folder = folder;
	}


	public void collect() throws IOException
	{
		m_result = new File(m_folder).listFiles( new FilenameFilter()
		{
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".txt");
			}
		});
	}
	
	
	public void fixCorruption(Connection connection, int bookId, int ...pages) throws SQLException, IOException
	{
		connection.setAutoCommit(false);
		
		String table = new File(m_folder).getName();
		PreparedStatement ps = connection.prepareStatement("UPDATE "+table+" SET json=?");
		
		for (int page: pages)
		{
			String json = NetworkBoundary.getHTML("http://shamela.ws/browse.php/book/get_page/"+String.valueOf(bookId)+"/"+String.valueOf(page) );
			ps.setString(1, json);
			ps.execute();
		}
		
		connection.commit();
		ps.close();
	}


	public void writeToDB(Connection connection) throws SQLException, IOException
	{
		connection.setAutoCommit(false);
		
		List<String> columns = DBUtils.createNotNullColumns("id INTEGER", "file_name TEXT", "json TEXT");
		DBUtils.createNullColumns(columns, "path TEXT");
		
		String table = new File(m_folder).getName();
		DBUtils.createTable(connection, table, columns);

		DBUtils.isolateColumnNames(columns, "id");
		PreparedStatement ps = connection.prepareStatement("INSERT INTO "+table+" ("+StringUtil.join(columns, ",")+") VALUES "+DBUtils.generatePlaceHolders(columns));

		for (File f: m_result)
		{
			String parentPath = f.getParent().substring( m_folder.length() ).trim();
			
			int i = 0;
			ps.setString(++i, f.getName());
			ps.setString(++i, IOUtils.readFileUtf8(f));
			
			if ( !parentPath.isEmpty() ) {
				ps.setString(++i, parentPath);
			} else {
				ps.setNull(++i, Types.OTHER);
			}

			ps.execute();
		}

		connection.commit();
		ps.close();
	}


	/**
	 * @return the result
	 */
	File[] getResult() {
		return m_result;
	}
}