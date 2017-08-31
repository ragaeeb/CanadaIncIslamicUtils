package com.canadainc.sunnah10.processors.shamela;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import com.canadainc.islamicutils.io.DBUtils;
import com.canadainc.sunnah10.Narration;
import com.canadainc.sunnah10.processors.Processor;
import com.canadainc.sunnah10.utils.SunnahUtils;

public class SunnahComPopulator extends ShamelaPopulator
{
	private String m_collection;
	private static final String TABLE_NAME = "narrations";
	private static final String TARGET_DB_NAME = "target";

	public SunnahComPopulator(String collection, Processor processor)
	{
		super("sunnah_com", "english/"+collection, processor);

		m_collection = collection;
	}


	/* (non-Javadoc)
	 * @see com.canadainc.sunnah10.processors.shamela.ShamelaPopulator#write(java.sql.Connection)
	 */
	@Override
	public void write(Connection c) throws SQLException
	{
		c.setAutoCommit(true);

		System.out.println("Creating database...");			

		DBUtils.attach(c, "sunnah_com_"+m_collection+".db", TARGET_DB_NAME);

		c.setAutoCommit(false);

		List<String> columns = DBUtils.createNotNullColumns("id INTEGER", "indexed_number INTEGER", "translation TEXT");
		DBUtils.createTable(c, TARGET_DB_NAME+"."+TABLE_NAME, columns);		
		PreparedStatement ps = DBUtils.createInsert(c, TARGET_DB_NAME+"."+TABLE_NAME, Arrays.asList("indexed_number", "translation"));
		
		for (Narration n: m_processor.getNarrations())
		{
			n.id = SunnahUtils.parseHadithNumber(n);
			
			String toConvert = n.text;

			if ( toConvert.endsWith("</b>") ) {
				toConvert = toConvert.substring( 0, toConvert.lastIndexOf("</b>") );
			}
			
			toConvert = toConvert.replaceAll("\\(S\\)|\\[SAW\\]|\\([sS]\\.[aA]\\.[wW]\\)|\\(SAW0{0,1}\\)|\\(saws\\)|SAW0|\\({0,1}SWAS\\){0,1}|\\(saW\\)|\\(saas\\)|[pP]\\.[bB]\\.[uU]\\.[hH]\\.{0,1}|pbuh", "ﷺ");
			toConvert = toConvert.replaceAll("\\s+", " ");
			toConvert = toConvert.replaceAll("<[^>]*>", "");

			if ( toConvert.startsWith("\"") && toConvert.endsWith("\"") ) {
				toConvert = toConvert.substring( 1, toConvert.length()-1 );
			}

			if ( toConvert.startsWith("\"") ) {
				toConvert = toConvert.substring(1);
			}

			n.text = toConvert.trim();
		}
		
		List<Narration> narrations = SunnahUtils.sort(m_processor.getNarrations(), true);

		for (Narration n: narrations)
		{
			int i = 0;

			ps.setInt(++i, n.id);
			ps.setString(++i, n.text.trim());

			ps.execute();
		}

		c.commit();
		ps.close();
	}
}