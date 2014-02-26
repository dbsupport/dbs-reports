/**
 * 
 */
package pl.com.dbs.reports.report.domain;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import pl.com.dbs.reports.api.report.pattern.PatternFormat;
import pl.com.dbs.reports.profile.domain.Profile;
import pl.com.dbs.reports.report.pattern.domain.ReportPattern;

import com.google.common.collect.Maps;

/**
 * Report builder.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
final class ReportBuilder {
	private static final Logger logger = Logger.getLogger(ReportBuilder.class);
	
	private ReportPattern pattern;
	private PatternFormat format;
	private String name;
	private Map<String, String> inparams;
	private Map<String, String> params;
	private StringBuilder sb;
	private Profile profile;
	private Report report;
	
	ReportBuilder(ReportPattern pattern, Profile profile, PatternFormat format, String name, Map<String, String> params) {
		this.pattern = pattern;
		this.format = format;
		this.name = name;
		//..store only input params ..
		this.inparams = params==null?new HashMap<String, String>():params;
		//..and separately params for processing..
		this.params = Maps.newHashMap(this.inparams);
		this.sb = new StringBuilder();
		this.profile = profile;
	}
	
	ReportBuilder addParam(String key, final String value) {
		if (StringUtils.isBlank(key)) return this;
		key = key.toUpperCase().trim();
		//..if param already exists - replace it..
		if (params.containsKey(key)) params.remove(key);
		//if (encodingContext!=null) params.put(key, encodingService.encode(value, encodingContext)); else 
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
	
//	ReportBuilder encoding(EncodingService encodingService) {
//		this.encodingService = encodingService;
//		this.encodingContext = encodingService!=null?this.encodingService.getEncodingContext():null;
//		return this;
//	}
	
	ReportBuilder build() {
		final String fullname = name+"."+format.getReportExtension();
		
		this.report = new Report(new ReportCreation() {
			@Override
			public boolean getTemporary() {
				return true;
			}
			
			@Override
			public Profile getProfile() {
				return profile;
			}
			
			@Override
			public ReportPattern getPattern() {
				return pattern;
			}
			
			@Override
			public Map<String, String> getParams() {
				return inparams;
			}
			
			@Override
			public String getName() {
				return fullname;
			}
			
			@Override
			public PatternFormat getFormat() {
				return format;
			}
			
			@Override
			public byte[] getContent() {
				return sb.toString().getBytes();
			}
		});
				
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
