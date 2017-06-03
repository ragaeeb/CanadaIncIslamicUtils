package com.canadainc.sunnah10;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.text.WordUtils;

import com.canadainc.sunnah10.utils.Dictionary;

public class BookCollector implements Collector
{
	/** (Key: Collection, Value: Book) */
	private Map<String,Set<Book>> m_bookNames;
	private Dictionary m_dictionary;
	private Map<Integer, String> adabArabicBookNames;

	public BookCollector()
	{
		adabArabicBookNames = new HashMap<Integer, String>();
		adabArabicBookNames.put(1, "كتاب الْوَالِدَيْنِ");
		adabArabicBookNames.put(2, "كتاب صِلَةِ الرَّحِمِ");
		adabArabicBookNames.put(3, "كتاب مَوَالِي");
		adabArabicBookNames.put(6, "كتاب الْجَارِ");
		adabArabicBookNames.put(7, "كتاب الْكَرَمِ وَ يَتِيمٌ");
		adabArabicBookNames.put(11, "كتاب الْمَعْرُوفِ");
		adabArabicBookNames.put(12, "كتاب الِانْبِسَاطِ إِلَى النَّاسِ");
		adabArabicBookNames.put(13, "كتاب الْمَشُورَةِ");
		adabArabicBookNames.put(17, "كتاب الزِّيَارَةِ");
		adabArabicBookNames.put(18, "كتاب الأكَابِرِ");
		adabArabicBookNames.put(19, "كتاب الصَّغِيرِ");
		adabArabicBookNames.put(20, "كتاب رَحْمَةِ");
		adabArabicBookNames.put(24, "كتاب السِّبَابِ");
		adabArabicBookNames.put(25, "كتاب السَّرَفِ فِي الْبِنَاءِ");
		adabArabicBookNames.put(26, "كتاب الرِّفْقِ");
		adabArabicBookNames.put(28, "كتاب الظُّلْم");
		adabArabicBookNames.put(33, "كتاب الأقوال");
		adabArabicBookNames.put(34, "كتاب الأسْمَاءِ");
		adabArabicBookNames.put(35, "كتاب الكُنْيَةِ");
		adabArabicBookNames.put(36, "كتاب الشِّعْرِ");
		adabArabicBookNames.put(37, "كتاب الْكَلامِ");
		adabArabicBookNames.put(40, "كتاب الْعُطَاسَ والتثاؤب");
		adabArabicBookNames.put(42, "كتاب السَّلامِ");
		adabArabicBookNames.put(43, "كتاب الاسْتِئْذَانُ");
		adabArabicBookNames.put(44, "كتاب أَهْلِ الْكِتَابِ");
		adabArabicBookNames.put(45, "كتاب الرَّسَائِلِ‏");
		adabArabicBookNames.put(46, "كتاب الْمَجَالِسِ");
		adabArabicBookNames.put(49, "كتاب الصباح والمساء");
		adabArabicBookNames.put(51, "كتاب الْبَهَائِمِ");
		adabArabicBookNames.put(53, "كتاب الْخِتَانِ");

		m_bookNames = new HashMap<String,Set<Book>>();
	}


	/*
	 * (non-Javadoc)
	 * @see com.canadainc.sunnah10.Collector#process(java.util.Collection, boolean, java.lang.String)
	 */
	public void process(Collection<Narration> narrations, String language, String collection)
	{
		Set<Book> names = m_bookNames.get(collection);

		if (names == null) {
			names = new HashSet<Book>();
		}

		for (Narration n: narrations)
		{
			correctBook(n, language, collection);
			names.add(n.book);
		}

		m_bookNames.put(collection, names);
	}


