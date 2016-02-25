package com.canadainc.sunnah10;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.text.WordUtils;

public class NarrationDecorator
{
	private Pattern chapterPattern;
	private Map<Integer, String> adabArabicChapterNames;
	private Map<String, String> typos;

	public NarrationDecorator()
	{
		adabArabicChapterNames = new HashMap<Integer, String>();
		adabArabicChapterNames.put(1, "كتاب الْوَالِدَيْنِ");
		adabArabicChapterNames.put(2, "كتاب صِلَةِ الرَّحِمِ");
		adabArabicChapterNames.put(3, "كتاب مَوَالِي");
		adabArabicChapterNames.put(6, "كتاب الْجَارِ");
		adabArabicChapterNames.put(7, "كتاب الْكَرَمِ وَ يَتِيمٌ");
		adabArabicChapterNames.put(11, "كتاب الْمَعْرُوفِ");
		adabArabicChapterNames.put(12, "كتاب الِانْبِسَاطِ إِلَى النَّاسِ");
		adabArabicChapterNames.put(13, "كتاب الْمَشُورَةِ");
		adabArabicChapterNames.put(17, "كتاب الزِّيَارَةِ");
		adabArabicChapterNames.put(18, "كتاب الأكَابِرِ");
		adabArabicChapterNames.put(19, "كتاب الصَّغِيرِ");
		adabArabicChapterNames.put(20, "كتاب رَحْمَةِ");
		adabArabicChapterNames.put(24, "كتاب السِّبَابِ");
		adabArabicChapterNames.put(25, "كتاب السَّرَفِ فِي الْبِنَاءِ");
		adabArabicChapterNames.put(26, "كتاب الرِّفْقِ");
		adabArabicChapterNames.put(28, "كتاب الظُّلْم");
		adabArabicChapterNames.put(33, "كتاب الأقوال");
		adabArabicChapterNames.put(34, "كتاب الأسْمَاءِ");
		adabArabicChapterNames.put(35, "كتاب الكُنْيَةِ");
		adabArabicChapterNames.put(36, "كتاب الشِّعْرِ");
		adabArabicChapterNames.put(37, "كتاب الْكَلامِ");
		adabArabicChapterNames.put(40, "كتاب الْعُطَاسَ والتثاؤب");
		adabArabicChapterNames.put(42, "كتاب السَّلامِ");
		adabArabicChapterNames.put(43, "كتاب الاسْتِئْذَانُ");
		adabArabicChapterNames.put(44, "كتاب أَهْلِ الْكِتَابِ");
		adabArabicChapterNames.put(45, "كتاب الرَّسَائِلِ‏");
		adabArabicChapterNames.put(46, "كتاب الْمَجَالِسِ");
		adabArabicChapterNames.put(49, "كتاب الصباح والمساء");
		adabArabicChapterNames.put(51, "كتاب الْبَهَائِمِ");
		adabArabicChapterNames.put(53, "كتاب الْخِتَانِ");

		typos = new HashMap<String, String>();
		typos.put("narated", "narrated");
		typos.put("transrmitters", "transmitters");
		typos.put("Mesenger", "Messenger");
		typos.put("Propet", "Prophet");
		typos.put("comitted", "committed");
		typos.put("(s.a.w)", "ﷺ");
		typos.put("Coindition", "Condition");
		//typos.put("(ra)", "(radi Allahu anhu)");
		//typos.put("(RAA)", "(radi Allahu anhu)");

		chapterPattern = Pattern.compile("^Chapter \\d+\\.*\\s*\\w", Pattern.CASE_INSENSITIVE);
	}


	public void decorate(Collection<Narration> narrations, boolean arabic, String collection)
	{
		String lastBabName = null;
		int lastBabNum = 0;
		
		for (Narration n: narrations)
		{
			if ( n.babName == null || n.babName.isEmpty() ) {
				n.babName = lastBabName;
				n.babNumber = lastBabNum;
			}
			
			lastBabName = n.babName;
			lastBabNum = n.babNumber;
			
			correctChapters(n);
			correctBook(n, arabic, collection);
			correctHadithNumber(n);
			correctHadithBody(n, arabic);
		}
	}


