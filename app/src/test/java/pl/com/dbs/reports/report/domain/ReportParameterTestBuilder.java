package pl.com.dbs.reports.report.domain;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import pl.com.dbs.reports.api.report.ReportParameter;
import pl.com.dbs.reports.api.report.ReportParameterType;

import java.util.List;
import java.util.Map;

/**
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2017
 */
public class ReportParameterTestBuilder {
	private Map<String, String> params = Maps.newHashMap();
	private List<ReportParameter> previous = Lists.newArrayList();

	private ReportParameterTestBuilder() {}

	private ReportParameterTestBuilder(List<ReportParameter> params) {
		this.previous = params;
	}

	public static ReportParameterTestBuilder builder() {
		return new ReportParameterTestBuilder();
	}

	public static ReportParameterTestBuilder builder(List<ReportParameter> params) {
		return new ReportParameterTestBuilder(params);
	}

	public ReportParameterTestBuilder with(final String name, final String value) {
		this.params.put(name, value);
		return this;
	}

	public List<ReportParameter> build() {
		this.previous.addAll(
				Lists.newArrayList(Iterables.transform(this.params.entrySet(), new Function<Map.Entry<String, String>, ReportParameter>() {
			@Override
			public ReportParameter apply(Map.Entry<String, String> input) {
				return build(input.getKey(), input.getValue());
			}
		})));

		return this.previous;
	}

	private ReportParameter build(final String name, final String value) {
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
