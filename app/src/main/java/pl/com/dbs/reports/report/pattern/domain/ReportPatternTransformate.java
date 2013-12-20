/**
 * 
 */
package pl.com.dbs.reports.report.pattern.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import pl.com.dbs.reports.api.report.ReportType;
import pl.com.dbs.reports.api.report.pattern.PatternInflater;
import pl.com.dbs.reports.api.report.pattern.PatternTransformate;
import pl.com.dbs.reports.support.db.domain.IEntity;



/**
 * TODO
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
@Entity
@Table(name = "tre_pattern_transformate")
public class ReportPatternTransformate implements IEntity, PatternTransformate {
	private static final long serialVersionUID = 743274921545655098L;

	@Id
	@Column(name = "id")
	@SequenceGenerator(name = "sg_pattern_transformate", sequenceName = "sre_pattern_transformate", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sg_pattern_transformate")
	private Long id;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "type")
	@Enumerated(EnumType.STRING)
	private ReportType type;
	
	@Lob
	@Column(name = "content")
	@Basic(fetch = FetchType.LAZY)
	private byte[] content;
	
	@OneToMany(mappedBy="transformate")
	private List<ReportPatternInflater> inflaters;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="pattern_id")
	private ReportPattern pattern;	
	
	public ReportPatternTransformate() { /* JPA*/ }
	
	public ReportPatternTransformate(final byte[] content, final String name, final ReportType type) {
//		Validate.notNull(content, "A content is no more!");
//		Validate.isTrue(content.length>0, "A content is 0!");
//		Validate.notEmpty(name, "A name is no more!");
//		Validate.notNull(type, "A type is no more!");
		this.name = name;
		this.type = type;
		this.content = content;
		this.inflaters = new ArrayList<ReportPatternInflater>();
	}
	
	public void setPattern(ReportPattern pattern) {
		this.pattern = pattern;
	}
	
	public void setInflaters(List<ReportPatternInflater> inflaters) {
		this.inflaters = inflaters;
		for (ReportPatternInflater i : inflaters) i.setTransformate(this);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public ReportType getType() {
		return type;
	}

	@Override
	public byte[] getContent() {
		return content;
	}

	@Override
	public List<? extends PatternInflater> getInflaters() {
		return inflaters;
	}
}
