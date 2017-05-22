package pl.com.dbs.reports.report.domain.builders;

import org.apache.commons.lang.StringUtils;
import pl.com.dbs.reports.api.report.ReportParameter;
import pl.com.dbs.reports.api.report.pattern.PatternTransformate;
import pl.com.dbs.reports.report.domain.builders.inflaters.ReportTextBlockInflater;

import java.util.List;
import java.util.regex.Matcher;

/**
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2017
 */
public class ReportBlockBuilderResolver {
	private ReportTextBlockInflater inflater;

	public ReportBlockBuilderResolver(ReportTextBlockInflater inflater) {
		this.inflater = inflater;
	}

	public ReportBlocksBuilder resolve(final PatternTransformate transformate, final List<ReportParameter> params) {
		ReportBlocksBuilder blocksbuilder = null;
		if (isReportExtension(transformate, "pdf")) {
			if (isPatternExtension(transformate, "rtf")) {
				blocksbuilder = new ReportRtfPdfBlocksBuilder(transformate, inflater, params);
			} else {
				blocksbuilder = new ReportTextPdfBlocksBuilder(transformate, inflater, params);
			}
		} else {
			blocksbuilder = new ReportTextBlocksBuilder(transformate, inflater, params);
		}

		return blocksbuilder;
	}

	private boolean isReportExtension(final PatternTransformate transformate, String ext) {
		if (StringUtils.isBlank(transformate.getFormat().getReportExtension())) return false;

		java.util.regex.Pattern extpattern = java.util.regex.Pattern.compile("^"+ext+"$",  java.util.regex.Pattern.CASE_INSENSITIVE);
		Matcher m = extpattern.matcher(transformate.getFormat().getReportExtension());
		m.reset();
		return m.find();
	}

	private boolean isPatternExtension(final PatternTransformate transformate, String ext) {
		if (StringUtils.isBlank(transformate.getFormat().getPatternExtension())) return false;

		java.util.regex.Pattern extpattern = java.util.regex.Pattern.compile("^"+ext+"$",  java.util.regex.Pattern.CASE_INSENSITIVE);
		Matcher m = extpattern.matcher(transformate.getFormat().getPatternExtension());
		m.reset();
		return m.find();
	}

}
