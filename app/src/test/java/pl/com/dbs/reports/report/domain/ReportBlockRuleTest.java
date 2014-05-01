/**
 * 
 */
package pl.com.dbs.reports.report.domain;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import pl.com.dbs.reports.report.PatternFactoryDefaultTest;
import pl.com.dbs.reports.report.domain.builders.ReportBlocksBuilder;
import pl.com.dbs.reports.report.domain.builders.ReportTextBlockInflaterDefault;
import pl.com.dbs.reports.report.domain.builders.ReportTextBlocksBuilder;
import pl.com.dbs.reports.report.pattern.domain.ReportPatternTransformate;
import pl.com.dbs.reports.security.domain.SessionContext;

/**
 * Rules tests base.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2014
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(SessionContext.class)
public abstract class ReportBlockRuleTest {
	private ReportTextBlockInflaterDefault inflater;
	Map<String, String> params;
	

	@Before
	public void doBeforeEachTestCase() {
		inflater = new ReportTextBlockInflaterDefault();
		params = new HashMap<String, String>();
	}
	
	@After  
    public void tearDown() {  
    }
	
	
	ReportBlocksBuilder builder(String filename) throws IOException {
		File file = read(filename);
		final byte[] content = readFile(file);
		
		ReportPatternTransformate transformate = new ReportPatternTransformate(content, filename, null);
		return new ReportTextBlocksBuilder(transformate, inflater, params);
	}
	
	private File read(String src) {
		File file = null;
		try {
			URL url = PatternFactoryDefaultTest.class.getClassLoader().getResource(src);
			file = new File(url.toURI());
		} catch (URISyntaxException e) {}
		if (!file.exists()) throw new IllegalStateException(); 
		return file;
	}	
	
	private byte[] readFile(final File file) throws IOException {
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
}
