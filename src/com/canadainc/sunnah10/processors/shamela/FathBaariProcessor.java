package com.canadainc.sunnah10.processors.shamela;

import java.util.List;

import org.json.simple.JSONObject;
import org.jsoup.nodes.Node;

import com.canadainc.common.text.TextUtils;
import com.canadainc.sunnah10.Narration;

public class FathBaariProcessor extends AbstractShamelaProcessor
{
	private Narration m_current;


	public FathBaariProcessor()
	{
	}

	@Override
	public void process(List<Node> nodes, JSONObject json)
	{
		for (Node e: nodes)
		{
			if ( ShamelaUtils.isTextNode(e) )
			{
				String body = e.toString().trim();

				if ( body.contains(" ") )
				{
					String index = body.substring(0, body.indexOf(" "));

					if ( index.matches("\\[\\d+\\]$") )
					{
						int hadithNum = Integer.parseInt( TextUtils.extractInside(index, "[", "]") );

						if (m_current != null && m_current.id < hadithNum) { // new hadith encountered, wrap up previous
							addCurrent();
							m_current = null;
						}

						if ( m_current == null && ( m_narrations.isEmpty() || validDiff(hadithNum) ) )
						{
							m_current = new Narration();
							m_current.id = hadithNum;
							m_current.text = body.substring( body.indexOf("]")+1 );
						} else if (m_current != null) {
							m_current.text += body;
						}
					}
				}

				if (m_current != null) {
					m_current.text += body;
				}
			} else if ( ShamelaUtils.isTitleSpan(e) ) {
				addCurrent();
				m_current = null;
			}
		}

		addCurrent();
	}

	private boolean validDiff(int hadithNum) {
		return ( getPrev().id < hadithNum ) && ( hadithNum-getPrev().id < 50 ); // not a huge jump
	}

	private void addCurrent()
	{
		if ( m_narrations.isEmpty() || ( m_current != null && ( getPrev() != m_current ) ) ) {
			m_narrations.add(m_current);
		}
	}
}