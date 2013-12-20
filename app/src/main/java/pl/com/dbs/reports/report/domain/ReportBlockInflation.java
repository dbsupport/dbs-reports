/**
 * 
 */
package pl.com.dbs.reports.report.domain;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import pl.com.dbs.reports.support.utils.separator.Separator;

/**
 * TODO
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
public class ReportBlockInflation {
	private static final Logger logger = Logger.getLogger(ReportBlockInflation.class);
	private String label;
	private String sql;
	
	/**
	 * Inflater expects parameters named like those..
	 * i.e: INPUT_PARAMETER
	 */
	private Set<String> input = new HashSet<String>();
	/**
	 * Inflater produces data named like those..
	 */
	//private Set<String> output = new HashSet<String>();
	
	ReportBlockInflation(String label, String sql) {
		this.label = label.toUpperCase();
		this.sql = sql.replace("\n", " ").replace("\r", "");
	}
	
	public String getSql() {
		return sql;
	}
	
	public String getLabel() {
		return label;
	}	

	void addInput(String key) {
		key = key.toUpperCase();
		if (!input.contains(key))
			input.add(key);
	}
	
//	void addOutput(String key) {
//		key = key.toUpperCase();
//		if (!output.contains(key))
//			output.add(key);
//	}	

	/**
	 * Is this inflater applicable for that block?
	 */
	boolean isApplicable(ReportBlock block) {
		return this.label.equalsIgnoreCase(block.getLabel());
	}

	/**
	 * Return SQL.
	 * If necessary replace it's input parameters with those from provided params' map.
	 */
	String build(Map<String, String> params) {
		String result = new String(sql);
		
		if (params.isEmpty()) {
			if (!input.isEmpty()) throw new IllegalStateException("Inflation("+label+") requires input parameters!");
		} else {
			//..replace input parameters in SQL with provided parameters..
			for (String key : input) {
				String value = params.get(key);
				if (value==null) throw new IllegalStateException("Inflation("+label+") requires input parameter:"+key+" but cant find one!");
				result = result.replaceAll("\\^\\$"+key+"\\^", value);
			}
		}
		
		logger.debug("SQL builded "+label+":"+result);
		return result;
	}
	
	/**
	 * Return results from ResultSet.
	 * If ResultSet is not applicable to inflater throws IllegalStateException.
	 */
	LinkedList<Map<String, String>> build(final ResultSet rs) throws SQLException {
		LinkedList<Map<String, String>> result = new LinkedList<Map<String, String>>();
		
		while (rs.next()) {
			Map<String, String> params = new HashMap<String, String>();
			ResultSetMetaData metaData = rs.getMetaData();
			for (int i=1; i<=metaData.getColumnCount(); i++) {
				String key = metaData.getColumnName(i);
				String value = rs.getString(key);
				params.put(key, value);
			}
			result.add(params);
		}
		
//		while (rs.next()) {
//			Map<String, String> params = new HashMap<String, String>();
//			for (String key : output) {
//				String value = rs.getString(key);
//				if (value==null) throw new IllegalStateException("Inflation("+label+") requires output parameter:"+key+" but cant find one!");
//				params.put(key, value);
//			}
//			result.add(params);
//		}
		return result;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("label:").append(label);
		sb.append(";input:");
		Separator s = new Separator(",");
		for (String variable : input) sb.append(s).append(variable);
//		sb.append(";output:");
//		for (String variable : output) sb.append(s).append(variable);
		int max=1000;
		sb.append(";sql:").append(sql.length()>max?(sql.substring(0, max)+".."):sql);
		return sb.toString();
	}
}
