package pl.com.dbs.reports;

import pl.com.dbs.reports.report.PatternFactoryDefaultTest;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2017
 */
public class TestFileHelper {

	public static File read(String src) {
		File file = null;
		try {
			URL url = PatternFactoryDefaultTest.class.getClassLoader().getResource(src);
			file = new File(url.toURI());
		} catch (URISyntaxException e) {}
		if (!file.exists()) throw new IllegalStateException();
		return file;
	}

	public static byte[] read(final File file) throws IOException {
		InputStream input = new FileInputStream(file);
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int bytesRead;
		while ((bytesRead = input.read(buffer)) != -1)
			output.write(buffer, 0, bytesRead);
		output.close();
		input.close();
		return output.toByteArray();
	}

	public static byte[] read2byte(final String src) throws IOException {
		return read(read(src));
	}

}
