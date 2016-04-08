/**
 * 
 */
package com.canadainc.sunnah10;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.jsoup.helper.StringUtil;

import com.canadainc.islamicutils.io.DBUtils;

/**
 * @author rhaq
 *
 */
public class SunnahGroupsTable implements SunnahPrimaryTable<RelatedNarration>
{
	private Connection m_c;
	private Map<Integer,Integer> m_idToGroup;
	
	/**
	 * 
	 */
	public SunnahGroupsTable()
	{
		m_idToGroup = new HashMap<Integer,Integer>();
	}
	

	/* (non-Javadoc)
	 * @see com.canadainc.sunnah10.SunnahTable#setConnection(java.sql.Connection)
	 */
	@Override
	public void setConnection(Connection c) {
		m_c = c;
	}

	/* (non-Javadoc)
	 * @see com.canadainc.sunnah10.SunnahTable#getTableName()
	 */
	@Override
	public String getTableName() {
		return "grouped_narrations";
	}

	/* (non-Javadoc)
	 * @see com.canadainc.sunnah10.SunnahTable#setLanguage(java.lang.String)
	 */
	@Override
	public void setLanguage(String language) {}

	/* (non-Javadoc)
	 * @see com.canadainc.sunnah10.SunnahTable#createIndices()
	 */
	@Override
	public void createIndices() throws SQLException
	{
	}


	@Override
	public int getIdFor(RelatedNarration x)
	{
		Integer i = m_idToGroup.get(x.narrationId);
		
		if (i == null) {
			i = m_idToGroup.get(x.otherId);
		}
		
		return i == null ? 0 : i.intValue();
	}


	@Override
	public void process(Collection<RelatedNarration> elements) throws SQLException
	{
		Map<Integer, Collection<Integer>> narrationToGroup = new HashMap<Integer, Collection<Integer>>();
		ArrayList< Collection<Integer> > groups = new ArrayList< Collection<Integer> >();
		
		for (RelatedNarration r: elements)
		{
			int a = r.narrationId;
			int b = r.otherId;
			
			Collection<Integer> group = narrationToGroup.get(a);
			
			if (group == null) {
				group = narrationToGroup.get(b);
			}
			
			if (group == null) {
				group = new HashSet<Integer>();
				groups.add(group);
			}
			
			group.add(a);
			group.add(b);
			narrationToGroup.put(a, group);
			narrationToGroup.put(b, group);
		}
		
		ArrayList<String> columns = new ArrayList<String>();
		columns.add("id INTEGER PRIMARY KEY");
		columns.add("narration_id INTEGER NOT NULL");
		columns.add("group_number INTEGER NOT NULL");
		columns.add("link_type INTEGER");
		
		PreparedStatement ps = m_c.prepareStatement("DROP TABLE IF EXISTS "+getTableName());
		ps.execute();
		
		DBUtils.createTable(m_c, getTableName(), columns);
		
		ps = m_c.prepareStatement("INSERT INTO "+getTableName()+" (narration_id,group_number) VALUES (?,?)");
		
		for (int i = 0; i < groups.size(); i++)
		{
			ArrayList<Integer> narrations = new ArrayList<Integer>();
			narrations.addAll( groups.get(i) );
			Collections.sort(narrations);
			
			for (Integer id: narrations)
			{
				int group = i+1;
				
				int j = 0;
				ps.setInt(++j, id);
				ps.setInt(++j, group);
				ps.execute();
				
				m_idToGroup.put(id,group);
			}
		}
		
		m_c.commit();
		ps.close();
	}
}