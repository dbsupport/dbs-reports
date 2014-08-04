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
import pl.com.dbs.reports.report.domain.builders.ReportBlocksBuildTerminationException;
import pl.com.dbs.reports.report.domain.builders.ReportTextBlock;
import pl.com.dbs.reports.report.domain.builders.inflaters.functions.ReportBlockInflaterTerminateFunction;

/**
 * Functions test.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
public class ReportBlockInflaterTerminateFunctionTest {
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
		String msg = "Przerywam bo dalej nie ma sensu";
		params.put("OUT_TERMINATE", "Result:1|msg:"+msg);
		ReportBlockInflaterTerminateFunction f = new ReportBlockInflaterTerminateFunction();
		
		for (Map.Entry<String, String> param : params.entrySet()) {
			try {
				if (f.supports(param)) 
					f.apply(context, param);
			} catch (ReportBlocksBuildTerminationException e) {
				
			}
		}
		
		assertTrue(logs.size()==1);
		assertTrue(logs.get(0).getMsg().equals(msg));
	}	
}
