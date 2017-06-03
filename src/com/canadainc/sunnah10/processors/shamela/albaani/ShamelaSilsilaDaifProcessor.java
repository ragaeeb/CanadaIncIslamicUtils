package com.canadainc.sunnah10.processors.shamela.albaani;

import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.json.simple.JSONObject;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import com.canadainc.sunnah10.Narration;
import com.canadainc.sunnah10.processors.shamela.AbstractShamelaProcessor;
import com.canadainc.sunnah10.processors.shamela.ShamelaUtils;

public class ShamelaSilsilaDaifProcessor extends AbstractShamelaProcessor
{
	public static final int[] GRADELESS_AHADEETH = new int[]{123,1038,2859,3615,3622,3697,3763,4022,4301,4387,4492,4604,4793,5209,5406,5501,5797,5961,6204};
	private static final String[] GRADES = ShamelaUtils.sortLongestToShortest("لا يصح", "لا أعلم له أصلا", "كذب", "كذب لا أصل له", "شاذ لا يصح", "مدرج الشطر الآخر",
			"ليس بحديث", "منكر بذكر الملكين", "منكر جداً بزيادة: (وواحدة)", "موضوع بهذا التمام",
			"ضعيف جدًا - أو موضوع بهذا السياق والتمام", "ضعيف جداً", "موضوع بهذا السياق", "ضعيف بهذا التمام",
			"باطل بهذا اللفظ", "منكر بذكر الفقرة", "باطل مرفوعا", "منكر جداً", "ضعيف جدا", "منكر، ضعيف الإسناد",
			"موضوع", "باطل", "منكر", "ضعيف", "لا أصل له", "لا أعرف له أصلا", "شاذ", "موقوف ضعيف", "لم أجده بهذا اللفظ",
			"موقوف", "ضعف جدا", "موضع", "ضهعيف", "مُنْكَرٌ", "مُنْكَرٌ بِذِكْرِ مِصْرَ", "ضَعِيفٌ", "مَوْضُوعٌ",
			"صعيف", "غريب", "لاأصل له مرفوعاً", "لاأصل بهذا اللفظ", "مقطوع ضعيف", "لا أعرفه مرفوعا", "َضعيف جداً",
			"ضعيف جداً أو مو", "متروك", "قلت: وهو متروك", "قلت: وهذا موضوع", "قلت: وهذا إسناد ضعيف جداً أو موضوع"
			);

	public ShamelaSilsilaDaifProcessor()
	{
		m_typos.ignore(140,6829,6830,6831);
		
		m_typos.add(6426, "<span class=\"title\">4835/ م</span>", "<span class=\"red\">4835 - </span>");
		m_typos.add(7130, "5179 (1) - ", "<span class=\"red\">5179 - </span>");
		m_typos.add(10865, "6887 (**) - ", "<span class=\"red\">6887 - </span>");
		m_typos.add(10965, "6930 (*) - ", "<span class=\"red\">6930 - </span>");
		m_typos.add(11260, "7150 (*) - ", "<span class=\"red\">7150 - </span>");
		m_typos.add(11226, "قلت: منكر جداً", "منكر جداً");
		m_typos.add(3450, "الغيب) .", "الغيب) .<br />موضوع");
		m_typos.add(4865, "شاركه) .", "شاركه) .<br />ضعيف");
		m_typos.add(8494, "فقال:. . . فذكره.", "فقال:. . . فذكره.<br />ضعيف جدا");
		m_typos.add(8566, " .: ", "<br />");
		m_typos.add(8262, "ضعيف:", "ضعيف.");

		m_typos.addRegexed(11224, "<span class=\"red\">[1-9]{1,2} - </span>", "");
		m_typos.addRegexed(11225, "<span class=\"red\">[1-9]{1,2} - </span>", "");
		m_typos.addRegexed(11224, "<br />", " ");
		m_typos.addRegexed(11225, "<br />", " ");

		m_typos.addContentStripper(8978, "(ألا إِنَّهُ", "الحديث", "...", "...", true);
		m_typos.addNumericListStripper(3299, "ثلاثة لا يكترثون للحساب", "وحق مواليه من نفسه", false);
		m_typos.addNumericListStripper(10495, "1 - مَنْ أَنْظَرَ مُعْسِرًا", "اللَّهُ جَوْفَهُ إِيمَانًا");
		m_typos.addNumericListStripper(9777, "إِنْ صَلَّيْتَ الضُّحَى", "أَنْ يُلْهِمَهُ ذِكْرَهُ)");
		m_typos.addNumericListStripper(10489, "مَا نَقَصَتْ صَدَقَةٌ", "عَلَيْهِ بَابَ فَقْرٍ");
		m_typos.addNumericListStripper(8195, "مَنْ لَبِسَ الصُّوفَ، وانْتَعَلَ", "وجل إلا أَكَبَّهُ اللهُ عز وجل");
	}


