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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import pl.com.dbs.reports.report.PatternFactoryDefaultTest;
import pl.com.dbs.reports.security.domain.SessionContext;

/**
 * TODO
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2014
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(SessionContext.class)
public class ReportBlockInflationTest {
	@Before
	public void doBeforeEachTestCase() {
		
	}
	
	@After  
    public void tearDown() {  
    }
	
	@Test
	public void should_have_switch_block() throws IOException, ReportBlockInflationException {
		StringBuffer sb = new StringBuffer(); 
		File file = read("pl/com/dbs/reports/domain/test1/test1.txt");
		final byte[] content = readFile(file);
		ReportTextBlocksBuilder bbuilder = new ReportTextBlocksBuilder(content);
		ReportBlock root = bbuilder.build().getBlock();
		
		String input0 = "V_DEFAULT";
		String input1 = "V_1";
		String input2 = "V_2";
		
		Map<String, String> params = new HashMap<String, String>();
		params.put(input0, "default-value");
		params.put(input1, "1");
		params.put(input2, "2");
		
		inflate(sb, root, params);
	}
	
	private void inflate(StringBuffer sb, ReportBlock root, Map<String, String> params) {
		if (root.hasContent()) {
			//..no more block inside.. generate content..
			sb.append(root.build(params));
			return;
		}		
		//..go deeper and inflate children..
		for (final ReportBlock block : root.getBlocks()) {
			inflate(sb, block, params);
		}
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
