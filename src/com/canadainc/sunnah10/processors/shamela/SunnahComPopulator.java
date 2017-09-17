package com.canadainc.sunnah10.processors.shamela;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.canadainc.common.text.TextUtils;
import com.canadainc.sunnah10.Narration;
import com.canadainc.sunnah10.processors.Processor;
import com.canadainc.sunnah10.processors.sunnah.com.AbstractSunnahDotComProcessor;
import com.canadainc.sunnah10.utils.SunnahUtils;

public class SunnahComPopulator implements DatabasePopulator
{
	private AbstractSunnahDotComProcessor m_processor;
	private String m_collection;

	public SunnahComPopulator(String collection, AbstractSunnahDotComProcessor processor)
	{
		m_processor = processor;
		m_collection = collection;
	}


	/* (non-Javadoc)
	 * @see com.canadainc.sunnah10.processors.shamela.ShamelaPopulator#write(java.sql.Connection)
	 */
	@Override
	public void write(Connection c) throws SQLException
	{
		PreparedStatement ps = DatabaseUtils.createPopulation(m_collection, c, "id", "translation", "commentary");

		for (Narration n: m_processor.getNarrations())
		{
			n.text = cleanText(n.text);
			n.translation = cleanText(n.translation);
		}

		List<Narration> narrations = SunnahUtils.sort(m_processor.getNarrations(), true);

		for (Narration n: narrations)
		{
			int i = 0;

			ps.setInt(++i, SunnahUtils.parseHadithNumber(n));
			ps.setString(++i, n.text.trim());
			ps.setString(++i, TextUtils.normalize(n.text.trim()));
			ps.setInt(++i, n.inBookNumber);
			ps.setString(++i, n.grading);
			ps.setInt(++i, n.id);
			ps.setString(++i, n.translation);

			if (n.commentary != null) {
				ps.setString(++i, n.commentary);
			} else {
				ps.setNull(++i, Types.OTHER);
			}

			ps.execute();
		}

		DatabaseUtils.createIndex(c);

		c.commit();
		ps.close();
	}


	private String cleanText(String text)
	{
		String toConvert = text;

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

		return toConvert.trim();
	}


	@Override
	public void process(Connection c) throws Exception
	{
		Map<Integer,Integer> idToTranslation = new HashMap<>();
		Map<Integer,Narration> arabicIdToNarration = new HashMap<>();
		Map<Integer,Narration> englishIdToNarration = new HashMap<>();
		Map<Integer,Narration> translated = new HashMap<>();

		populateCollection(c, "arabic", idToTranslation, arabicIdToNarration);
		populateCollection(c, "english", idToTranslation, englishIdToNarration);

		for ( int id: arabicIdToNarration.keySet() )
		{
			Integer translationId = idToTranslation.get(id);
			
			if (translationId != null)
			{
				Narration arabic = arabicIdToNarration.get(id);
				Narration english = englishIdToNarration.get(translationId);

				arabic.translation = english.text;
				translated.put(id, arabic);
			}
		}

		for ( int id: englishIdToNarration.keySet() )
		{
			int arabicId = idToTranslation.get(id);

			if ( !translated.containsKey(arabicId) )
			{
				Narration arabic = arabicIdToNarration.get(arabicId);
				Narration english = englishIdToNarration.get(id);
				
				if (arabic == null) {
					System.out.println(arabicId);
				}
				
				arabic.translation = english.text;

				translated.put(arabicId, arabic);
			}
		}

		m_processor.getNarrations().addAll( translated.values() );
	}


	private void populateCollection(Connection c, String language, Map<Integer,Integer> idToTranslation, Map<Integer,Narration> idToNarration) throws SQLException, Exception
	{
		PreparedStatement ps = c.prepareStatement("SELECT * FROM sunnah_com WHERE path=? ORDER BY id");
		ps.setString(1, language+"/"+m_collection);

		ResultSet rs = ps.executeQuery();

		int lastSize = 0;

		while ( rs.next() )
		{
			Object obj = JSONValue.parse( rs.getString("json") );
			JSONArray arr = new JSONArray();

			if (obj instanceof JSONArray) {
				arr = (JSONArray)obj;
			} else {
				arr.add(obj);
			}

			for (Object o: arr)
			{
				JSONObject json = (JSONObject)o;

				if (json == null) {
					System.err.println("Invalid JSON: "+rs.getString("file_name")+"; "+rs.getInt("id"));
				}			

				boolean process = m_processor.preprocess(json);

				if (process)
				{
					int pageNumber = m_processor.getPageNumber(json);

					try {
						m_processor.process(json);
					} catch (Exception ex) {
						System.err.println("ErrorOnPage: "+pageNumber);
						ex.printStackTrace();
						throw ex;
					}

					List<Narration> narrations = m_processor.getNarrations();
					int newSize = narrations.size();

					for (int i = lastSize; i < newSize; i++) {
						narrations.get(i).pageNumber = pageNumber;
					}

					lastSize = newSize;
				}
			}
		}

		ps.close();
		rs.close();

		for ( Narration n: m_processor.getNarrations() ) {
			idToNarration.put(n.id, n);
		}

		idToTranslation.putAll( m_processor.getTranslations() );

		m_processor.getNarrations().clear();
		m_processor.getTranslations().clear();
	}
}