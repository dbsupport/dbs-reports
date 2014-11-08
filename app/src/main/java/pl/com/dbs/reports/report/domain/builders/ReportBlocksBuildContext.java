/**
 * 
 */
package pl.com.dbs.reports.report.domain.builders;

import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;

import pl.com.dbs.reports.api.report.ReportLoggings;

/**
 * Inflater context .
 * After inflation returns content and logs;
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2014
 */
@Slf4j
public class ReportBlocksBuildContext {
	private Map<String, String> params;
	private StringBuilder sb;
	private LinkedList<ReportTextBlock> blocks;
	
	
	public ReportBlocksBuildContext(ReportTextBlock root, Map<String, String> params, StringBuilder sb) {
		this.blocks = root.getBlocks();
		this.params = params;
		this.sb = sb;
		//..add parameters log..
		final StringBuilder lsb = new StringBuilder();
		for (Map.Entry<String, String> param : params.entrySet()) {
			if (!StringUtils.isBlank(param.getValue())) {
				String value = param.getValue().length()>255?(param.getValue().substring(0, 255)+".."):param.getValue();
				lsb.append(param.getKey()).append("=").append(value).append(System.getProperty("line.separator"));
			}
		}
		log.info(ReportLoggings.MRK_USER, lsb.toString());		
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
	
	public StringBuilder getContent() {
		return sb;
	}

}
