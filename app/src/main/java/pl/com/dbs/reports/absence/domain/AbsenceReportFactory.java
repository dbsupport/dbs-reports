/**
 * 
 */
package pl.com.dbs.reports.absence.domain;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.inject.internal.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import pl.com.dbs.reports.api.report.*;
import pl.com.dbs.reports.api.report.pattern.PatternFormat;
import pl.com.dbs.reports.api.report.pattern.PatternTransformate;
import pl.com.dbs.reports.profile.domain.Profile;
import pl.com.dbs.reports.report.domain.ReportProduceContextDefault;
import pl.com.dbs.reports.report.domain.builders.ReportBlockBuilderResolver;
import pl.com.dbs.reports.report.domain.builders.ReportBlocksBuilder;
import pl.com.dbs.reports.report.domain.builders.inflaters.ReportTextBlockInflater;
import pl.com.dbs.reports.report.pattern.domain.ReportPattern;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2017
 */
@Slf4j
@Component
public class AbsenceReportFactory implements ReportFactory {
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static final SimpleDateFormat FLAT_DATE_FORMAT = new SimpleDateFormat("yyyyMMddHHmmss");
	private static final String SEPARATOR = ", ";
	private static final String EOL = "\r\n";
	private static final String ABSENCES_PARAM = "V_ABSENCES";
	private static final String ERRORS_PARAM = "V_ERRORS";
	private static final String DATA_PARAM = "V_DATA";

	private AbsenceProcessor absenceProcessor;
	private ReportBlockBuilderResolver builderResolver;

	@Autowired
	public AbsenceReportFactory(AbsenceProcessor absenceProcessor,
								@Qualifier("report.block.inflater.default") ReportTextBlockInflater inflater) {
		this.absenceProcessor = absenceProcessor;
		this.builderResolver = new ReportBlockBuilderResolver(inflater);
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
		List<ReportParameter> params = (List<ReportParameter>)((ReportProduceContextDefault)context).getParameters();
		Profile creator = ((ReportProduceContextDefault)context).getCreator();

		//..find transformate..
		final PatternTransformate transformate = pattern.getTransformate(format);
		Validate.notNull(transformate, "Transformate is no more!");

		ReportParameter param = Iterables.find(params, new Predicate<ReportParameter>() {
			@Override
			public boolean apply(ReportParameter input) {
				return ReportParameterType.FILE.compareTo(input.getType()) == 0;
			}
		});

		Validate.notNull(param, "File parameter is no more!");

		//..convert, validate, upload ftp and execute ssh..
		final AbsenceResult result = absenceProcessor.process(param.getValue().getBytes(), generateFilename(creator));

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

	String generateFilename(Profile creator) {
		String filename = creator.getLogin().replaceAll("\\.", "_").toLowerCase();
		filename = filename.replaceAll("[^a-z0-9_]", "");
		filename += "_" + FLAT_DATE_FORMAT.format(new Date());
		return filename;
	}


	byte[] produce(final AbsenceResult result, final PatternTransformate transformate) {
		List<ReportParameter> params = Lists.newArrayList();

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

		params.add(createParameter(ABSENCES_PARAM, sb.toString()));

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
		params.add(createParameter(ERRORS_PARAM, sb.toString()));
		params.add(createParameter(DATA_PARAM, DATE_FORMAT.format(new Date())));

		//..resolve builder for blocks..
		final ReportBlocksBuilder blocksbuilder = builderResolver.resolve(transformate, params);
		Validate.notNull(blocksbuilder, "Blocks builder is no more!");

		//.inflate blocks tree..
		blocksbuilder.build();

		return blocksbuilder.getContent();
	}

	private ReportParameter createParameter(final String name, final String value) {
		return new ReportParameter() {
			@Override
			public String getName() {
				return name;
			}

			@Override
			public String getValue() {
				return value;
			}

			@Override
			public String getDescription() {
				return null;
			}

			@Override
			public ReportParameterType getType() {
				return ReportParameterType.TEXT;
			}
		};
	}
}
