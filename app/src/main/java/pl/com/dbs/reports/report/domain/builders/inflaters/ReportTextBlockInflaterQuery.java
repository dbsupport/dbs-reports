/**
 * 
 */
package pl.com.dbs.reports.report.domain.builders.inflaters;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import pl.com.dbs.reports.api.support.db.SqlExecutor;
import pl.com.dbs.reports.api.support.db.SqlExecutorContext;
import pl.com.dbs.reports.report.domain.builders.ReportBlockException;
import pl.com.dbs.reports.report.domain.builders.ReportBlockInflation;
import pl.com.dbs.reports.report.domain.builders.ReportBlockInflationException;
import pl.com.dbs.reports.report.domain.builders.ReportBlocksBuildContext;
import pl.com.dbs.reports.report.domain.builders.ReportTextBlock;
import pl.com.dbs.reports.report.domain.builders.inflaters.functions.ReportBlockInflaterFunction;
import pl.com.dbs.reports.support.encoding.EncodingContext;
import pl.com.dbs.reports.support.encoding.EncodingService;

/**
 * Inflates blocks.
 * Takes root block (tree root) and goes deep inside and inflates blocks (if they are inflateables).
 * Requires BD connection.
 * Queries BD during inflation for parameters.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2014
 */
@Component("report.block.inflater.query")
public class ReportTextBlockInflaterQuery extends ReportTextBlockInflater {
	private static final Logger logger = Logger.getLogger(ReportTextBlockInflaterQuery.class);
	/**
	 * Inflation executing endpoint.
	 */
	private SqlExecutor<Map<String, String>> executor;
	private EncodingService encodingService;

	
	@Autowired
	public ReportTextBlockInflaterQuery(SqlExecutor<Map<String, String>> executor, EncodingService encodingService) {
		this.executor = executor;
		this.encodingService = encodingService;
	}
	
	/**
	 * Iterates through blocks and changes its content inflating them from sql-inflations.
	 */
	@Override
	public void inflate(final ReportBlocksBuildContext context) throws ReportBlockException {
		EncodingContext encodingContext = encodingService.getEncodingContext();
		for (ReportTextBlock block : context.getBlocks()) {
			try {
				inflateBlock(block, context, encodingContext);
			} catch (ReportBlockInflationException e) {
				logger.error("Error inflating block:"+block.getLabel(), e);
				throw new ReportBlockException(e);
			}
		}
	}
	
	/**
	 * Takes block and corelated inflation (if any).
	 * Executes inflation - producing parameters which are used to build (replace) block's variables.
	 */
	private void inflateBlock(final ReportTextBlock block, final ReportBlocksBuildContext context, final EncodingContext encodingContext) throws ReportBlockException, ReportBlockInflationException, DataAccessException { 
		if (block.hasContent()) {
			//..no more block inside.. generate content..
			block.build(context.getParams(), context.getContent());
			return;
		}
		//..result of inflations..
		final List<Map<String, String>> result = query(block, context.getParams(), encodingContext);
		
		//..repeat block generation as many times as received data on list..
		for (Map<String, String> row : result) {
			//..remember parameters (put on stack latest values)..
			for (Entry<String, String> param : row.entrySet()) {
				//..is this param a function?
				ReportBlockInflaterFunction function = resolveFunction(param);
				if (function != null) {
					function.apply(context, param);
				} else {
					//..add parameter then..
					context.addParam(param);
				}
			}
			//..go deeper and inflate children..
			for (final ReportTextBlock child : block.getBlocks()) {
				inflateBlock(child, context, encodingContext);
			}
		}
	}
	
	private List<Map<String, String>> query(final ReportTextBlock block, final Map<String, String> parameters, final EncodingContext encodingContext) throws ReportBlockInflationException, DataAccessException {
		//..result of inflations..
		List<Map<String, String>> result = new LinkedList<Map<String, String>>();
		//..inflate if has inflation..
		ReportBlockInflation inflation = block.getInflation();
		
		if (inflation!=null) {
			//..run inflater.. query db.. and convert to list of result parameters..
			result = 
				(List<Map<String, String>>)executor.query(new SqlExecutorContext<Map<String, String>>(
						encodingService.decode(inflation.build(parameters), encodingContext))
						.mapping(new RowMapper<Map<String, String>>() {
						@Override
						public Map<String, String> mapRow(ResultSet rs, int rowNum) throws SQLException {
//							try {
//								Thread.sleep(5000);
//							} catch (InterruptedException e) {}
							
							Map<String, String> params = new HashMap<String, String>();
							ResultSetMetaData metaData = rs.getMetaData();
							for (int i=1; i<=metaData.getColumnCount(); i++) {
								final String key = metaData.getColumnName(i);
								final String value = encodingService.encode(rs.getString(key), encodingContext);
								params.put(key, value);
							}						
							return params;
						}
				}));				
		} else if (block.isInflateable()) {
			logger.error("Block("+block.getLabel()+") requires inflation but cant find one!");
			throw new IllegalStateException("Block("+block.getLabel()+") requires inflation but cant find one!");
		}

		return result;
	}
	
}