	@Override
	public void process(List<Node> nodes, JSONObject json)
	{
		Narration n = null;
		int nodeSize = nodes.size();

		for (int i = 0; i < nodeSize; i++)
		{
			Node e = nodes.get(i);

			if ( ShamelaUtils.isHadithNumberNode(e) && !ShamelaUtils.isHadithRangeNode(e) && ShamelaUtils.isHadithNumberValid(e, m_narrations, n) )
			{
				n = ShamelaUtils.createNewNarration(n, e, m_narrations);

				if (i+1 < nodeSize)
				{
					++i;
					e = nodes.get(i);
					n.text = ShamelaUtils.extractRoundHadith(e);

					if (i+2 < nodeSize)
					{
						e = nodes.get(i+2);

						if ( ShamelaUtils.isTextNode(e) )
						{
							i += 2;
							String grade = ((TextNode)e).text().trim();
							attachCommentary(grade, n);
						}
					}
				}
			} else if ( ShamelaUtils.isTextNode(e) ) {
				String commentary = ((TextNode)e).text();

				if (n != null) {
					attachCommentary(commentary, n);
				} else if ( !m_narrations.isEmpty() ) {
					attachCommentaryToPrev(commentary);
				}
			} else if ( ShamelaUtils.isFootnote(e) ) {
				String commentary = ShamelaUtils.extractText(e);

				if (n != null) {
					n.commentary += commentary;
				} else if ( !m_narrations.isEmpty() ) {
					attachCommentaryToPrev(commentary);
				}
			}
		}

		ShamelaUtils.appendIfValid(n, m_narrations);
	}


	private String bodyStartsWithGrade(String body)
	{
		for (String grade: GRADES)
		{
			if ( body.startsWith(grade) ) {
				return grade;
			}
		}

		return null;
	}


	private void attachCommentaryToPrev(String body)
	{
		Narration n = m_narrations.get( m_narrations.size()-1 );
		attachCommentary(body, n);
	}


	private void attachCommentary(String body, Narration n)
	{
		if ( ArrayUtils.contains(GRADES, body.replace(".", "")) ) {
			int endIndex = body.length();
			n.grading = body.substring(0, body.endsWith(".") ? endIndex-1 : endIndex).trim(); // remove the period
		} else if (n.grading == null && ( bodyStartsWithGrade(body) != null ) ) {
			String match = bodyStartsWithGrade(body);
			int index = body.substring( match.length() ).indexOf(" ");
			String grade = body.substring(0, match.length()+index);
			n.grading = grade.substring(0, grade.endsWith(".") ? grade.length()-1 : grade.length()).trim();
			n.commentary += body.substring(index+1);
		} else if ( (n.grading != null) || !hasGrade(n.id) ) {
			n.commentary += body;
		} else {
			n.text += body;
		}
	}

	@Override
	public boolean hasGrade(int id) {
		return !ArrayUtils.contains(GRADELESS_AHADEETH, id);
	}
}