	public void correctHadithBody(Narration n, boolean arabic)
	{
		String toConvert = n.text;

		if ( toConvert.endsWith("</b>") ) {
			toConvert = toConvert.substring( 0, toConvert.lastIndexOf("</b>") );
		}

		if (!arabic)
		{
			toConvert = toConvert.replaceAll("\\(S\\)|\\[SAW\\]|\\([sS]\\.[aA]\\.[wW]\\)|\\(SAW0{0,1}\\)|\\(saws\\)|SAW0|\\({0,1}SWAS\\){0,1}|\\(saW\\)|\\(saas\\)|[pP]\\.[bB]\\.[uU]\\.[hH]\\.{0,1}|pbuh", "ﷺ");
			toConvert = correctTypos(toConvert);
			toConvert = toConvert.replaceAll("\\s+", " ");
			//toConvert = toConvert.replaceAll("\\s{2,}", "");
			
			// TODO: " : "
			// TODO: ":   "
			// TODO: sal Allaahu alayhi wa sallam
			// TODO: ( sal Allaahu alayhi wa sallam)
			// TODO: STARTS WITH: "1450; ) Jabir said: The Messenger of Allah (ﷺ) sent us on an expedition and made Abu ‘Ubaidah b. al-Jarrah our leader. We had to meet a caravan of the Quraish. He gave us a bag of dates as a light meal during the journey. We had nothing except that. Abu ‘Ubaidah would give each of us one date. We used to suck them as a child sucks, and drink water after that and it sufficed us that day till night. We used to beat leaves off the trees with our sticks (for food), wetted them with water and ate them. We then went to the coast of the sea. There appeared to us a body like a great mound. When we came to it, we found that it was an animal called al-anbar. Abu ‘Ubaidah said: It is a carrion, and it is not lawful for us. He then said: No, we are the Messengers of the Apostel of Allah (ﷺ) and we are in the path of Allah. If you are forced by necessity (to eat it), then eat it. We stayed feeding on it for one mouth, till we became fat, and we were three hundred in number. When we came to the Messenger of Allah (ﷺ), we mentioned it to him. He said : It is a provision which Allah has brought forth for you, and give us some to eat if you have any meat of it with you. So we sent some of it to the Messenger of Allah (ﷺ) and he ate (it)."
			// 14; It was narrated that Ans said: "The Messenger of Allah sacrificed two Amlah rams."
		}

		toConvert = toConvert.replaceAll("<[^>]*>", "");
		
		if ( toConvert.startsWith("\"") && toConvert.endsWith("\"") ) {
			toConvert = toConvert.substring( 1, toConvert.length()-1 );
		}
		
		if ( toConvert.startsWith("\"") ) {
			toConvert = toConvert.substring(1);
		}
		
		//TODO: STARTSWITH: " Abdullah b. Umar
		
		n.text = toConvert.trim();
	}


	public String correctTypos(String body)
	{
		for ( String key: typos.keySet() ) {
			body = body.replace( key, typos.get(key) );
		}

		return body;
	}


	public void correctHadithNumber(Narration n)
	{
		String result = n.hadithNumber.trim();

		if ( result.startsWith("Introduction ") )
		{
			result = result.substring( "Introduction ".length() );
			n.hadithNumber = result;
		}
	}


	public void correctChapters(Narration n)
	{
		String babName = n.babName;
		
		if ( babName != null && !babName.trim().isEmpty() )
		{
			int babNumber = n.babNumber;
			
			if ( babName.startsWith(".") || babName.startsWith(":") ) {
				babName = babName.substring(1);
			} else if ( babName.startsWith("B.") ) {
				babName = babName.substring(2);
			}
			
			babName = WordUtils.capitalizeFully(babName);
			
			if (babNumber == 0)
			{
				Matcher matcher = chapterPattern.matcher(babName);

				if ( matcher.find() )
				{
					String match = babName.substring( matcher.start(), matcher.end() ).trim();
					babNumber = Integer.parseInt( match.replaceAll("\\D+","") );

					for (int k = matcher.end()-1; k < babName.length(); k++)
					{
						char current = babName.charAt(k);

						if ( Character.isAlphabetic(current) ) {
							babName = babName.substring(k);
							break;
						}
					}
				}
			}
			
			babName = babName.replaceAll("<[^>]*>", "").trim();
			
			if ( babName.isEmpty() ) {
				babName = null;
			} else if ( babName.endsWith(" ?") ) { // Returning Salam While Urinating ?
				babName = babName.substring( 0, babName.length()-2 )+"?";
			}
			
			correctTypos(babName);
			
			n.babNumber = babNumber;
		} else {
			babName = null;
		}
		
		n.babName = babName;
	}


	public void correctBook(Narration n, boolean arabic, String collection)
	{
		String bookName = n.bookName;
		int bookID = n.bookId;

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

			if ( bookName.equalsIgnoreCase("ok") && arabic && collection.equals("adab") )
			{
				if ( adabArabicChapterNames.containsKey(bookID) ) {
					bookName = adabArabicChapterNames.get(bookID);
				} else {
					bookName = "كتاب";
				}
			}

			if (bookName == null || bookName.isEmpty()) {
				System.out.println("WARNING! "+bookName+"; "+bookID+"; "+collection);
			}
		}

		n.bookName = bookName.trim();
		
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
}