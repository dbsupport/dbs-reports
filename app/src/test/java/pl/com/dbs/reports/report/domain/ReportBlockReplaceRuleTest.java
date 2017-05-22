/**
 * 
 */
package pl.com.dbs.reports.report.domain;

import org.junit.Test;
import pl.com.dbs.reports.report.domain.builders.ReportBlockException;
import pl.com.dbs.reports.report.domain.builders.ReportBlockInflationException;
import pl.com.dbs.reports.report.domain.builders.ReportBlocksBuilder;

import java.io.IOException;

import static org.junit.Assert.assertTrue;

/**
 * REPLACE tests
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2014
 */
public class ReportBlockReplaceRuleTest extends ReportBlockRuleTest {
	static final String V_0 = "Whether 'tis nobler in the mind to suffer";
	static final String V_1 = "The slings and arrows of outrageous fortune";
	static final String SENTENCE = "Kto postepuje godniej: ten, kto biernie ";
	

	@Test
	public void shouldnt_have_tags() throws IOException, ReportBlockInflationException, ReportBlockException {
		params = ReportParameterTestBuilder.builder()
				.with("V_0", V_0)
				.with("V_1", V_1)
				.build();

		ReportBlocksBuilder builder = 
				builder("pl/com/dbs/reports/domain/rule/test/test3.txt");
		
		String sb = new String(builder.build().getContent());
		
		System.out.print(sb);
		
		assertTrue(!sb.contains("^$V_0^"));
		assertTrue(!sb.contains("^$V_1^"));
	}
	
	@Test
	public void check_production_pattern() throws IOException, ReportBlockInflationException, ReportBlockException {
		params = ReportParameterTestBuilder.builder()
		.with("INDATWYP", "INDATWYP")
		.with("VROW", "VROW")
		.with("VKOR", "VKOR")
		.with("VNIP", "VNIP")
		.with("VREG", "VREG")
		.with("VNAZ", "VNAZ")
		.with("VPES", "VPES")
		.with("VTYP", "VTYP")
		.with("VDOK", "VDOK")
		.with("VNAZW", "VNAZW")
		.with("VIMIE", "VIMIE")
		.with("VDATUR", "VDATUR")
		.with("VIMIE2", "VIMIE2")
		.with("VNAZW2", "VNAZW2")
		.with("VOBYW", "VOBYW")
		.with("VPLEC", "VPLEC")
		.with("VTYT1", "VTYT1")
		.with("VTYT2", "VTYT2")
		.with("VTYT3", "VTYT3")
		.with("VZUSDEB", "VZUSDEB")
		.with("VKASA", "VKASA")
		.with("VKOD", "VKOD")
		.with("VMIEJSC", "VMIEJSC")
		.with("VGMINA2", "VGMINA2")
		.with("VULICA", "VULICA")
		.with("VNRDOMU", "VNRDOMU")
		.with("VNRMIESZ", "VNRMIESZ")
		.with("VKODKRAJU", "VKODKRAJU")
		.with("VKODZAGR", "VKODZAGR")
		.build();

		ReportBlocksBuilder builder =
				builder("pl/com/dbs/reports/domain/rule/test/zua.txt");
		
		String sb = new String(builder.build().getContent());
		
		System.out.print(sb);
		
		for (pl.com.dbs.reports.api.report.ReportParameter entry : params) {
			assertTrue(!sb.toString().contains("^$"+entry.getName()+"^"));
		}
	}	
}
