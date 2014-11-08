/**
 * 
 */
package pl.com.dbs.reports.report.domain.functions;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.com.dbs.reports.report.ReportLogTestAppender;
import pl.com.dbs.reports.report.domain.builders.ReportBlocksBuildContext;
import pl.com.dbs.reports.report.domain.builders.ReportBlocksBuildTerminationException;
import pl.com.dbs.reports.report.domain.builders.ReportTextBlock;
import pl.com.dbs.reports.report.domain.builders.inflaters.functions.ReportBlockInflaterTerminateFunction;
import ch.qos.logback.classic.spi.ILoggingEvent;

/**
 * Functions test.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2013
 */
public class ReportBlockInflaterTerminateFunctionTest {
	private ReportTextBlock root;
	private ReportBlocksBuildContext context; 
	private Map<String, String> params;
	protected ReportLogTestAppender testAppender;
	
	@Before
	public void doBeforeEachTestCase() {
		params = new HashMap<String, String>();
		
		root = mock(ReportTextBlock.class);
		when(root.getBlocks()).thenReturn(new LinkedList<ReportTextBlock>());
		
		context = new ReportBlocksBuildContext(root, params, new StringBuilder());
		
		ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger)LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
	    testAppender = (ReportLogTestAppender)root.getAppender("TEST");
	    if (testAppender != null) testAppender.clear();		
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
		
		ILoggingEvent lastEvent = testAppender.getLastEvent();
		assertTrue(msg.equals(lastEvent.getMessage()));		
	}	
}
