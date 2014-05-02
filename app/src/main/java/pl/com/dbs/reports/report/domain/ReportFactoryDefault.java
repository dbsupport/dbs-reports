/**
 * 
 */
package pl.com.dbs.reports.report.domain;

import java.util.Map;
import java.util.regex.Matcher;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import pl.com.dbs.reports.api.report.Report;
import pl.com.dbs.reports.api.report.ReportFactory;
import pl.com.dbs.reports.api.report.ReportProduceContext;
import pl.com.dbs.reports.api.report.ReportValidationException;
import pl.com.dbs.reports.api.report.pattern.PatternFormat;
import pl.com.dbs.reports.api.report.pattern.PatternTransformate;
import pl.com.dbs.reports.profile.dao.ProfileDao;
import pl.com.dbs.reports.profile.domain.Profile;
import pl.com.dbs.reports.report.domain.builders.ReportBlocksBuilder;
import pl.com.dbs.reports.report.domain.builders.ReportRtfPdfBlocksBuilder;
import pl.com.dbs.reports.report.domain.builders.ReportTextBlockInflater;
import pl.com.dbs.reports.report.domain.builders.ReportTextBlocksBuilder;
import pl.com.dbs.reports.report.domain.builders.ReportTextPdfBlocksBuilder;
import pl.com.dbs.reports.report.pattern.domain.ReportPattern;
import pl.com.dbs.reports.security.domain.SessionContext;

/**
 * Default report generation factory for reports.
 * Breakes (deconstructs) content (text) onto blocks tree, 
 * inflates them (fills with db data) using template sqls
 * and merge back (construct) into content.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
@Component
public class ReportFactoryDefault implements ReportFactory {
	private static final Logger logger = Logger.getLogger(ReportFactoryDefault.class);
	private ProfileDao profileDao;
	private ReportTextBlockInflater inflater;
	
	@Autowired
	public ReportFactoryDefault(ProfileDao profileDao, @Qualifier("report.block.inflater.query") ReportTextBlockInflater inflater) {
		this.profileDao = profileDao;
		this.inflater = inflater;
	}
	
	
	
	/* (non-Javadoc)
	 * @see pl.com.dbs.reports.api.report.ReportFactory#getName()
	 */
	@Override
	public String getName() {
		return ReportFactoryDefault.class.getCanonicalName();
	}

	/* (non-Javadoc)
	 * @see pl.com.dbs.reports.api.report.ReportFactory#produce(pl.com.dbs.reports.api.report.ReportProduceContext)
	 */
	@Override
	public Report produce(final ReportProduceContext context) throws ReportValidationException, DataAccessException {
		ReportPattern pattern = ((ReportProduceContextDefault)context).getPattern();
		PatternFormat format = ((ReportProduceContextDefault)context).getFormat();
		String name = ((ReportProduceContextDefault)context).getName();
		Map<String, String> params = ((ReportProduceContextDefault)context).getParameters();

		//..find transformate..
		PatternTransformate transformate = pattern.getTransformate(format);
		Validate.notNull(transformate, "Transformate is no more!");
		
		logger.debug("Input parameters:");
		for (Map.Entry<String, String> entry : params.entrySet()) logger.debug(entry.getKey()+":"+entry.getValue());
		
		//..resolve builder for blocks..
		ReportBlocksBuilder blocksbuilder = resolveBlocksBuilder(transformate, params);
		Validate.notNull(blocksbuilder, "Blocks builder is no more!");
		
		//.inflate blocks tree..
		try {
			blocksbuilder.build();
		} catch (ReportBlockException e) {
			throw new ReportValidationException(e);
		}
		
		//..report builder..
		Profile profile = profileDao.find(SessionContext.getProfile().getId());
		ReportBuilder reportbuilder = new ReportBuilder(pattern, profile, format, name, params, blocksbuilder.getContent());
		
		//..finally return new report...
		return reportbuilder.build().getReport();
	}
	
	
	private ReportBlocksBuilder resolveBlocksBuilder(final PatternTransformate transformate, final Map<String, String> params) {
		ReportBlocksBuilder blocksbuilder = null;
		if (isReportExtension(transformate, "pdf")) {
			if (isPatternExtension(transformate, "rtf")) {
				blocksbuilder = new ReportRtfPdfBlocksBuilder(transformate, inflater, params);
			} else { 
				blocksbuilder = new ReportTextPdfBlocksBuilder(transformate, inflater, params);
			}
		} else {
			blocksbuilder = new ReportTextBlocksBuilder(transformate, inflater, params);
		}
		
		if (blocksbuilder!=null) {
			//Profile profile = profileDao.find(SessionContext.getProfile().getId());
			//..add profile login..
			blocksbuilder.addParameter(Profile.PARAMETER_USER, SessionContext.getProfile().getLogin())
			//..add profile parameter (HR authorities)..
			.addParameter(Profile.PARAMETER_PROFILE, SessionContext.getProfile().getClientAuthorityMetaData());
		}

		return blocksbuilder;
	}
	
	private boolean isReportExtension(final PatternTransformate transformate, String ext) {
		if (StringUtils.isBlank(transformate.getFormat().getReportExtension())) return false;
		
		java.util.regex.Pattern extpattern = java.util.regex.Pattern.compile("^"+ext+"$",  java.util.regex.Pattern.CASE_INSENSITIVE);
	    Matcher m = extpattern.matcher(transformate.getFormat().getReportExtension());
	    m.reset();
	    return m.find();
	}
	
	private boolean isPatternExtension(final PatternTransformate transformate, String ext) {
		if (StringUtils.isBlank(transformate.getFormat().getPatternExtension())) return false;
		
		java.util.regex.Pattern extpattern = java.util.regex.Pattern.compile("^"+ext+"$",  java.util.regex.Pattern.CASE_INSENSITIVE);
	    Matcher m = extpattern.matcher(transformate.getFormat().getPatternExtension());
	    m.reset();
	    return m.find();
	}		
}
