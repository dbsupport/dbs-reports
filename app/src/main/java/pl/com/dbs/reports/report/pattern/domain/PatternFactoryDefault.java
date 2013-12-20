/**
 * 
 */
package pl.com.dbs.reports.report.pattern.domain;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.lang.Validate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pl.com.dbs.reports.api.report.ReportFactory;
import pl.com.dbs.reports.api.report.pattern.Pattern;
import pl.com.dbs.reports.api.report.pattern.PatternFactory;
import pl.com.dbs.reports.api.report.pattern.PatternProduceContext;
import pl.com.dbs.reports.api.report.pattern.PatternValidationException;
import pl.com.dbs.reports.api.report.pattern.PatternValidator;
import pl.com.dbs.reports.profile.dao.ProfileDao;
import pl.com.dbs.reports.report.domain.ReportFactoryDefault;
import pl.com.dbs.reports.security.domain.SessionContext;

/**
 * Domyslna obsluga paczek z wzorcami (definicjami) raportow.
 * 
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
@Component
public final class PatternFactoryDefault implements PatternFactory {
	private static final Log logger = LogFactory.getLog(PatternFactoryDefault.class);
	
	private List<PatternValidator> validators;
	private ProfileDao profileDao;
	private ReportFactoryDefault factory;
	
	@Autowired
	public PatternFactoryDefault(ProfileDao profileDao, ReportFactoryDefault factory, List<PatternValidator> validators) {
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
			
			for (PatternValidator validator : validators) {
				logger.info("["+validator.getOrder()+"]"+" validating: "+ validator.getClass());
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
		return PatternFactoryDefault.class.getCanonicalName();
	}
	
	/* (non-Javadoc)
	 * @see pl.com.dbs.reports.api.report.pattern.PatternFactory#getReportFactory()
	 */
	@Override
	public ReportFactory getReportFactory() {
		return factory;
	}

}
