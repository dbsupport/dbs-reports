/**
 * 
 */
package pl.com.dbs.reports.report.domain;

import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import pl.com.dbs.reports.api.report.ReportFactory;
import pl.com.dbs.reports.api.report.ReportProduceContext;
import pl.com.dbs.reports.api.report.ReportProduceResult;
import pl.com.dbs.reports.api.report.ReportProduceStatus;
import pl.com.dbs.reports.api.report.pattern.PatternFormat;
import pl.com.dbs.reports.api.report.pattern.PatternTransformate;
import pl.com.dbs.reports.report.domain.builders.ReportBlockBuilderResolver;
import pl.com.dbs.reports.report.domain.builders.ReportBlocksBuilder;
import pl.com.dbs.reports.report.domain.builders.inflaters.ReportTextBlockInflater;
import pl.com.dbs.reports.report.pattern.domain.ReportPattern;

import java.util.List;

/**
 * Default report generation factory for reports.
 * Breakes (deconstructs) content (text) onto blocks tree, 
 * inflates them (fills with db data) using template sqls
 * and merge back (construct) into content.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2013
 */
@Component
public class ReportFactoryDefault implements ReportFactory {
	//private static final Logger logger = Logger.getLogger(ReportFactoryDefault.class);
	private ReportBlockBuilderResolver builderResolver;
	
	@Autowired
	public ReportFactoryDefault(@Qualifier("report.block.inflater.query") ReportTextBlockInflater inflater) {
		this.builderResolver = new ReportBlockBuilderResolver(inflater);
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
	public ReportProduceResult produce(final ReportProduceContext context) {
		ReportPattern pattern = (ReportPattern)(((ReportProduceContextDefault)context).getPattern());
		PatternFormat format = ((ReportProduceContextDefault)context).getFormat();
		List<pl.com.dbs.reports.api.report.ReportParameter> params = (List<pl.com.dbs.reports.api.report.ReportParameter>)((ReportProduceContextDefault)context).getParameters();
		
		//..find transformate..
		PatternTransformate transformate = pattern.getTransformate(format);
		Validate.notNull(transformate, "Transformate is no more!");
		
		//..resolve builder for blocks..
		final ReportBlocksBuilder blocksbuilder = builderResolver.resolve(transformate, params);
		Validate.notNull(blocksbuilder, "Blocks builder is no more!");
		
		//.inflate blocks tree..
		blocksbuilder.build();
		
		//..construct result..
		return new ReportProduceResult() {
			@Override
			public byte[] getContent() {
				return blocksbuilder.getContent();
			}

			@Override
			public ReportProduceStatus getStatus() {
				return ReportProduceStatus.OK;
			}
		};
	}
	
	
}
