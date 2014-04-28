/**
 * 
 */
package pl.com.dbs.reports.report.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import pl.com.dbs.reports.api.report.Report;
import pl.com.dbs.reports.api.report.ReportFactory;
import pl.com.dbs.reports.api.report.ReportProduceContext;
import pl.com.dbs.reports.api.report.ReportValidationException;
import pl.com.dbs.reports.api.report.pattern.PatternFormat;
import pl.com.dbs.reports.api.report.pattern.PatternInflater;
import pl.com.dbs.reports.api.report.pattern.PatternTransformate;
import pl.com.dbs.reports.profile.dao.ProfileDao;
import pl.com.dbs.reports.profile.domain.Profile;
import pl.com.dbs.reports.report.pattern.domain.ReportPattern;
import pl.com.dbs.reports.security.domain.SessionContext;

import com.google.common.collect.Maps;

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
	@Autowired private ReportTextBlockQueryInflater inflater;
	
	@Autowired
	public ReportFactoryDefault(ProfileDao profileDao) {
		this.profileDao = profileDao;
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
		ReportBlocksBuilder blocksbuilder = resolveBlocksBuilder(transformate);
		Validate.notNull(blocksbuilder, "Blocks builder is no more!");
		blocksbuilder.deconstruct();
		
		//.inflate blocks tree..
		try {
			inflater.inflate(blocksbuilder.getRootBlock(), resolveParameters(params), resolveInflations(transformate));
		} catch (ReportBlockException e) {
			throw new ReportValidationException(e, "report.execute.detailed.error");
		} catch (ReportBlockInflationException e) {
			throw new ReportValidationException(e, "report.execute.detailed.error");
		}
		
		//..construct blocks tree back..
		blocksbuilder.construct();
		
		//..report builder..
		Profile profile = profileDao.find(SessionContext.getProfile().getId());
		ReportBuilder reportbuilder = new ReportBuilder(pattern, profile, format, name, params, blocksbuilder.getContent());
		
		//..finally return new report...
		return reportbuilder.build().getReport();
	}
	
	
	private ReportBlocksBuilder resolveBlocksBuilder(final PatternTransformate transformate) {
		ReportBlocksBuilder builder = null;
		if (isReportExtension(transformate, "pdf")) {
			if (isPatternExtension(transformate, "rtf")) {
				builder = new ReportRtfPdfBlocksBuilder(transformate.getContent());
			} else { 
				builder = new ReportTextPdfBlocksBuilder(transformate.getContent());
			}
		} else {
			builder = new ReportTextBlocksBuilder(transformate.getContent());
		}	
		return builder;
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
	
	private Map<String, String> resolveParameters(final Map<String, String> params) {
		//..copy of params..
		Map<String, String> parameters = Maps.newHashMap(params==null?new HashMap<String, String>():params);
		
		//Profile profile = profileDao.find(SessionContext.getProfile().getId());
		//..add profile login..
		parameters.put(Profile.PARAMETER_USER, SessionContext.getProfile().getLogin());//profile.getLogin());
		//..add profile parameter (HR authorities)..
		parameters.put(Profile.PARAMETER_PROFILE, SessionContext.getProfile().getClientAuthorityMetaData());
		return parameters;
	}
	
	/**
	 * Iterate thorough all .sql files and obtain all inflaters from them..
	 */
	private List<ReportBlockInflation> resolveInflations(final PatternTransformate transformate) {
		List<ReportBlockInflation> inflations = new ArrayList<ReportBlockInflation>();
		for (PatternInflater inflater : transformate.getInflaters()) {
			inflations.addAll(new ReportBlockInflationsBuilder(inflater.getContent()).build().getInflations());
		}
		return inflations;
	}	
}
