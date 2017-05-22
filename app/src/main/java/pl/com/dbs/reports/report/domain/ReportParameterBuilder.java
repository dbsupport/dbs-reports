package pl.com.dbs.reports.report.domain;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import pl.com.dbs.reports.api.report.ReportParameterType;
import pl.com.dbs.reports.profile.domain.Profile;
import pl.com.dbs.reports.security.domain.SessionContext;

import java.util.List;

/**
 * Report parameters builder.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2017
 */
public class ReportParameterBuilder {
	private List<pl.com.dbs.reports.api.report.ReportParameter> parameters = Lists.newArrayList();

	private ReportParameterBuilder() {}

	public static ReportParameterBuilder builder() {
		return new ReportParameterBuilder();
	}

	public ReportParameterBuilder parameters(List<pl.com.dbs.reports.api.report.ReportParameter> parameters) {
		this.parameters.addAll(parameters);
		return this;
	}

	public List<pl.com.dbs.reports.report.domain.ReportParameter> build() {
		List<pl.com.dbs.reports.report.domain.ReportParameter> parameters = Lists.newArrayList();
		//..add parameters: profile login..
		//..add profile parameter (HR authorities)..
		Profile profile = SessionContext.getProfile();
		parameters.add(new pl.com.dbs.reports.report.domain.ReportParameter(Profile.PARAMETER_USER, profile.getLogin()).type(ReportParameterType.PROFILE));
		parameters.add(new pl.com.dbs.reports.report.domain.ReportParameter(Profile.PARAMETER_PROFILE, profile.getClientAuthorityMetaData()).type(ReportParameterType.PROFILE));

		parameters.addAll(
			Lists.newArrayList((Iterables.transform(this.parameters, new Function<pl.com.dbs.reports.api.report.ReportParameter, ReportParameter>() {
				@Override
				public pl.com.dbs.reports.report.domain.ReportParameter apply(pl.com.dbs.reports.api.report.ReportParameter input) {
					return build(input);
				}
			}))));

		return parameters;
	}

	public String getSuffix() {
		Optional<pl.com.dbs.reports.api.report.ReportParameter> divisibles = Iterables.tryFind(this.parameters, new Predicate<pl.com.dbs.reports.api.report.ReportParameter>() {
			@Override
			public boolean apply(pl.com.dbs.reports.api.report.ReportParameter input) {
				return ReportParameterType.DIVISIBLE.equals(input.getType());
			}
		});

		return divisibles.isPresent() ? divisibles.get().getValue().replaceAll("[^A-Za-z0-9 ]", "") : "";
	}

	private pl.com.dbs.reports.report.domain.ReportParameter build(pl.com.dbs.reports.api.report.ReportParameter parameter)  {
		pl.com.dbs.reports.report.domain.ReportParameter result = null;

		if (ReportParameterType.FILE.equals(parameter.getType())) {
			result = new pl.com.dbs.reports.report.domain.ReportParameter(parameter.getName(), parameter.getValue().getBytes());
		} else {
			result = new pl.com.dbs.reports.report.domain.ReportParameter(parameter.getName(), parameter.getValue());
			result.type(parameter.getType());
		}

		result.description(parameter.getDescription());

		return result;
	}

}