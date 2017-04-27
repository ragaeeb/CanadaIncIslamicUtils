package com.canadainc.sunnah10;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.canadainc.common.io.IOUtils;

public class DiskNarrationReader
{
	private int m_translator;
	private int m_counter;
	private static final String REGEX_FILE_NAME = "\\.";

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
			n.id = ++m_counter;
			n.hadithNumber = body.substring(0, dotIndex).trim();
			n.text = body.substring(dotIndex+1).trim();
			n.inBookNumber = inBookNumber;
			
			if (m_translator != 0) {
				n.translator = SunnahConstants.BULUGH_MARAM_TRANSLATOR;
			}

			narrations.add(n);
		}

		return narrations;
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

	public class NarrationFilter implements FilenameFilter
	{
		@Override
		public boolean accept(File dir, String name) {
			return name.matches("^\\d{4}\\.txt$");
		}
	}

	public void setTranslator(int translator) {
		m_translator = translator;
	}
}