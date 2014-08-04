/**
 * 
 */
package pl.com.dbs.reports.report.domain;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Map;

import org.junit.Test;

import pl.com.dbs.reports.report.domain.builders.ReportBlockException;
import pl.com.dbs.reports.report.domain.builders.ReportBlockInflationException;
import pl.com.dbs.reports.report.domain.builders.ReportBlocksBuilder;

/**
 * REPLACE tests
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2014
 */
public class ReportBlockReplaceRuleTest extends ReportBlockRuleTest {
	static final String V_0 = "Whether 'tis nobler in the mind to suffer";
	static final String V_1 = "The slings and arrows of outrageous fortune";
	static final String SENTENCE = "Kto postepuje godniej: ten, kto biernie ";
	

	@Test
	public void shouldnt_have_tags() throws IOException, ReportBlockInflationException, ReportBlockException {
		params.put("V_0", V_0);
		params.put("V_1", V_1);

		ReportBlocksBuilder builder = 
				builder("pl/com/dbs/reports/domain/rule/test/test3.txt");
		
		String sb = new String(builder.build().getContent());
		
		System.out.print(sb);
		
		assertTrue(!sb.contains("^$V_0^"));
		assertTrue(!sb.contains("^$V_1^"));
	}
	
	@Test
	public void check_production_pattern() throws IOException, ReportBlockInflationException, ReportBlockException {
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
		
		ReportBlocksBuilder builder = 
				builder("pl/com/dbs/reports/domain/rule/test/zua.txt");
		
		String sb = new String(builder.build().getContent());
		
		System.out.print(sb);
		
		for (Map.Entry<String, String> entry : params.entrySet()) {
			assertTrue(!sb.toString().contains("^$"+entry.getKey()+"^"));
		}
	}	
}
