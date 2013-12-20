/**
 * 
 */
package pl.com.dbs.reports.report.pattern.domain;

import java.io.IOException;

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

import org.apache.commons.lang.Validate;

import pl.com.dbs.reports.api.report.pattern.PatternInflater;
import pl.com.dbs.reports.support.db.domain.IEntity;


/**
 * TODO
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
@Entity
@Table(name = "tre_pattern_inflater")
public class ReportPatternInflater implements IEntity, PatternInflater {
	private static final long serialVersionUID = -8218474021971435173L;

	@Id
	@Column(name = "id")
	@SequenceGenerator(name = "sg_pattern_inflater", sequenceName = "sre_pattern_inflater", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sg_pattern_inflater")
	private Long id;	
	
	@Column(name = "name")
	private String name;
	
	@Lob
	@Column(name = "content")
	@Basic(fetch = FetchType.LAZY)	
	private byte[] content;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="transformate_id")
	private ReportPatternTransformate transformate;		
	
	
	public ReportPatternInflater() { /*JPA*/ }
	
	public ReportPatternInflater (final byte[] content, final String name) throws IOException {
		Validate.notNull(content, "A content is no more!");
		Validate.isTrue(content.length>0, "A content is 0!");		
		Validate.notEmpty(name, "A name is no more!");
		this.name = name;
		this.content = content;
	}
	
	public void setTransformate(ReportPatternTransformate transformate) {
		this.transformate = transformate;
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
