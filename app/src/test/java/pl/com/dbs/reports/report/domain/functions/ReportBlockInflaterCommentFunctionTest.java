/**
 * 
 */
package pl.com.dbs.reports.report.domain.functions;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pl.com.dbs.reports.api.report.ReportLog;
import pl.com.dbs.reports.report.domain.builders.ReportBlocksBuildContext;
import pl.com.dbs.reports.report.domain.builders.ReportTextBlock;
import pl.com.dbs.reports.report.domain.builders.inflaters.functions.ReportBlockInflaterCommentFunction;

/**
 * Raport generation tests.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
public class ReportBlockInflaterCommentFunctionTest {
	private ReportTextBlock root;
	private List<ReportLog> logs;
	private ReportBlocksBuildContext context; 
	private Map<String, String> params;
	
	@Before
	public void doBeforeEachTestCase() {
		params = new HashMap<String, String>();
		
		root = mock(ReportTextBlock.class);
		when(root.getBlocks()).thenReturn(new LinkedList<ReportTextBlock>());
		
		logs = new LinkedList<ReportLog>();
		context = new ReportBlocksBuildContext(root, params, logs, new StringBuilder());
	}
	
	@After  
    public void tearDown() {  
    } 	
	
	@Test
	public void poprawnie_wykryj() {
		String msg = "Lorem ipsum dolor";
		params.put("OUT_COMMENT", "resUlT:1|msg:"+msg);
		ReportBlockInflaterCommentFunction f = new ReportBlockInflaterCommentFunction();
		
		for (Map.Entry<String, String> param : params.entrySet()) {
			if (f.supports(param)) f.apply(context, param);
		}
		
		assertTrue(logs.size()==1);
		assertTrue(logs.get(0).getMsg().equals(msg));
	}	
}
