/**
 * 
 */
package pl.com.dbs.reports.report.domain;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import pl.com.dbs.reports.api.report.Report;
import pl.com.dbs.reports.api.report.ReportFactory;
import pl.com.dbs.reports.api.report.ReportFormat;
import pl.com.dbs.reports.api.report.ReportProduceContext;
import pl.com.dbs.reports.api.report.ReportValidationException;
import pl.com.dbs.reports.api.report.pattern.Pattern;
import pl.com.dbs.reports.api.report.pattern.PatternFormat;
import pl.com.dbs.reports.api.report.pattern.PatternInflater;
import pl.com.dbs.reports.api.report.pattern.PatternTransformate;
import pl.com.dbs.reports.api.support.db.SqlExecutor;
import pl.com.dbs.reports.profile.dao.ProfileDao;
import pl.com.dbs.reports.profile.domain.Profile;
import pl.com.dbs.reports.report.pattern.domain.ReportPattern;
import pl.com.dbs.reports.security.domain.SessionContext;

/**
 * Default report generation fatory for some kind of raports.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
@Component
public class ReportFactoryDefault implements ReportFactory {
	private static final Logger logger = Logger.getLogger(ReportFactoryDefault.class);
	private ProfileDao profileDao;
	private SqlExecutor executor;
	
	@Autowired
	public ReportFactoryDefault(ProfileDao profileDao, SqlExecutor executor) {
		this.profileDao = profileDao;
		this.executor = executor;
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
		
		//..report builder..
		Profile profile = profileDao.find(SessionContext.getProfile().getId());
		final ReportBuilder builder = new ReportBuilder(pattern, profile, format, name, params);
		
		//..and now add to params profile parameters..
		if (profile.isGlobal()&&profile.hasClientAuthorities()) params.put(Profile.PARAMETER_PROFILE, profile.getClientAuthority().getMetaData());

		logger.debug("Input parameters:");
		for (Map.Entry<String, String> entry : params.entrySet()) logger.debug(entry.getKey()+":"+entry.getValue());
		
		//..find transformate..
		PatternTransformate transformate = resolveTransformate(pattern, format.getFormat());
		Validate.notNull(transformate, "Transformate is no more!");

		//..build inflaters..
		List<ReportBlockInflation> inflations = resolveInflations(transformate);
		
		//..deconstruct transformate on blocks..
		//FIXME: find builder
		ReportBlock root = new ReportTextBlocksBuilder(transformate.getContent()).build().getBlock();
		//block.print();
		
		//..iterate through blocks..
		for (ReportBlock block : root.getBlocks()) inflate(block, inflations, builder);
			
		//..finally return new report...
		return builder.build().getReport();
	}
	
	@SuppressWarnings("unchecked")
	private void inflate(final ReportBlock root, final List<ReportBlockInflation> inflations, final ReportBuilder builder) throws ReportValidationException, DataAccessException { 
		if (root.hasContent()) {
			//..no more block inside.. generate content..
			builder.addContent(root.build(builder.getParams()));
			return;
		}
		
		//..result of inflations..
		List<Map<String, String>> params = new LinkedList<Map<String, String>>();
		//..select inflater (by label)..
		final ReportBlockInflation inflation = root.findInflater(inflations);
		if (inflation!=null) {
			try {
				//..run inflater.. query db.. and convert to list of result parameters..
				params = 
					(List<Map<String, String>>)executor.execute(inflation.build(builder.getParams()), new RowMapper<Map<String, String>>() {
						@Override
						public Map<String, String> mapRow(ResultSet rs, int rowNum) throws SQLException {
							Map<String, String> params = new HashMap<String, String>();
							ResultSetMetaData metaData = rs.getMetaData();
							for (int i=1; i<=metaData.getColumnCount(); i++) {
								final String key = metaData.getColumnName(i);
								final String value = rs.getString(key);
								params.put(key, value);
							}						
							return params;
						}
					});				
				//params = inflation.build(executor.execute(inflation.build(builder.getParams())));
			} catch (ReportBlockInflationException e) {
				throw new ReportValidationException(e, "report.execute.detailed.error");
			}
		}
		
		//..repeat block generation as many times as received data on list..
		for (Map<String, String> param : params) {
			//..remember parameters (put on stack latest values)..
			builder.addParams(param);
			//..go deeper and inflate children..
			for (final ReportBlock block : root.getBlocks()) {
				inflate(block, inflations, builder);
			}
		}
	}
	
	/**
	 * Find transformate for given pattern..
	 * FIXME: to API?
	 */
	private PatternTransformate resolveTransformate(Pattern pattern, ReportFormat format) {
		for (PatternTransformate transformate : pattern.getTransformates())		
			if (transformate.getFormat().getFormat().equals(format)) return transformate;
		return null;
	}
	
	/**
	 * Iterate thorough all .sql files and obtain all inflaters from them..
	 */
	private List<ReportBlockInflation> resolveInflations(PatternTransformate transformate) {
		List<ReportBlockInflation> inflaters = new ArrayList<ReportBlockInflation>();
		for (PatternInflater inflater : transformate.getInflaters()) {
			inflaters.addAll(new ReportBlockInflationsBuilder(inflater.getContent()).build().getInflations());
		}
		return inflaters;
	}
	
}
