package com.canadainc.islamicutils.io;

import java.io.*;
import java.net.*;

public class NetworkBoundary
{
	public static String getHTML(String urlToRead)
	{
		URL url;
		HttpURLConnection conn;
		BufferedReader rd;
		String line;
		StringBuffer result = new StringBuffer();

		try {
			url = new URL(urlToRead);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			rd = new BufferedReader( new InputStreamReader( conn.getInputStream(), "UTF-8" ) );

			while ( (line = rd.readLine() ) != null) {
				result.append(line);
			}

			rd.close();
		} catch (Exception e) {
			//e.printStackTrace();
		}
		return result.toString();
	}

	public static void saveFile(String uri, String destinationFile) throws IOException
	{
		URL url = new URL(uri);
		InputStream is = url.openStream();
		OutputStream os = new FileOutputStream(destinationFile);

		byte[] b = new byte[2048];
		int length;

		while ((length = is.read(b)) != -1) {
			os.write(b, 0, length);
		}

		is.close();
		os.close();
	}
}
