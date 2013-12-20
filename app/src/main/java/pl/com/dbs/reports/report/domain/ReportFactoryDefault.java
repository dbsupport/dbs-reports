/**
 * 
 */
package pl.com.dbs.reports.report.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pl.com.dbs.reports.api.report.Report;
import pl.com.dbs.reports.api.report.ReportFactory;
import pl.com.dbs.reports.api.report.ReportFormat;
import pl.com.dbs.reports.api.report.ReportProduceContext;
import pl.com.dbs.reports.api.report.ReportValidationException;
import pl.com.dbs.reports.api.report.pattern.Pattern;
import pl.com.dbs.reports.api.report.pattern.PatternInflater;
import pl.com.dbs.reports.api.report.pattern.PatternTransformate;
import pl.com.dbs.reports.api.support.db.ConnectionContext;
import pl.com.dbs.reports.api.support.db.ConnectionService;
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
	private SqlExecutor executor;
	private ProfileDao profileDao;
	private ConnectionService connectionService;
	
	@Autowired
	public ReportFactoryDefault(SqlExecutor executor, ProfileDao profileDao, ConnectionService connectionService) {
		this.executor = executor;
		this.profileDao = profileDao;
		this.connectionService = connectionService;
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
	public Report produce(final ReportProduceContext context) throws ReportValidationException {
		ReportPattern pattern = ((ReportProduceContextDefault)context).getPattern();
		ReportFormat format = ((ReportProduceContextDefault)context).getFormat();
		String name = ((ReportProduceContextDefault)context).getName();
		Map<String, String> params = ((ReportProduceContextDefault)context).getParameters();
		
		//..report builder..
		Profile profile = profileDao.find(SessionContext.getProfile().getId());
		final ReportBuilder builder = new ReportBuilder(pattern, profile, format, name);
		builder.addParams(new HashMap<String, String>(params));
		
		//..immutable map..
		params = new HashMap<String, String>(params);
		//..and now add to params profile parameters..
		if (profile.isGlobal()) params.put(Profile.PARAMETER_UUID, profile.getUuid());

		logger.debug("Input parameters:");
		for (Map.Entry<String, String> entry : params.entrySet()) logger.debug(entry.getKey()+":"+entry.getValue());
		
		//..find transformate..
		PatternTransformate transformate = resolveTransformate(pattern, format);
		Validate.notNull(transformate, "Transformate is no more!");

		//..build inflaters..
		List<ReportBlockInflation> inflations = resolveInflations(transformate);
		
		//..deconstruct transformate on blocks..
		//FIXME: find builder
		ReportBlock root = new ReportTextBlocksBuilder(transformate.getContent()).build().getBlock();
		//block.print();
		//..iterate through blocks..
		for (ReportBlock block : root.getBlocks()) inflate(block, inflations, builder, connectionService.getContext());
		
		//..finally return new report...
		return builder.build().getReport();
	}
	
	private void inflate(final ReportBlock root, final List<ReportBlockInflation> inflations, final ReportBuilder builder, final ConnectionContext connectionContext) throws ReportValidationException {
		if (root.hasContent()) {
			//..no more block inside.. generate content..
			builder.addContent(root.build(builder.getParams()));
			return;
		}
		
		//..determine how many times block should be inflated..
		LinkedList<Map<String, String>> params = new LinkedList<Map<String, String>>();
		//..select inflater (by label)..
		final ReportBlockInflation inflation = root.findInflater(inflations);
		if (inflation!=null) {
			try {
				//..run inflater.. query db.. and convert to list of result parameters..
				params = inflation.build(executor.execute(connectionContext, inflation.build(builder.getParams())));
			} catch (Exception e) {
				throw new ReportValidationException(e, "report.execute.detailed.error");
			}
		}
		
		//..repeat block generation as many times as received data on list..
		for (Map<String, String> param : params) {
			//..remember parameters (put on stack latest values)..
			builder.addParams(param);
			//..go deeper and inflate children..
			for (final ReportBlock block : root.getBlocks()) {
				inflate(block, inflations, builder, connectionContext);
			}
		}
	}
	
	/**
	 * Find transformate for given pattern..
	 * FIXME: to API?
	 */
	private PatternTransformate resolveTransformate(Pattern pattern, ReportFormat format) {
		for (PatternTransformate transformate : pattern.getTransformates())		
			if (transformate.getFormat().equals(format)) return transformate;
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
