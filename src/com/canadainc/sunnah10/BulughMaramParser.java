package com.canadainc.sunnah10;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.canadainc.common.io.IOUtils;
import com.canadainc.islamicutils.io.DBUtils;

public class BulughMaramParser
{
	private static final String HADITH_NUMBER_MARKER = "HADITS KE-";
	private static final String MARKER = "<!--** isi content ** -->";
	private int m_counter;
	private static final String REGEX_FILE_NAME = "\\.";
	private int m_idCounter;
	private Map<Integer,Book> m_indexToBook;

	public BulughMaramParser()
	{
		m_counter = 0;
		m_idCounter = 0;
		
		m_indexToBook = new HashMap<Integer,Book>();
		
		for (int i = 12; i <= 29; i++) {
			m_indexToBook.put(i, new Book(2, "كتاب الصلاة"));
		}
		
		for (int i = 44; i <= 65; i++) {
			m_indexToBook.put(i, new Book(7, null));
		}
		
		for (int i = 66; i <= 79; i++) {
			m_indexToBook.put(i, new Book(8, "كتاب النكاح"));
		}
	}

	
	public void createDB(Collection<Narration> narrations) throws SQLException
	{
		DBUtils.cleanUp("res/sunnah10/bulugh.db");
		
		Connection connection = DriverManager.getConnection("jdbc:sqlite:res/sunnah10/bulugh.db");
		connection.setAutoCommit(false);

		ArrayList<String> columns = new ArrayList<String>();
		columns.add("id INTEGER PRIMARY KEY");
		columns.add("en_hadith_number TEXT NOT NULL");
		columns.add("en_body TEXT NOT NULL");
		columns.add("ar_hadith_number TEXT");
		columns.add("ar_body TEXT");
		columns.add("en_book_name TEXT NOT NULL");
		columns.add("en_book_num INTEGER NOT NULL");
		columns.add("en_chapter_name TEXT NOT NULL");
		columns.add("en_chapter_num INTEGER NOT NULL");

		String table = "bulugh_narrations";
		DBUtils.createTable(connection, table, columns);
		DBUtils.isolateColumnNames(columns, "id", "ar_hadith_number", "ar_body");

		PreparedStatement ps = connection.prepareStatement("INSERT INTO "+table+"("+StringUtil.join(columns, ",")+") VALUES "+DBUtils.generatePlaceHolders(columns) );
		
		for (Narration n: narrations)
		{
			int i = 0;
			ps.setString(++i, n.hadithNumber);
			ps.setString(++i, n.text);
			ps.setString(++i, n.book.name);
			ps.setInt(++i, n.book.id);
			ps.setString(++i, n.chapter.title);
			ps.setInt(++i, n.chapter.number);
			ps.execute();
		}

		connection.commit();
		ps.close();
		
		connection.close();
	}
	

	private List<Narration> readBulughNarrations(File chapterPath, String bookName, int inBookNumber) throws IOException
	{
		ArrayList<Narration> narrations = new ArrayList<Narration>();
		File[] textFiles = chapterPath.listFiles( new NarrationFilter() );
		String[] tokens = chapterPath.getName().split(REGEX_FILE_NAME);
		Chapter c = new Chapter( tokens[1].trim(), Integer.parseInt( tokens[0].trim() ) );
		tokens = bookName.split(REGEX_FILE_NAME);
		Book b = new Book( Integer.parseInt( tokens[0].trim() ), tokens[1].trim() );

		for (File f: textFiles)
		{
			String body = IOUtils.readFileUtf8(f).trim();
			int dotIndex = body.indexOf(".");

			Narration n = new Narration();
			n.chapter = c;
			n.book = b;
			n.id = ++m_idCounter;
			n.hadithNumber = body.substring(0, dotIndex).trim();
			n.text = body.substring(dotIndex+1).trim();
			n.inBookNumber = inBookNumber;
			n.translator = SunnahConstants.BULUGH_MARAM_TRANSLATOR;
			narrations.add(n);
		}

		return narrations;
	}


	public List<Narration> parseArabicChapter(File chapter) throws IOException
	{
		ArrayList<Narration> narrations = new ArrayList<Narration>();
		String body = IOUtils.readFileUtf8(chapter);
		body = body.substring( body.indexOf(MARKER)+MARKER.length(), body.lastIndexOf(MARKER) ).trim();
		String[] hadiths = body.split("<p>&nbsp;</p>");

		for (String hadith: hadiths)
		{
			if ( !hadith.trim().isEmpty() )
			{
				int hadithNumberStart = hadith.indexOf(HADITH_NUMBER_MARKER)+HADITH_NUMBER_MARKER.length();
				int hadithNumberEnd = hadith.indexOf("<", hadithNumberStart);

				Document doc = Jsoup.parse(hadith);
				Elements spans = doc.select("p[dir=rtl]");

				for (Element span: spans)
				{
					Narration n = new Narration();
					n.id = ++m_counter;
					n.hadithNumber = hadith.substring(hadithNumberStart, hadithNumberEnd);
					n.text = span.text().trim();

					narrations.add(n);
				}
			}
		}

		return narrations;
	}
	
	
	public List<Narration> readStaticBulughArabic(File filePath) throws Exception
	{
		ArrayList<Narration> narrations = new ArrayList<Narration>();
		File[] books = filePath.listFiles( new HtmlFilter() );
		
		for (File f: books) {
			narrations.addAll( parseArabicChapter(f) );
		}
		
		return narrations;
	}


	public List<Narration> readStaticBulugh(File filePath) throws IOException
	{
		ArrayList<Narration> narrations = new ArrayList<Narration>();

		ChaptersFilter cf = new ChaptersFilter();
		File[] books = filePath.listFiles( new BookFilter() );

		for (int bookId = 0; bookId < books.length; bookId++)
		{
			File b = books[bookId];
			File[] chapters = b.listFiles(cf);
			int inBookNumber = 0;

			for (int chapterNumber = 0; chapterNumber < chapters.length; chapterNumber++)
			{
				File chapter = chapters[chapterNumber];
				narrations.addAll( readBulughNarrations(chapter, b.getName(), ++inBookNumber) );
			}
		}

		return narrations;
	}


	public class NarrationFilter implements FilenameFilter
	{
		@Override
		public boolean accept(File dir, String name) {
			return name.matches("^\\d{4}\\.txt$");
		}
	}
	
	public class HtmlFilter implements FilenameFilter
	{
		@Override
		public boolean accept(File dir, String name) {
			return name.matches("^\\d{1,2}[a-z]+\\.html$");
		}
	}


	private class BookFilter implements FilenameFilter
	{
		@Override
		public boolean accept(File dir, String name) {
			return dir.isDirectory() && name.matches("^\\d{2}\\. [a-zA-Z ]+$");
		}
	}

	private class ChaptersFilter implements FilenameFilter
	{
		@Override
		public boolean accept(File dir, String name) {
			return dir.isDirectory() && name.matches("^\\d{2}\\. [a-zA-Z \\(\\)\\-',]+$");
		}
	}
}