	public void correctBook(Narration n, String language, String collection)
	{
		boolean arabic = language.equals("arabic");
		String bookName = n.book.name;
		int bookID = n.book.id;

		if ( ( bookName == null || bookName.isEmpty() ) && collection.equals("nasai") ) {
			if (bookID == 47) {
				bookName = "كتاب الإيمان وشرائعه";
			} else if (bookID == 4) {
				bookName = "كتاب الغسل والتيمم";
			}
		} else if ( ( bookName == null || bookName.isEmpty() ) && collection.equals("nawawi40") ) {
			bookName = !arabic ? "40 Hadith Nawawi" : "الأربعون النووية";
		} else if ( ( bookName == null || bookName.isEmpty() ) && collection.equals("qudsi40") ) {
			bookName = !arabic ? "40 Hadith Qudsi" : "الحديث القدسي";
		} else {
			bookName = WordUtils.capitalizeFully(bookName);
			bookName = bookName.replace("\n", " ").trim();

			if ( bookName.equalsIgnoreCase("ok") || bookName.equalsIgnoreCase("Blank") && arabic && collection.equals("adab") )
			{
				bookName = adabArabicBookNames.get(bookID);

				if (bookName == null) {
					bookName = "كتاب";
				}
			}
			
			if (bookName == null || bookName.isEmpty()) {
				System.out.println("WARNING! "+bookName+"; "+bookID+"; "+collection);
			}
		}

		if (!arabic)
		{
			int transliterationIndex = bookName.indexOf("(");

			if (transliterationIndex >= 0)
			{
				int endIndex = bookName.indexOf(")", transliterationIndex);

				if (endIndex == -1) {
					endIndex = bookName.length();
				}

				n.book.transliteration = WordUtils.capitalizeFully( bookName.substring(transliterationIndex+1, endIndex) );
				bookName = bookName.substring(0, transliterationIndex);
			}
		}

		if (m_dictionary != null) {
			n.book.name = m_dictionary.correctTypos(bookName).trim();
		} else {
			n.book.name = bookName.trim();
		}

		// 55: Virtues And Merits Of The Prophet (Pbuh)
		// 60: Prophetic Commentary On The Qur'an (Tafseer Of The Prophet (pbuh)
		// 38: Setting Free And Wala\'

		// Chapters/babnames todo
		// TODO: 0; Hu
		// TODO: The Prophet (saw)
		//TODO: The Virtues Of Sahl Bin Sa'd (ra)
		//"no Hypocrite Loves 'ali, And No Believer Hates Him."
		//'ali Is From Me And I Am From 'ali
		// His (saw) Wish That Abu Bakr Be Among Those Who Are Called From All Of The Gates Of Paradise...
		//"stick To The Two After Me, Abu Bakr And 'umar"
		//0; Chapter. “the Mufarridun Have Preceded...”
		//Chapter (...) The Supplication: “i Seek Refuge In Allah’s Perfect Words From The Evil Of What He Created”
		//--
		//33; Regarding Surat Al-ahzab
		//0; Regarding Surat Al-ahzab
		//0; Regarding Surat Al-baqarah
		//0; In How Much Time May One Recite The Qur'an?
		//1; In How Much Time May One Recite The Qur'an?
		//20; 'whoever Recites The Qur'an, Then Let Him Ask Allah By It'
		//27; the Hadith: In Brief: "what Do I Have To Do With The World ! I Am Not In The World But As A Rider."
		//22; an Illustration About  The Length Of Life And A Person's Aspirations Increasing As He Becomes Old, And That He Will Become Old In The End
		//56; (what Has Been Related) About Having Patience With Afflictions
		//158; [what Has Been Related] About Voluntary Prayers While Sitting
		//0; , 49. What Is Mentioned In The Book Of Retaliation From Al-mujtaba Which Is Not Contained
		//47; , 48. One Who Takes His Right To Retaliation Without The Involvement Of The Ruler
		//46; ,47. Mentioning The Hadith Of 'amr Bin Hazm Concerning Blood Money, And Different Versions
		//44; 45.diyah For Fingers.
		//43; , 44. Diyah For Teeth
		//42; , 43. If A Sightless Eye That Looks Fine Is Destroyed
		//41; 42. Can Anyone Be Blamed For The Sin Of Another?
		//16; , 17. Al-qisas For A Tooth
		//0; Chapters 17, 18. Al-qisas For A Front Tooth
		//18; "go To Your Family" Does Not Necessarily Mean Divorce
		//0; Pre-emption And Its Rulings
		//0; What Was Mentioned Concerning Mina
		//0; The Talbiyah
		//0; Chapter8. Hajj On Behalf Of A Deceased Person Who Did Not Perform Hajj
		//0; Chapter73. One Who Asks By The Face Of Allah, The Mighty And Sublime
		//0; Chapter  75. The Reward Or One Who Gives
		//14; what Was Narrated Concerning The Collectors Of Zakat.
		//0; Chapter 5: Fighting The Offender And Killing The Apostate
		//0; Chapter 2: Types Of Diyah (blood Money)
		//0; Chapter 1
		//0; Chapter 1
		//0; Chapter 5: Exhortation To Have Good Morals
	}


	public Map< String, Set<Book> > getCollected() {
		return m_bookNames;
	}


	@Override
	public void setDictionary(Dictionary d) {
		m_dictionary = d;
	}
}