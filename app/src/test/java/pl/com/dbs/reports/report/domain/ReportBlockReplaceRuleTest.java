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
 * REPLACE tests
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2014
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(SessionContext.class)
public class ReportBlockReplaceRuleTest {
	Map<String, String> params;
	static final String V_0 = "Whether 'tis nobler in the mind to suffer";
	static final String V_1 = "The slings and arrows of outrageous fortune";
	static final String SENTENCE = "Kto postepuje godniej: ten, kto biernie ";
	
	
	@Before
	public void doBeforeEachTestCase() {
		params = new HashMap<String, String>();
	}
	
	@After  
    public void tearDown() {  
    }
	
	@Test
	public void shouldnt_have_tags() throws IOException, ReportBlockInflationException, ReportBlockException {
		StringBuffer sb = new StringBuffer();
		ReportTextBlock root = build("pl/com/dbs/reports/domain/rule/test/test3.txt");
		
		params.put("V_0", V_0);
		params.put("V_1", V_1);
		
		inflate(sb, root, params);
		
		System.out.print(sb);
		
		assertTrue(!sb.toString().contains("^$V_0^"));
		assertTrue(!sb.toString().contains("^$V_1^"));
	}
	
	@Test
	public void check_production_pattern() throws IOException, ReportBlockInflationException, ReportBlockException {
		StringBuffer sb = new StringBuffer();
		ReportTextBlock root = build("pl/com/dbs/reports/domain/rule/test/zua.txt");
		
		params.put("INDATWYP", "INDATWYP");
		params.put("VROW", "VROW");
		params.put("VKOR", "VKOR");
		params.put("VNIP", "VNIP");
		params.put("VREG", "VREG");
		params.put("VNAZ", "VNAZ");
		params.put("VPES", "VPES");
		params.put("VTYP", "VTYP");
		params.put("VDOK", "VDOK");
		params.put("VNAZW", "VNAZW");
		params.put("VIMIE", "VIMIE");
		params.put("VDATUR", "VDATUR");
		params.put("VIMIE2", "VIMIE2");
		params.put("VNAZW2", "VNAZW2");
		params.put("VOBYW", "VOBYW");
		params.put("VPLEC", "VPLEC");
		params.put("VTYT1", "VTYT1");
		params.put("VTYT2", "VTYT2");
		params.put("VTYT3", "VTYT3");
		params.put("VZUSDEB", "VZUSDEB");
		params.put("VKASA", "VKASA");
		params.put("VKOD", "VKOD");
		params.put("VMIEJSC", "VMIEJSC");
		params.put("VGMINA2", "VGMINA2");
		params.put("VULICA", "VULICA");
		params.put("VNRDOMU", "VNRDOMU");
		params.put("VNRMIESZ", "VNRMIESZ");
		params.put("VKODKRAJU", "VKODKRAJU");
		params.put("VKODZAGR", "VKODZAGR");
		
		
		inflate(sb, root, params);
		
		System.out.print(sb);
		
		for (Map.Entry<String, String> entry : params.entrySet()) {
			assertTrue(!sb.toString().contains("^$"+entry.getKey()+"^"));
		}
	}	
	
	
	
	private ReportTextBlock build(String filename) throws IOException {
		File file = read(filename);
		final byte[] content = readFile(file);
		ReportTextBlocksBuilder bbuilder = new ReportTextBlocksBuilder(content);
		return bbuilder.deconstruct().getRootBlock();		
	}
	
	private void inflate(StringBuffer sb, ReportTextBlock root, Map<String, String> params) throws ReportBlockException {
		if (root.hasContent()) {
			//..no more block inside.. generate content..
			sb.append(root.build(params));
			return;
		}		
		//..go deeper and inflate children..
		for (final ReportTextBlock block : root.getBlocks()) {
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
