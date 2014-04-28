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
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import pl.com.dbs.reports.api.support.db.SqlExecutor;
import pl.com.dbs.reports.api.support.db.SqlExecutorContext;
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
@Component
public class ReportTextBlockQueryInflater {
	/**
	 * Inflation executing endpoint.
	 */
	@Autowired private SqlExecutor<Map<String, String>> executor;
	@Autowired protected EncodingService encodingService;
	
	/**
	 * Iterates through blocks and chenges its content inflating them from sql-inflations.
	 */
	public void inflate(final ReportTextBlock root, final Map<String, String> parameters, final List<ReportBlockInflation> inflations) throws ReportBlockException, ReportBlockInflationException, DataAccessException {
		EncodingContext encodingContext = encodingService.getEncodingContext();
		for (ReportTextBlock block : root.getBlocks()) {
			inflateBlock(block, parameters, inflations, encodingContext);
		}
	}
	
	/**
	 * Takes block and corelated inflation (if any).
	 * Executes inflation - producing parameters which are used to build (replace) block's variables.
	 *  
	 */
	private void inflateBlock(final ReportTextBlock block, final Map<String, String> parameters, final List<ReportBlockInflation> inflations, final EncodingContext encodingContext) throws ReportBlockException, ReportBlockInflationException, DataAccessException { 
		if (block.hasContent()) {
			//..no more block inside.. generate content..
			block.build(parameters);
			return;
		}
		//..result of inflations..
		List<Map<String, String>> params = new LinkedList<Map<String, String>>();
		//..inflate if has inflation..
		ReportBlockInflation inflation = block.findInflater(inflations);
		if (inflation!=null) {
			//..run inflater.. query db.. and convert to list of result parameters..
			params = 
				(List<Map<String, String>>)executor.query(new SqlExecutorContext<Map<String, String>>(
					inflation.build(parameters))
						.mapping(new RowMapper<Map<String, String>>() {
						@Override
						public Map<String, String> mapRow(ResultSet rs, int rowNum) throws SQLException {
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
		}
		
		//..repeat block generation as many times as received data on list..
		for (Map<String, String> param : params) {
			//..remember parameters (put on stack latest values)..
			for (Entry<String, String> p : param.entrySet()) addParam(parameters, p.getKey(), p.getValue());
			//..go deeper and inflate children..
			for (final ReportTextBlock child : block.getBlocks()) {
				inflateBlock(child, parameters, inflations, encodingContext);
			}
		}
	}
	
	private void addParam(final Map<String, String> parameters, String key, final String value) {
		if (StringUtils.isBlank(key)) return;
		key = key.toUpperCase().trim();
		//..if param already exists - replace it..
		if (parameters.containsKey(key)) parameters.remove(key);
		//if (encodingContext!=null) params.put(key, encodingService.encode(value, encodingContext)); else 
		parameters.put(key, value);
	}	
}
