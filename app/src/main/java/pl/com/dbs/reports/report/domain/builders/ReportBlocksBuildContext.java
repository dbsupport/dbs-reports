/**
 * 
 */
package pl.com.dbs.reports.report.domain.builders;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;

import pl.com.dbs.reports.api.report.ReportLog;
import pl.com.dbs.reports.api.report.ReportLogType;

/**
 * Inflater context .
 * After inflation returns content and logs;
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2014
 */
public class ReportBlocksBuildContext {
	private Map<String, String> params;
	private StringBuilder sb;
	private LinkedList<ReportTextBlock> blocks;
	private List<ReportLog> logs;
	
	
	public ReportBlocksBuildContext(ReportTextBlock root, Map<String, String> params, List<ReportLog> logs, StringBuilder sb) {
		this.blocks = root.getBlocks();
		this.params = params;
		this.logs = logs;
		this.sb = sb;
	}
	
	public LinkedList<ReportTextBlock> getBlocks() {
		return blocks;
	}
	
	public Map<String, String> getParams() {
		return params;
	}
	
	public ReportBlocksBuildContext addParam(Entry<String, String> param) {
		if (param==null||StringUtils.isBlank(param.getKey())) return this;
		String key = param.getKey().toUpperCase().trim();

		//..if param already exists - replace it..
		if (params.containsKey(key)) params.remove(key);
		//if (encodingContext!=null) params.put(key, encodingService.encode(value, encodingContext)); else 
		params.put(key, param.getValue());
		return this;
	}
	
	public ReportBlocksBuildContext addLog(final ReportLogType type, final String msg) {
		logs.add(new ReportLog() {
			@Override
			public ReportLogType getType() {
				return type;
			}
			@Override
			public String getMsg() {
				return msg;
			}
			@Override
			public Date getDate() {
				return new Date();
			}
		});
		return this;
	}
	
	public StringBuilder getContent() {
		return sb;
	}

}
