/**
 * 
 */
package pl.com.dbs.reports.report.domain.functions;

import ch.qos.logback.classic.spi.ILoggingEvent;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.com.dbs.reports.api.report.ReportParameter;
import pl.com.dbs.reports.report.ReportLogTestAppender;
import pl.com.dbs.reports.report.domain.builders.ReportBlocksBuildContext;
import pl.com.dbs.reports.report.domain.builders.ReportTextBlock;
import pl.com.dbs.reports.report.domain.builders.inflaters.functions.ReportBlockInflaterCommentFunction;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Raport generation tests.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2013
 */
public class ReportBlockInflaterCommentFunctionTest {
	private ReportTextBlock root;
	private ReportBlocksBuildContext context; 
	private List<ReportParameter> params;
	protected ReportLogTestAppender testAppender;
	
	@Before
	public void doBeforeEachTestCase() {
		params = Lists.newArrayList();
		
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
		Map<String, String> params = Maps.newHashMap();
		String msg = "Lorem ipsum dolor";
		params.put("OUT_COMMENT", "resUlT:1|msg:"+msg);
		ReportBlockInflaterCommentFunction f = new ReportBlockInflaterCommentFunction();
		
		for (Map.Entry<String, String> param: params.entrySet()) {
			if (f.supports(param)) f.apply(context, param);
		}
		
		ILoggingEvent lastEvent = testAppender.getLastEvent();
		assertTrue(msg.equals(lastEvent.getMessage()));
	}	
}
