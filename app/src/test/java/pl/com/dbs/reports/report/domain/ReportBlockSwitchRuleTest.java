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
	private ReportTextBlockInflater inflater;
	
	
	@Before
	public void doBeforeEachTestCase() {
		inflater = new ReportTextBlockInflater();
		
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
	public void should_split_1() throws IOException, ReportBlockException {
		ReportTextBlocksBuilder builder = build("pl/com/dbs/reports/domain/rule/test/test0.txt");
		
		params.put("V_0", "");
		inflater.inflate(builder.getRootBlock(), params);
		builder.construct();
		System.out.print(builder.getContentAsString());
		
		assertTrue(builder.getContentAsString().equals("empty"));
	}
	
	@Test
	public void should_split_2() throws IOException, ReportBlockException {
		ReportTextBlocksBuilder builder = build("pl/com/dbs/reports/domain/rule/test/test0.txt");
		
		params.put("V_0", "0");
		inflater.inflate(builder.getRootBlock(), params);
		builder.construct();
		System.out.print(builder.getContentAsString());
	
		assertTrue(builder.getContentAsString().equals("zero"+EOF+"zero"));
	}

	@Test
	public void should_split_3() throws IOException, ReportBlockException {
		ReportTextBlocksBuilder builder = build("pl/com/dbs/reports/domain/rule/test/test0.txt");
		params.put("V_0", "1");
		inflater.inflate(builder.getRootBlock(), params);
		builder.construct();
		System.out.print(builder.getContentAsString());
	
		assertTrue(builder.getContentAsString().equals("one by one"+EOF));
	}
	
	@Test
	public void should_split_4() throws IOException, ReportBlockException {
		ReportTextBlocksBuilder builder = build("pl/com/dbs/reports/domain/rule/test/test0.txt");
		params.put("V_0", "2");
		inflater.inflate(builder.getRootBlock(), params);
		builder.construct();
		System.out.print(builder.getContentAsString());
	
		assertTrue(builder.getContentAsString().equals("two"+EOF+"two two"));
	}
	
	@Test
	public void should_split_5() throws IOException, ReportBlockException {
		ReportTextBlocksBuilder builder = build("pl/com/dbs/reports/domain/rule/test/test0.txt");
		params.put("V_0", "3");
		inflater.inflate(builder.getRootBlock(), params);
		builder.construct();
		System.out.print(builder.getContentAsString());
	
		assertTrue(builder.getContentAsString().equals(" three "));
	}
	
	@Test
	public void should_split_6() throws IOException, ReportBlockException {
		ReportTextBlocksBuilder builder = build("pl/com/dbs/reports/domain/rule/test/test0.txt");
		params.put("V_0", "_123_- !@#$%^&*");
		inflater.inflate(builder.getRootBlock(), params);
		builder.construct();
		System.out.print(builder.getContentAsString());
	
		assertTrue(builder.getContentAsString().equals("strange!"+EOF));		
	}
	
	@Test
	public void should_split_7() throws IOException, ReportBlockException {
		ReportTextBlocksBuilder builder = build("pl/com/dbs/reports/domain/rule/test/test0.txt");
		params.put("V_0", "4");
		inflater.inflate(builder.getRootBlock(), params);
		builder.construct();
		System.out.print(builder.getContentAsString());
	
		assertTrue(builder.getContentAsString().equals("default suitcase and switcher "));			
	}
	
	@Test
	public void shouldnt_have_any_tags() throws IOException, ReportBlockException {
		ReportTextBlocksBuilder builder = build("pl/com/dbs/reports/domain/rule/test/test1.txt");
		
		params.put("V_0", "");
		params.put("V_1", "");
		params.put("V_2", "");
		params.put("V_3", "");
		
		inflater.inflate(builder.getRootBlock(), params);
		builder.construct();
		String sb = builder.getContentAsString();
		System.out.print(sb);
		
		assertTrue(!sb.contains("SWITCH"));
		assertTrue(!sb.contains("^$V_0^"));
		assertTrue(!sb.contains("^$V_1^"));
		assertTrue(!sb.contains("^$V_2^"));
		assertTrue(!sb.contains("^$V_3^"));
		assertTrue(!sb.contains("^$I_0^"));
		assertTrue(!sb.contains("^$I_1^"));
		assertTrue(!sb.contains("^$I_2^"));
		assertTrue(!sb.contains("^$I_3^"));
		assertTrue(!sb.contains("^$I_DEFAULT^"));
	}
	
	@Test
	public void should_be_null_variant() throws IOException, ReportBlockException {
		ReportTextBlocksBuilder builder = build("pl/com/dbs/reports/domain/rule/test/test2.txt");
		
		params.put("V_0", "");
		
		inflater.inflate(builder.getRootBlock(), params);
		builder.construct();
		String sb = builder.getContentAsString();
		System.out.print(sb);
		
		assertTrue(sb.equals(SENTENCE+"..."));
	}
	
	@Test
	public void should_be_default_variant() throws IOException, ReportBlockException {
		ReportTextBlocksBuilder builder = build("pl/com/dbs/reports/domain/rule/test/test2.txt");	
		
		params.put("V_0", "nonabove");
		
		inflater.inflate(builder.getRootBlock(), params);
		builder.construct();
		String sb = builder.getContentAsString();
		System.out.print(sb);
		
		assertTrue(sb.equals(SENTENCE+params.get(I_DEFAULT)+EOF));
	}	
	
	@Test
	public void should_be_a_variant_1() throws IOException, ReportBlockException {
		ReportTextBlocksBuilder builder = build("pl/com/dbs/reports/domain/rule/test/test2.txt");	
		
		params.put("V_0", "0");
		inflater.inflate(builder.getRootBlock(), params);
		builder.construct();
		String sb = builder.getContentAsString();
		System.out.print(sb);
		assertTrue(sb.equals(SENTENCE+params.get(I_0)));
	}
		
	@Test
	public void should_be_a_variant_2() throws IOException, ReportBlockException {
		ReportTextBlocksBuilder builder = build("pl/com/dbs/reports/domain/rule/test/test2.txt");	
		
		params.put("V_0", "1");
		inflater.inflate(builder.getRootBlock(), params);
		builder.construct();
		String sb = builder.getContentAsString();
		System.out.print(sb);
		assertTrue(sb.equals(SENTENCE+params.get(I_1)));
	}
		
	@Test
	public void should_be_a_variant_3() throws IOException, ReportBlockException {
		ReportTextBlocksBuilder builder = build("pl/com/dbs/reports/domain/rule/test/test2.txt");	
		
		params.put("V_0", "2");
		inflater.inflate(builder.getRootBlock(), params);
		builder.construct();
		String sb = builder.getContentAsString();
		System.out.print(sb);
		assertTrue(sb.equals(SENTENCE+params.get(I_2)));
	}

	@Test
	public void should_be_a_variant_4() throws IOException, ReportBlockException {
		ReportTextBlocksBuilder builder = build("pl/com/dbs/reports/domain/rule/test/test2.txt");	
		params.put("V_0", "3");
		inflater.inflate(builder.getRootBlock(), params);
		builder.construct();
		String sb = builder.getContentAsString();
		System.out.print(sb);
		assertTrue(sb.equals(SENTENCE+params.get(I_3)));			
		
	}	
	
	
	
	
	
	
	
	private ReportTextBlocksBuilder build(String filename) throws IOException {
		File file = read(filename);
		final byte[] content = readFile(file);
		ReportTextBlocksBuilder bbuilder = new ReportTextBlocksBuilder(content);
		return bbuilder.deconstruct();		
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
