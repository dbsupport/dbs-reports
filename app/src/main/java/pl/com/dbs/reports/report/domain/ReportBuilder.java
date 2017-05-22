package pl.com.dbs.reports.report.domain;

import com.google.common.collect.Lists;
import org.apache.commons.lang.StringUtils;
import pl.com.dbs.reports.api.report.pattern.PatternFormat;
import pl.com.dbs.reports.profile.domain.Profile;
import pl.com.dbs.reports.report.pattern.domain.ReportPattern;

import java.util.List;

/**
 * Report builder.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2013
 */
public final class ReportBuilder {
	private static final int MAX_SUFFIX_LENGTH = 10;
	private static final int MAX_NAME_LENGTH = 20;
	private static final int MAX_EXT_LENGTH = 10;

	private ReportPattern pattern;
	private PatternFormat format;
	private Profile profile;
	private String name;
	private String suffix = "";
	private List<pl.com.dbs.reports.report.domain.ReportParameter> parameters = Lists.newArrayList();

	private ReportBuilder() {}

	public static ReportBuilder builder() {
		return new ReportBuilder();
	}

	public ReportBuilder name(String name) {
		this.name = name;
		return this;
	}

	public ReportBuilder pattern(ReportPattern pattern) {
		this.pattern = pattern;
		return this;
	}


	public ReportBuilder format(PatternFormat format) {
		this.format = format;
		return this;
	}

	public ReportBuilder profile(Profile profile) {
		this.profile = profile;
		return this;
	}


	public ReportBuilder parameters(List<pl.com.dbs.reports.report.domain.ReportParameter> parameters) {
		this.parameters.addAll(parameters);
		return this;
	}

	public ReportBuilder suffix(String suffix) {
		this.suffix = !StringUtils.isBlank(suffix) ? abbreviate(suffix, MAX_SUFFIX_LENGTH) : "";
		return this;
	}

	public Report build() {
		String fullname = abbreviate(name, MAX_NAME_LENGTH) + "-" + suffix;
		fullname += "."+abbreviate(format.getReportExtension(), MAX_EXT_LENGTH);

		return new Report(pattern, fullname, profile)
				.format(format)
				.parameters(this.parameters);
	}

	private String abbreviate(String value, int length) {
		if (value==null) return null;
		value = value.trim();
		if (value.length()<length) return value;
		return value.substring(0, length);
	}
}
