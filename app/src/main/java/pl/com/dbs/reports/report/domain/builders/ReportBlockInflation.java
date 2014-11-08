/**
 * 
 */
package pl.com.dbs.reports.report.domain.builders;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.com.dbs.reports.support.utils.separator.Separator;

/**
 * This is SQL with unresolved parameters.  
 * Needs to be inflated mostly by some data.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2013
 */
public class ReportBlockInflation {
	private static final Logger logger = LoggerFactory.getLogger(ReportBlockInflation.class);
	private String label;
	private String sql;
	
	/**
	 * Inflater expects parameters named like those..
	 * i.e: INPUT_PARAMETER
	 */
	protected Set<String> input = new HashSet<String>();
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
	public boolean isApplicable(String label) {
		return this.label.equalsIgnoreCase(label);
	}

	/**
	 * Return SQL.
	 * If necessary replace it's input parameters with those from provided params' map.
	 */
	public String build(Map<String, String> params) throws ReportBlockInflationException {
		//String result = new String(sql);
		StringBuffer result = new StringBuffer(sql);
		
		if (params.isEmpty()) {
			if (!input.isEmpty()) throw new ReportBlockInflationException("Inflation("+label+") requires input parameters!");
		} else {
			//..replace input parameters in SQL with provided parameters..
			for (String key : input) {
				String value = params.get(key);
				if (value==null) throw new ReportBlockInflationException("Inflation("+label+") requires value for parameter: "+key+" but cant find one!");
				result = replace(result, key, value);
			}
		}
		
		logger.debug("SQL builded "+label+":"+result);
		return result.toString();
	}
	
	private StringBuffer replace(StringBuffer sb, String key, String value) {
		Matcher m = Pattern.compile("\\^\\$"+key+"\\^", Pattern.CASE_INSENSITIVE).matcher(sb);
		if (m.find()) {
			//String buf = result.substring(m.start(), m.end()).toUpperCase();
			return replace(sb.replace(m.start(), m.end(), value), key, value);
		}					
		return sb;
	}
	
	/**
	 * Return results from ResultSet.
	 * If ResultSet is not applicable to inflater throws IllegalStateException.
	 */
//	LinkedList<ReportParameter> build(final ResultSet rs) throws SQLException {
//		LinkedList<Map<String, String>> result = new LinkedList<Map<String, String>>();
//		
//		while (rs.next()) {
//			Map<String, String> params = new HashMap<String, String>();
//			ResultSetMetaData metaData = rs.getMetaData();
//			for (int i=1; i<=metaData.getColumnCount(); i++) {
//				String key = metaData.getColumnName(i);
//				String value = rs.getString(key);
//				params.put(key, value);
//			}
//			result.add(params);
//		}
//		
////		while (rs.next()) {
////			Map<String, String> params = new HashMap<String, String>();
////			for (String key : output) {
////				String value = rs.getString(key);
////				if (value==null) throw new IllegalStateException("Inflation("+label+") requires output parameter:"+key+" but cant find one!");
////				params.put(key, value);
////			}
////			result.add(params);
////		}
//		return result;
//	}
	
	@Override
	public String toString() {
		Separator separator = new Separator(";");
		StringBuilder sb = new StringBuilder("label:").append(label);
		if (input!=null&&!input.isEmpty()) {
			sb.append(separator).append("input:");
			Separator s = new Separator(",");
			for (String variable : input) sb.append(s).append(variable);
		}
		if (!StringUtils.isBlank(sql)) {
			int max=1000;
			sb.append(separator).append("sql:").append(sql.length()>max?(sql.substring(0, max)+".."):sql);
		}
		return sb.toString();
	}
}
