package com.canadainc.sunnah10.processors.shamela;

import java.util.List;

import org.json.simple.JSONObject;
import org.jsoup.nodes.Node;

import com.canadainc.sunnah10.Narration;

public class ShamelaIbaanahProcessor extends AbstractShamelaProcessor
{
	private ShamelaProcessor m_processor = new AbstractShamelaProcessor();

	private int m_counter;

	@Override
	public void process(List<Node> nodes, JSONObject json)
	{
		int currentSize = getNarrations().size();
		m_processor.process(nodes, json);

		int newSize = getNarrations().size();

		for (int i = currentSize; i < newSize; i++)
		{
			Narration n = getNarrations().get(i);
			n.inBookNumber = n.id;
			n.id = ++m_counter;
		}
	}

	/* (non-Javadoc)
	 * @see com.canadainc.sunnah10.processors.shamela.AbstractShamelaProcessor#getNarrations()
	 */
	@Override
	public List<Narration> getNarrations() {
		return m_processor.getNarrations();
	}
}