/**
 * 
 */
package pl.com.dbs.reports.report.domain;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import pl.com.dbs.reports.report.domain.builders.ReportBlockException;
import pl.com.dbs.reports.report.domain.builders.ReportBlocksBuilder;
import pl.com.dbs.reports.security.domain.SessionContext;

/**
 * SWITCH/ENDSWITCH tests
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2014
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(SessionContext.class)
public class ReportBlockSwitchRuleTest extends ReportBlockRuleTest {
	static final String I_DEFAULT = "I_DEFAULT";
	static final String I_0 = "I_0";
	static final String I_1 = "I_1";
	static final String I_2 = "I_2";
	static final String I_3 = "I_3";
	static final String SENTENCE = "Kto postepuje godniej: ten, kto biernie ";
	static final String EOF = "\n";//System.getProperty("line.separator");
	
	
	@Before
	public void doBeforeEachTestCase() {
		super.doBeforeEachTestCase();
		
		params.put(I_DEFAULT, "The slings and arrows of outrageous fortune");
		params.put(I_0, "Stoi pod gradem zajadlych strzal losu");
		params.put(I_1, "znosic ostre ciosy niesprawiedliwej fortuny");
		params.put(I_2, "Znosic pociski zawsitnego losu");
		params.put(I_3, "strzaly i razy nieprzytomne");		
	}
	

	@Test
	public void should_split_1() throws IOException, ReportBlockException {
		params.put("V_0", "");
		
		ReportBlocksBuilder builder = builder("pl/com/dbs/reports/domain/rule/test/test0.txt");
		
		String sb = new String(builder.build().getContent());
		
		System.out.print(sb);		
		
		
		assertTrue(sb.equals("empty"));
	}
	
	@Test
	public void should_split_2() throws IOException, ReportBlockException {
		params.put("V_0", "0");
		
		ReportBlocksBuilder builder = builder("pl/com/dbs/reports/domain/rule/test/test0.txt");
		
		String sb = new String(builder.build().getContent());
		
		System.out.print(sb);		

		
		assertTrue(sb.equals("zero"+EOF+"zero"));
	}

	@Test
	public void should_split_3() throws IOException, ReportBlockException {
		params.put("V_0", "1");
		
		ReportBlocksBuilder builder = builder("pl/com/dbs/reports/domain/rule/test/test0.txt");
		
		String sb = new String(builder.build().getContent());
		
		System.out.print(sb);	
		
		assertTrue(sb.equals("one by one"+EOF));
	}
	
	@Test
	public void should_split_4() throws IOException, ReportBlockException {
		params.put("V_0", "2");
		
		ReportBlocksBuilder builder = builder("pl/com/dbs/reports/domain/rule/test/test0.txt");
		
		String sb = new String(builder.build().getContent());
		
		System.out.print(sb);	
	
		assertTrue(sb.equals("two"+EOF+"two two"));
	}
	
	@Test
	public void should_split_5() throws IOException, ReportBlockException {
		params.put("V_0", "3");
		
		ReportBlocksBuilder builder = builder("pl/com/dbs/reports/domain/rule/test/test0.txt");
		
		String sb = new String(builder.build().getContent());
		
		System.out.print(sb);			
		
		assertTrue(sb.equals(" three "));
	}
	
	@Test
	public void should_split_6() throws IOException, ReportBlockException {
		params.put("V_0", "_123_- !@#$%^&*");
		
		ReportBlocksBuilder builder = builder("pl/com/dbs/reports/domain/rule/test/test0.txt");
		
		String sb = new String(builder.build().getContent());
		
		System.out.print(sb);		
	
		assertTrue(sb.equals("strange!"+EOF));		
	}
	
	@Test
	public void should_split_7() throws IOException, ReportBlockException {
		params.put("V_0", "4");
		
		ReportBlocksBuilder builder = builder("pl/com/dbs/reports/domain/rule/test/test0.txt");
		
		String sb = new String(builder.build().getContent());
		
		System.out.print(sb);	
	
		assertTrue(sb.equals("default suitcase and switcher "));			
	}
	
	@Test
	public void shouldnt_have_any_tags() throws IOException, ReportBlockException {
		params.put("V_0", "");
		params.put("V_1", "");
		params.put("V_2", "");
		params.put("V_3", "");
		
		ReportBlocksBuilder builder = builder("pl/com/dbs/reports/domain/rule/test/test1.txt");
		
		String sb = new String(builder.build().getContent());
		
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
		params.put("V_0", "");
		
		ReportBlocksBuilder builder = builder("pl/com/dbs/reports/domain/rule/test/test2.txt");
		
		String sb = new String(builder.build().getContent());
		
		System.out.print(sb);		
		
		assertTrue(sb.equals(SENTENCE+"..."));
	}
	
	@Test
	public void should_be_default_variant() throws IOException, ReportBlockException {
		params.put("V_0", "nonabove");
		
		ReportBlocksBuilder builder = builder("pl/com/dbs/reports/domain/rule/test/test2.txt");
		
		String sb = new String(builder.build().getContent());
		
		System.out.print(sb);			
		
		assertTrue(sb.equals(SENTENCE+params.get(I_DEFAULT)+EOF));
	}	
	
	@Test
	public void should_be_a_variant_1() throws IOException, ReportBlockException {
		params.put("V_0", "0");
		
		ReportBlocksBuilder builder = builder("pl/com/dbs/reports/domain/rule/test/test2.txt");
		
		String sb = new String(builder.build().getContent());
		
		System.out.print(sb);
		
		
		assertTrue(sb.equals(SENTENCE+params.get(I_0)));
	}
		
	@Test
	public void should_be_a_variant_2() throws IOException, ReportBlockException {
		params.put("V_0", "1");
		
		ReportBlocksBuilder builder = builder("pl/com/dbs/reports/domain/rule/test/test2.txt");
		
		String sb = new String(builder.build().getContent());
		
		System.out.print(sb);
		
		assertTrue(sb.equals(SENTENCE+params.get(I_1)));
	}
		
	@Test
	public void should_be_a_variant_3() throws IOException, ReportBlockException {
		params.put("V_0", "2");
		
		ReportBlocksBuilder builder = builder("pl/com/dbs/reports/domain/rule/test/test2.txt");
		
		String sb = new String(builder.build().getContent());
		
		System.out.print(sb);
		assertTrue(sb.equals(SENTENCE+params.get(I_2)));
	}

	@Test
	public void should_be_a_variant_4() throws IOException, ReportBlockException {
		params.put("V_0", "3");
		
		ReportBlocksBuilder builder = builder("pl/com/dbs/reports/domain/rule/test/test2.txt");
		
		String sb = new String(builder.build().getContent());
		
		System.out.print(sb);		
		assertTrue(sb.equals(SENTENCE+params.get(I_3)));			
	}	
	
	@Test
	public void should_choose_space() throws IOException, ReportBlockException {
		params.put("INTEXT", " ");

		ReportBlocksBuilder builder = builderContent("[BLOCK(INIT)BLOCK][SWITCH(^$INTEXT^)#CASE( )spacja #CASE()pusty #CASE(aa)aa #CASE($)inny SWITCH]");
		
		String sb = new String(builder.build().getContent());
		
		System.out.print(sb);		
		assertTrue(sb.equals("spacja "));			
	}
	
	@Test
	public void should_choose_empty() throws IOException, ReportBlockException {
		params.put("INTEXT", "");

		ReportBlocksBuilder builder = builderContent("[BLOCK(INIT)BLOCK][SWITCH(^$INTEXT^)#CASE( )spacja #CASE()pusty #CASE(aa)aa #CASE($)inny SWITCH]");
		
		String sb = new String(builder.build().getContent());
		
		System.out.print(sb);		
		assertTrue(sb.equals("pusty "));			
	}
	
	@Test
	public void should_choose_otherwise() throws IOException, ReportBlockException {
		params.put("INTEXT", "bum tralalala chlupie fala");

		ReportBlocksBuilder builder = builderContent("[BLOCK(INIT)BLOCK][SWITCH(^$INTEXT^)#CASE( )spacja #CASE()pusty #CASE(aa)aa #CASE($)inny SWITCH]");
		
		String sb = new String(builder.build().getContent());
		
		System.out.print(sb);		
		assertTrue(sb.equals("inny "));			
	}	
}
