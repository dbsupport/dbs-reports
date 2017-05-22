/**
 * 
 */
package pl.com.dbs.reports.report.domain;

import org.apache.commons.lang.Validate;
import pl.com.dbs.reports.api.report.ReportParameterType;
import pl.com.dbs.reports.support.db.domain.AEntity;
import pl.com.dbs.reports.support.db.domain.IEntity;

import javax.persistence.*;


/**
 * 
 * Report parameter entity.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2017
 */
@Entity
@Table(name = "tre_report_parameter")
public class ReportParameter implements IEntity, pl.com.dbs.reports.api.report.ReportParameter {

	@Id
	@Column(name = "id")
	@SequenceGenerator(name = "sg_report_parameter", sequenceName = "sre_report_parameter", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sg_report_parameter")
	private Long id;

	@Column(name = "name")
	private String name;

	@Column(name = "value")
	private String value;

	@Column(name = "description")
	private String description;

	@Lob
	@Column(name = "content")
	@Basic(fetch = FetchType.LAZY)
	private byte[] content;

	@Column(name = "type")
	@Enumerated(EnumType.STRING)
	private ReportParameterType type;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="report_id")
	private Report report;

	public ReportParameter() {/* JPA */}

    public ReportParameter(String name, String value) {
    	Validate.notNull(name, "name is no more!");
    	Validate.notNull(value, "value is no more!");

    	this.name = name;
		this.value = value;
		this.type = ReportParameterType.TEXT;
    }

	public ReportParameter(String name, final byte[] content) {
		Validate.notNull(name, "name is no more!");
		Validate.notNull(content, "content is no more!");

		this.name = name;
		this.content = content;
		this.type = ReportParameterType.FILE;
	}
    
    public ReportParameter description(String description) {
    	this.description = description;
    	return this;
    }

	public ReportParameter type(ReportParameterType type) {
		this.type = type;
		return this;
	}

	public ReportParameter report(Report report) {
		this.report = report;
		return this;
	}

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
		return description;
	}

	public byte[] getContent() {
		return content;
	}

	@Override
	public ReportParameterType getType() {
		return type;
	}
}
