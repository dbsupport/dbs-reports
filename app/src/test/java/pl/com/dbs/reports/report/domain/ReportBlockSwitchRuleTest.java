/**
 * 
 */
package pl.com.dbs.reports.report.domain;

import static org.junit.Assert.assertTrue;

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
 * SWITCH/ENDSWITCH tests
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2014
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(SessionContext.class)
public class ReportBlockSwitchRuleTest {
	Map<String, String> params;
	static final String I_DEFAULT = "I_DEFAULT";
	static final String I_0 = "I_0";
	static final String I_1 = "I_1";
	static final String I_2 = "I_2";
	static final String I_3 = "I_3";
	static final String SENTENCE = "Kto postepuje godniej: ten, kto biernie ";
	static final String EOF = System.getProperty("line.separator");
	
	
	@Before
	public void doBeforeEachTestCase() {
		params = new HashMap<String, String>();
		
		params.put(I_DEFAULT, "The slings and arrows of outrageous fortune");
		params.put(I_0, "Stoi pod gradem zajadlych strzal losu");
		params.put(I_1, "znosic ostre ciosy niesprawiedliwej fortuny");
		params.put(I_2, "Znosic pociski zawsitnego losu");
		params.put(I_3, "strzaly i razy nieprzytomne");		
	}
	
	@After  
    public void tearDown() {  
    }

	@Test
	public void should_split() throws IOException, ReportBlockInflationException, ReportBlockException {
		
		StringBuffer sb = new StringBuffer();
		ReportBlock root = build("pl/com/dbs/reports/domain/rule/test/test0.txt");
		
		params.put("V_0", "");
		inflate(sb, root, params);
		System.out.print(sb);
		
		assertTrue(sb.toString().equals("empty"));
		
		sb = new StringBuffer();
		params.put("V_0", "0");
		inflate(sb, root, params);
		System.out.print(sb);
	
		assertTrue(sb.toString().equals("zero"+EOF+"zero"));		

		sb = new StringBuffer();
		params.put("V_0", "1");
		inflate(sb, root, params);
		System.out.print(sb);
	
		assertTrue(sb.toString().equals("one by one"+EOF));		
		

		sb = new StringBuffer();
		params.put("V_0", "2");
		inflate(sb, root, params);
		System.out.print(sb);
	
		assertTrue(sb.toString().equals("two"+EOF+"two two"));		
		
		sb = new StringBuffer();
		params.put("V_0", "3");
		inflate(sb, root, params);
		System.out.print(sb);
	
		assertTrue(sb.toString().equals(" three "));	
		
		
		sb = new StringBuffer();
		params.put("V_0", "_123_- !@#$%^&*");
		inflate(sb, root, params);
		System.out.print(sb);
	
		assertTrue(sb.toString().equals("strange!"+EOF));		
		
		
		sb = new StringBuffer();
		params.put("V_0", "4");
		inflate(sb, root, params);
		System.out.print(sb);
	
		assertTrue(sb.toString().equals("default suitcase and switcher "));			
	}
	
	@Test
	public void shouldnt_have_any_tags() throws IOException, ReportBlockInflationException, ReportBlockException {
		StringBuffer sb = new StringBuffer();
		ReportBlock root = build("pl/com/dbs/reports/domain/rule/test/test1.txt");
		
		params.put("V_0", "");
		params.put("V_1", "");
		params.put("V_2", "");
		params.put("V_3", "");
		
		inflate(sb, root, params);
		
		System.out.print(sb);
		
		assertTrue(!sb.toString().contains("SWITCH"));
		assertTrue(!sb.toString().contains("^$V_0^"));
		assertTrue(!sb.toString().contains("^$V_1^"));
		assertTrue(!sb.toString().contains("^$V_2^"));
		assertTrue(!sb.toString().contains("^$V_3^"));
		assertTrue(!sb.toString().contains("^$I_0^"));
		assertTrue(!sb.toString().contains("^$I_1^"));
		assertTrue(!sb.toString().contains("^$I_2^"));
		assertTrue(!sb.toString().contains("^$I_3^"));
		assertTrue(!sb.toString().contains("^$I_DEFAULT^"));
	}
	
	@Test
	public void should_be_null_variant() throws IOException, ReportBlockInflationException, ReportBlockException {
		StringBuffer sb = new StringBuffer();
		ReportBlock root = build("pl/com/dbs/reports/domain/rule/test/test2.txt");
		
		params.put("V_0", "");
		
		inflate(sb, root, params);
		
		System.out.print(sb);
		
		assertTrue(sb.toString().equals(SENTENCE+"..."));
	}
	
	@Test
	public void should_be_default_variant() throws IOException, ReportBlockInflationException, ReportBlockException {
		StringBuffer sb = new StringBuffer();
		ReportBlock root = build("pl/com/dbs/reports/domain/rule/test/test2.txt");		
		
		params.put("V_0", "nonabove");
		
		inflate(sb, root, params);
		
		System.out.print(sb);
		
		assertTrue(sb.toString().equals(SENTENCE+params.get(I_DEFAULT)+EOF));
	}	
	
	@Test
	public void should_be_a_variant() throws IOException, ReportBlockInflationException, ReportBlockException {
		StringBuffer sb = new StringBuffer();
		ReportBlock root = build("pl/com/dbs/reports/domain/rule/test/test2.txt");
		
		params.put("V_0", "0");
		inflate(sb, root, params);
		System.out.println(sb);
		assertTrue(sb.toString().equals(SENTENCE+params.get(I_0)));
		
		
		
		sb = new StringBuffer(); 
		params.put("V_0", "1");
		inflate(sb, root, params);
		System.out.println(sb);
		assertTrue(sb.toString().equals(SENTENCE+params.get(I_1)));		
		
		
		sb = new StringBuffer(); 
		params.put("V_0", "2");
		inflate(sb, root, params);
		System.out.println(sb);
		assertTrue(sb.toString().equals(SENTENCE+params.get(I_2)));
		
		sb = new StringBuffer(); 
		params.put("V_0", "3");
		inflate(sb, root, params);
		System.out.println(sb);
		assertTrue(sb.toString().equals(SENTENCE+params.get(I_3)));			
		
	}	
	
	private ReportBlock build(String filename) throws IOException {
		File file = read(filename);
		final byte[] content = readFile(file);
		ReportTextBlocksBuilder bbuilder = new ReportTextBlocksBuilder(content);
		return bbuilder.build().getBlock();		
	}
	
	private void inflate(StringBuffer sb, ReportBlock root, Map<String, String> params) throws ReportBlockException {
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
