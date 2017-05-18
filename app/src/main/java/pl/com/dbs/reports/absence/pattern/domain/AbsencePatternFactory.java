/**
 * 
 */
package pl.com.dbs.reports.absence.pattern.domain;

import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.com.dbs.reports.absence.domain.AbsenceReportFactory;
import pl.com.dbs.reports.api.report.ReportFactory;
import pl.com.dbs.reports.api.report.pattern.*;
import pl.com.dbs.reports.profile.dao.ProfileDao;
import pl.com.dbs.reports.report.pattern.domain.PatternBuilder;
import pl.com.dbs.reports.report.pattern.domain.PatternProduceContextDefault;
import pl.com.dbs.reports.security.domain.SessionContext;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2017
 */
@Component
public final class AbsencePatternFactory implements PatternFactory {
	private static final Logger logger = LoggerFactory.getLogger(AbsencePatternFactory.class);

	private List<PatternValidator> validators;
	private ProfileDao profileDao;
	private AbsenceReportFactory factory;

	public AbsencePatternFactory() {}

	@Autowired
	public AbsencePatternFactory(ProfileDao profileDao, AbsenceReportFactory factory, List<PatternValidator> validators) {
		this.profileDao = profileDao;
		this.factory = factory;
		this.validators = validators;
	}

	/**
	 * Validate input data.
	 * Produce data.
	 */
	@Override
	public Pattern produce(final PatternProduceContext context) throws PatternValidationException {
		Validate.notNull(context, "A context is no more!");
		File file = ((PatternProduceContextDefault)context).getFile();
		Validate.notNull(file, "A file is no more!");
		try {
			PatternBuilder builder = new PatternBuilder(file, profileDao.find(SessionContext.getProfile().getId()));
			builder.build();
			int order = 0;
			for (PatternValidator validator : validators) {
				logger.info("["+order+++"]"+" validating: "+ validator.getClass());
				validator.validate(builder.getPattern());
			}
			return builder.getPattern();
		} catch (IOException e) {
			throw new PatternValidationException(e, "report.pattern.import.file.ioexception", e.getMessage());
		}
	}
	

	/* (non-Javadoc)
	 * @see pl.com.dbs.reports.api.report.pattern.PatternFactory#getName()
	 */
	@Override
	public String getName() {
		return AbsencePatternFactory.class.getCanonicalName();
	}
	
	/* (non-Javadoc)
	 * @see pl.com.dbs.reports.api.report.pattern.PatternFactory#getReportFactory()
	 */
	@Override
	public ReportFactory getReportFactory() {
		return factory;
	}

}
