/**
 * 
 */
package pl.com.dbs.reports.report.service;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import pl.com.dbs.reports.api.report.ReportLoggings;


/**
 * Aspect thats puts into MDC generated report id - for report logging support (logback).
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2014
 */
@Aspect
@Component("report.processing.service.aspect")
public class ReportProcessingServiceAspect {
	
	
	@Pointcut("execution(public * pl.com.dbs.reports.report.service.ReportProcessingService.generate(..)) && args(id)")
	private void pointcut(final long id) {}

	@Before("pointcut(id)")
	public void before(final long id) {
		MDC.put(ReportLoggings.MDC_ID, String.valueOf(id));
	}
	
	@After("pointcut(id)")
	public void after(final long id) {
        MDC.clear();
	}
}
