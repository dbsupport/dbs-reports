/**
 * 
 */
package pl.com.dbs.reports.report.domain;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import pl.com.dbs.reports.api.report.ReportFormat;
import pl.com.dbs.reports.profile.domain.Profile;
import pl.com.dbs.reports.report.pattern.domain.ReportPattern;

/**
 * TODO
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
final class ReportBuilder {
	private static final Logger logger = Logger.getLogger(ReportBuilder.class);
	
	private ReportPattern pattern;
	private ReportFormat format;
	private String name;
	private Map<String, String> params;
	private StringBuilder sb;
	private Profile profile;
	private Report report;
	
	ReportBuilder(ReportPattern pattern, Profile profile, ReportFormat format, String name) {
		this.pattern = pattern;
		this.format = format;
		this.name = name;
		this.params = new HashMap<String, String>();
		this.sb = new StringBuilder();
		this.profile = profile;
	}
	
	ReportBuilder addParam(String key, String value) {
		if (StringUtils.isBlank(key)) return this;
		key = key.toUpperCase().trim();
		//..if param already exists - replace it..
		if (params.containsKey(key)) params.remove(key) ;
		params.put(key, value);
		return this;
	}
	
	ReportBuilder addParams(Map<String, String> params) {
		for (Entry<String, String> param : params.entrySet()) addParam(param.getKey(), param.getValue());
		return this;
	}
	
	ReportBuilder addContent(String content) {
		if (content!=null) sb.append(content);
		return this;
	}
	
	ReportBuilder build() {
		String fullname = name+"."+format.getExt();
		//FIXME: params - powinny byc tylko te przekazane z formatki 
		this.report = new Report(this.pattern, this.profile, fullname, format, sb.toString().getBytes(), params);
		logger.info("Report with name:"+fullname+" is build!");
		return this;
	}
	
	Map<String, String> getParams() {
		return params;
	}

	Report getReport() {
		return report;
	}
}
