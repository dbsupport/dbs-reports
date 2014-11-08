/**
 * 
 */
package pl.com.dbs.reports.report.pattern.domain;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import pl.com.dbs.reports.api.report.pattern.PatternForm;
import pl.com.dbs.reports.support.db.domain.IEntity;



/**
 * Input form of given pattern.
 * Allows to put some data before generating report. 
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2013
 */
@Entity
@Table(name = "tre_pattern_form")
public class ReportPatternForm implements IEntity, PatternForm {
	private static final long serialVersionUID = -2021213634165834253L;

	@Id
	@Column(name = "id")
	@SequenceGenerator(name = "sg_pattern_form", sequenceName = "sre_pattern_form", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sg_pattern_form")
	private Long id;
	
	@Column(name = "name")
	private String name;
	
	@Lob
	@Column(name = "content")
	@Basic(fetch = FetchType.LAZY)
	private byte[] content;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="pattern_id")
	private ReportPattern pattern;	
	
	public ReportPatternForm() { /* JPA*/ }
	
	public ReportPatternForm(final byte[] content, final String name) {
//		Validate.notNull(content, "A content is no more!");
//		Validate.isTrue(content.length>0, "A content is 0!");
//		Validate.notEmpty(name, "A name is no more!");
		this.name = name;
		this.content = content;
	}
	
	public void setPattern(ReportPattern pattern) {
		this.pattern = pattern;
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public byte[] getContent() {
		return content;
	}
}
