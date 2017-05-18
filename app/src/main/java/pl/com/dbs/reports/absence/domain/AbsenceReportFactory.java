/**
 * 
 */
package pl.com.dbs.reports.absence.domain;

import com.google.common.collect.Maps;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
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

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

/**
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2017
 */
@Component
public class AbsenceReportFactory implements ReportFactory {
	//private static final Logger logger = Logger.getLogger(ReportFactoryDefault.class);
	private ReportTextBlockInflater inflater;
	private AbsenceImporter absenceImporter;
	private AbsenceProcessor absenceProcessor;
	private MessageSource messageSource;

	@Autowired
	public AbsenceReportFactory(@Qualifier("report.block.inflater.default") ReportTextBlockInflater inflater,
								AbsenceImporter absenceImporter,
								AbsenceProcessor absenceProcessor,
								MessageSource messageSource) {
		this.inflater = inflater;
		this.absenceImporter = absenceImporter;
		this.absenceProcessor = absenceProcessor;
		this.messageSource = messageSource;
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

		Collection<String> values = params.values();

		String content = null;

		for (Map.Entry<String, String> entry : params.entrySet()) {
			if (entry.getKey().toUpperCase().contains("FILE")) {
				content = entry.getValue();
				break;
			}
		}

		if (content == null) {
			throw new IllegalStateException("Current version requires FILE name parameter for absences reports.");
		}

		//..find transformate..
		PatternTransformate transformate = pattern.getTransformate(format);
		Validate.notNull(transformate, "Transformate is no more!");

		Map<String, String> tmp = Maps.newHashMap();
		try {
			List<AbsenceInput> inputs = absenceImporter.read(content);

			AbsenceResult result = absenceProcessor.process(inputs);
			String separator = "	";
			StringBuilder sb = new StringBuilder();
			for (AbsenceOutput output : result.getAbsences()) {
				sb
					.append(output.getPesel()).append(separator)
					.append(output.getSeries()).append("/").append(output.getNumber()).append(separator)
					.append(output.getMatcle()).append(separator)
					.append(output.getSocdos()).append(separator)
					.append(output.getDateFrom()).append(separator)
					.append(output.getDateTo()).append(separator)
					.append(output.getSicknessCode()).append(separator)
					.append(output.getMotifa()).append("\n");
			}

			tmp.put("$^ABSENCES^", sb.toString());

			sb = new StringBuilder();
			for (AbsenceError error : result.getErrors()) {
				sb
						.append(error.getPesel()).append(separator)
						.append(error.getSeries()).append("/").append(error.getNumber()).append(separator)
						.append(error.getMatcle()).append(separator)
						.append(error.getSocdos()).append(separator)
						.append(error.getDateFrom()).append(separator)
						.append(error.getDateTo()).append(separator)
						.append(error.getSicknessCode()).append(separator)
						.append(error.getMotifa()).append(separator)
						.append(error.getDescription()).append("\n");
			}
			tmp.put("$^ERRORS^", sb.toString());

		} catch (IOException e) {
			String msg = messageSource.getMessage("absence.validation.file.read.problem", null, null);
		} catch (Exception e) {
			throw new IllegalStateException("Error processing absences.", e);
		}
		
		//..resolve builder for blocks..
		final ReportBlocksBuilder blocksbuilder = resolveBlocksBuilder(transformate, params);
		Validate.notNull(blocksbuilder, "Blocks builder is no more!");
		
		//.inflate blocks tree..
		blocksbuilder.build();
		
		//..construct result..
		return new ReportProduceResult() {
			@Override
			public byte[] getContent() {
				return blocksbuilder.getContent();
			}

			@Override
			public ReportProduceStatus getStatus() {
				return ReportProduceStatus.OK;
			}
		};
	}
	
	
	private ReportBlocksBuilder resolveBlocksBuilder(final PatternTransformate transformate, final Map<String, String> params) {
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
