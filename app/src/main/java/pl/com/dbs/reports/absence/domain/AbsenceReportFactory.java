/**
 * 
 */
package pl.com.dbs.reports.absence.domain;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import pl.com.dbs.reports.api.report.ReportFactory;
import pl.com.dbs.reports.api.report.ReportProduceContext;
import pl.com.dbs.reports.api.report.ReportProduceResult;
import pl.com.dbs.reports.api.report.ReportProduceStatus;
import pl.com.dbs.reports.api.report.pattern.PatternFormat;
import pl.com.dbs.reports.api.report.pattern.PatternTransformate;
import pl.com.dbs.reports.report.domain.ReportProduceContextDefault;
import pl.com.dbs.reports.report.domain.builders.ReportBlocksBuilder;
import pl.com.dbs.reports.report.domain.builders.ReportRtfPdfBlocksBuilder;
import pl.com.dbs.reports.report.domain.builders.ReportTextBlocksBuilder;
import pl.com.dbs.reports.report.domain.builders.ReportTextPdfBlocksBuilder;
import pl.com.dbs.reports.report.domain.builders.inflaters.ReportTextBlockInflater;
import pl.com.dbs.reports.report.pattern.domain.ReportPattern;

import javax.print.DocFlavor;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.regex.Matcher;

/**
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2017
 */
@Slf4j
@Component
public class AbsenceReportFactory implements ReportFactory {
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static final String SEPARATOR = ", ";
	private static final String EOL = "\r\n";
	private static final String ABSENCES_PARAM = "V_ABSENCES";
	private static final String ERRORS_PARAM = "V_ERRORS";
	private static final String DATA_PARAM = "V_DATA";

	private AbsenceProcessor absenceProcessor;
	private ReportTextBlockInflater textBlockInflater;

	@Autowired
	public AbsenceReportFactory(AbsenceProcessor absenceProcessor,
								@Qualifier("report.block.inflater.default") ReportTextBlockInflater textBlockInflater) {
		this.absenceProcessor = absenceProcessor;
		this.textBlockInflater = textBlockInflater;
	}

	/* (non-Javadoc)
	 * @see pl.com.dbs.reports.api.report.ReportFactory#getName()
	 */
	@Override
	public String getName() {
		return AbsenceReportFactory.class.getCanonicalName();
	}

	/* (non-Javadoc)
	 * @see pl.com.dbs.reports.api.report.ReportFactory#produce(pl.com.dbs.reports.api.report.ReportProduceContext)
	 */
	@Override
	public ReportProduceResult produce(final ReportProduceContext context) {
		ReportPattern pattern = (ReportPattern)(((ReportProduceContextDefault)context).getPattern());
		PatternFormat format = ((ReportProduceContextDefault)context).getFormat();
		Map<String, String> params = ((ReportProduceContextDefault)context).getParameters();

		//..find transformate..
		final PatternTransformate transformate = pattern.getTransformate(format);
		Validate.notNull(transformate, "Transformate is no more!");

		Map.Entry<String, String> param = input(params);

		//..convert, validate, upload ftp and execute ssh..
		final AbsenceResult result = absenceProcessor.process(param.getValue().getBytes(), filename(param.getKey()));//FIXME: UTF-8

		//..finally procude report summary..
		return new ReportProduceResult() {
			@Override
			public byte[] getContent() {
				return produce(result, transformate);
			}

			@Override
			public ReportProduceStatus getStatus() {
				return ReportProduceStatus.OK;
			}
		};
	}

	String filename(String key) {
		String filename = key.replaceAll("\\.", "_").toLowerCase();
		filename = filename.replaceAll("[^a-z0-9_]", "");
		return filename;
	}
	
	private ReportBlocksBuilder resolveBlocksBuilder(final PatternTransformate transformate, final Map<String, String> params) {
		ReportBlocksBuilder blocksbuilder = null;
		if (isReportExtension(transformate, "pdf")) {
			if (isPatternExtension(transformate, "rtf")) {
				blocksbuilder = new ReportRtfPdfBlocksBuilder(transformate, textBlockInflater, params);
			} else { 
				blocksbuilder = new ReportTextPdfBlocksBuilder(transformate, textBlockInflater, params);
			}
		} else {
			blocksbuilder = new ReportTextBlocksBuilder(transformate, textBlockInflater, params);
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

	private Map.Entry<String, String> input(final Map<String, String> params) {
		Collection<String> values = params.values();

		Map.Entry<String, String> input = null;

		for (Map.Entry<String, String> entry : params.entrySet()) {
			if (entry.getKey().toUpperCase().contains("FILE")) {
				input = entry;
				break;
			}
		}

		Validate.notNull(input, "Current version requires only FILE (one) parameter for absences reports.");

		return input;
	}

	byte[] produce(final AbsenceResult result, PatternTransformate transformate) {
		Map<String, String> params = Maps.newHashMap();

		StringBuilder sb = new StringBuilder();
		for (AbsenceOutput output : result.getAbsences()) {
			sb
					.append(output.getPesel()).append(SEPARATOR)
					.append(output.getSeries()).append("/").append(output.getNumber()).append(SEPARATOR)
					.append(output.getMatcle()).append(SEPARATOR)
					.append(output.getSocdos()).append(SEPARATOR)
					.append(output.getDateFrom()).append(SEPARATOR)
					.append(output.getDateTo()).append(SEPARATOR)
					.append(output.getSicknessCode()).append(SEPARATOR)
					.append(output.getMotifa()).append(EOL);
		}

		params.put(ABSENCES_PARAM, sb.toString());

		sb = new StringBuilder();
		for (AbsenceError error : result.getErrors()) {
			sb
					.append(error.getPesel()).append(SEPARATOR)
					.append(error.getSeries()).append("/").append(error.getNumber()).append(SEPARATOR)
					.append(error.getMatcle()).append(SEPARATOR)
					.append(error.getSocdos()).append(SEPARATOR)
					.append(error.getDateFrom()).append(SEPARATOR)
					.append(error.getDateTo()).append(SEPARATOR)
					.append(error.getSicknessCode()).append(SEPARATOR)
					.append(error.getMotifa()).append(SEPARATOR)
					.append(error.getDescription()).append(EOL);
		}
		params.put(ERRORS_PARAM, sb.toString());
		params.put(DATA_PARAM, DATE_FORMAT.format(new Date()));

		//..resolve builder for blocks..
		final ReportBlocksBuilder blocksbuilder = resolveBlocksBuilder(transformate, params);
		Validate.notNull(blocksbuilder, "Blocks builder is no more!");

		//.inflate blocks tree..
		blocksbuilder.build();

		return blocksbuilder.getContent();
	}
}
