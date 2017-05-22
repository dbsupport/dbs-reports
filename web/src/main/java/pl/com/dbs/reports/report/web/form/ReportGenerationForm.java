/**
 * 
 */
package pl.com.dbs.reports.report.web.form;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import pl.com.dbs.reports.api.report.ReportParameter;
import pl.com.dbs.reports.api.report.ReportParameterType;
import pl.com.dbs.reports.api.report.pattern.PatternFormat;
import pl.com.dbs.reports.report.domain.ReportGenerationContext;
import pl.com.dbs.reports.report.pattern.domain.ReportPattern;
import pl.com.dbs.reports.support.web.form.DForm;
import pl.com.dbs.reports.support.web.form.IFormParameter;
import pl.com.dbs.reports.support.web.form.IFormParameters;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;


/**
 * Report generation form.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2013
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "form", namespace = "http://www.dbs.com.pl/reports/1.0/form")
public class ReportGenerationForm extends DForm implements ReportGenerationContext {
	private static final long serialVersionUID = -5420902467886273804L;
	public static final String KEY = "reportGenerationForm";
	private Long pattern;
	private String name;
	private PatternFormat format;
	/**
	 * ...to satisfy interface..
	 */
	private List<PatternFormat> formats;
	
	public ReportGenerationForm() {
		super();
	}
	
	public void reset(ReportPattern pattern) {
		super.reset();
		this.pattern = pattern.hasId()?pattern.getId():null;
		this.name = pattern.getManifest().getNameTemplate();
		this.formats = pattern.getFormats();
		if (this.formats.size()==1) this.format = formats.get(0);
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public void setExtension(String value) {
		for (PatternFormat format : formats) {
			if (format.getReportExtension().equalsIgnoreCase(value)) {
				this.format = format;
				return;
			}
		}
		this.format = null;
	}
	
	public String getExtension() {
		return this.format!=null?format.getReportExtension():"";
	}

	@Override
	public long getPattern() {
		return pattern;
	}

	@Override
	public PatternFormat getFormat() {
		return format;
	}

	@Override
	public String getName() {
		return name;
	}
	
	public String getFullname() {
		return name+"."+format.getReportExtension();
	}

	@Override
	public List<List<ReportParameter>> getParameters() {
		List<List<ReportParameter>> parameters = Lists.newArrayList();
		for (final IFormParameters params : fetchParameters()) {
			List<ReportParameter> rparams = convert(params.getParameters());
			if (params.isDivisibled()) {
				rparams.add(new ReportParameter() {

					@Override
					public String getName() {
						return params.getName();
					}

					@Override
					public String getValue() {
						return params.getValue();
					}

					@Override
					public String getDescription() {
						return null;
					}

					@Override
					public ReportParameterType getType() {
						return ReportParameterType.DIVISIBLE;
					}
				});
			}
			parameters.add(rparams);
		}

		return parameters;
	}

	private List<ReportParameter> convert(final List<IFormParameter> parameters) {
		return Lists.newArrayList(Iterables.transform(parameters, new Function<IFormParameter, ReportParameter>() {
			@Override
			public ReportParameter apply(IFormParameter input) {
				return convert(input);
			}
		}));
	}

	private ReportParameter convert(final IFormParameter param) {
		return new ReportParameter() {

			@Override
			public String getName() {
				return param.getName();
			}

			@Override
			public String getValue() {
				return param.getFile()==null ? param.getValue() : readFile(param.getFile());
			}

			@Override
			public String getDescription() {
				return param.getDescription();
			}

			@Override
			public ReportParameterType getType() {
				return param.getFile()!=null ? ReportParameterType.FILE : ReportParameterType.TEXT;
			}
		};
	}

}
