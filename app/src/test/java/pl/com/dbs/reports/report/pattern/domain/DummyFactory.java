package pl.com.dbs.reports.report.pattern.domain;

import pl.com.dbs.reports.api.report.ReportFactory;
import pl.com.dbs.reports.api.report.pattern.Pattern;
import pl.com.dbs.reports.api.report.pattern.PatternFactory;
import pl.com.dbs.reports.api.report.pattern.PatternProduceContext;
import pl.com.dbs.reports.api.report.pattern.PatternValidationException;

/**
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2017
 */
public class DummyFactory implements PatternFactory {

	@Override
	public String getName() {
		return DummyFactory.class.getCanonicalName();
	}

	@Override
	public Pattern produce(PatternProduceContext patternProduceContext) throws PatternValidationException {
		return null;
	}

	@Override
	public ReportFactory getReportFactory() {
		return null;
	}
}
