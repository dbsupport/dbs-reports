/**
 * 
 */
package pl.com.dbs.reports.report.pattern.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import pl.com.dbs.reports.api.report.pattern.PatternFormat;
import pl.com.dbs.reports.api.report.pattern.PatternInflater;
import pl.com.dbs.reports.api.report.pattern.PatternTransformate;
import pl.com.dbs.reports.support.db.domain.IEntity;



/**
 * Report transformate.
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
	
    @Embedded
    private ReportPatternFormat format;
	
	@Lob
	@Column(name = "content")
	@Basic(fetch = FetchType.LAZY)
	private byte[] content;
	
	//@OneToMany(mappedBy="transformate", orphanRemoval=true)
	@ManyToMany(fetch=FetchType.EAGER)
	  @JoinTable(
	      name="tre_pattern_trainf",
	      joinColumns={@JoinColumn(name="transformate_id", referencedColumnName="id")},
	      inverseJoinColumns={@JoinColumn(name="inflater_id", referencedColumnName="id")})	
	private List<ReportPatternInflater> inflaters = new ArrayList<ReportPatternInflater>();
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="pattern_id")
	private ReportPattern pattern;	
	
	public ReportPatternTransformate() { /* JPA*/ }
	
	public ReportPatternTransformate(final byte[] content, final String name, final PatternFormat format) {
//		Validate.notNull(content, "A content is no more!");
//		Validate.isTrue(content.length>0, "A content is 0!");
//		Validate.notEmpty(name, "A name is no more!");
//		Validate.notNull(type, "A type is no more!");
		this.name = name;
		this.format = (ReportPatternFormat)format;
		this.content = content;
		this.inflaters = new ArrayList<ReportPatternInflater>();
	}
	
	public void setPattern(ReportPattern pattern) {
		this.pattern = pattern;
	}
	
	public void addInflater(ReportPatternInflater inflater) {
		this.inflaters.add(inflater);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public PatternFormat getFormat() {
		return format;
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
