/**
 * 
 */
package pl.com.dbs.reports.report.domain;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
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
import pl.com.dbs.reports.api.report.ReportProduceContext;
import pl.com.dbs.reports.api.report.ReportValidationException;
import pl.com.dbs.reports.api.report.pattern.PatternFormat;
import pl.com.dbs.reports.api.report.pattern.PatternTransformate;
import pl.com.dbs.reports.api.support.db.SqlExecutor;
import pl.com.dbs.reports.profile.dao.ProfileDao;
import pl.com.dbs.reports.profile.domain.Profile;
import pl.com.dbs.reports.report.pattern.domain.ReportPattern;
import pl.com.dbs.reports.security.domain.SessionContext;
import pl.com.dbs.reports.support.encoding.EncodingService;

/**
 * Default report generation factory for some kind of raports.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
@Component
public class ReportFactoryDefault implements ReportFactory {
	private static final Logger logger = Logger.getLogger(ReportFactoryDefault.class);
	private ProfileDao profileDao;
	private SqlExecutor executor;
	@Autowired private EncodingService encodingService;	

	
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
		ReportBuilder builder = new ReportBuilder(pattern, profile, format, name, params);
		
		//..add params that wont be persisted:
		//..add profile login..
		builder.addParam(Profile.PARAMETER_USER, profile.getLogin());
		//..add profile parameter (HR authorities)..
		builder.addParam(Profile.PARAMETER_PROFILE, SessionContext.getProfile().getClientAuthorityMetaData());

		logger.debug("Input parameters:");
		for (Map.Entry<String, String> entry : builder.getParams().entrySet()) logger.debug(entry.getKey()+":"+entry.getValue());
		
		//..find transformate..
		PatternTransformate transformate = pattern.getTransformate(format);
		Validate.notNull(transformate, "Transformate is no more!");
		
		//..inflation context..
		ReportInflationContext rfcontext = new ReportInflationContext(builder, transformate, encodingService.getEncodingContext());
		
		//..deconstruct transformate on blocks..
		//FIXME: find builder
		ReportBlock root = new ReportTextBlocksBuilder(transformate.getContent()).build().getBlock();
		//root.print();
		
		//..iterate through blocks..
		for (ReportBlock block : root.getBlocks()) inflate(block, rfcontext);
			
		//..finally return new report...
		return builder.build().getReport();
	}
	
	@SuppressWarnings("unchecked")
	private void inflate(final ReportBlock root, final ReportInflationContext context) throws ReportValidationException, DataAccessException { 
		if (root.hasContent()) {
			//..no more block inside.. generate content..
			context.getBuilder().addContent(root.build(context.getBuilder().getParams()));
			return;
		}
		//..result of inflations..
		List<Map<String, String>> params = new LinkedList<Map<String, String>>();
		//..select inflater (by label)..
		final ReportBlockInflation inflation = root.findInflater(context.getInflations());
		if (inflation!=null) {
			try {
				//..run inflater.. query db.. and convert to list of result parameters..
				params = 
					(List<Map<String, String>>)executor.execute(inflation.build(context.getBuilder().getParams()), new RowMapper<Map<String, String>>() {
						@Override
						public Map<String, String> mapRow(ResultSet rs, int rowNum) throws SQLException {
							Map<String, String> params = new HashMap<String, String>();
							ResultSetMetaData metaData = rs.getMetaData();
							for (int i=1; i<=metaData.getColumnCount(); i++) {
								final String key = metaData.getColumnName(i);
								final String value = encodingService.encode(rs.getString(key), context.getEncodingContext());
								params.put(key, value);
							}						
							return params;
						}
					});				
			} catch (ReportBlockInflationException e) {
				throw new ReportValidationException(e, "report.execute.detailed.error");
			}
		}
		
		//..repeat block generation as many times as received data on list..
		for (Map<String, String> param : params) {
			//..remember parameters (put on stack latest values)..
			context.getBuilder().addParams(param);
			//..go deeper and inflate children..
			for (final ReportBlock block : root.getBlocks()) {
				inflate(block, context);
			}
		}
	}
	

	
}
