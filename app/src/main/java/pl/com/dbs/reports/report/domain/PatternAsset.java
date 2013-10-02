/**
 * 
 */
package pl.com.dbs.reports.report.domain;

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

import pl.com.dbs.reports.asset.api.Asset;
import pl.com.dbs.reports.support.db.domain.AEntity;

/**
 * Report pattern asset.
 * Pattern assets in separate table.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
@Entity
@Table(name = "tre_pattern_asset")
public class PatternAsset extends AEntity implements Asset {
	private static final long serialVersionUID = 7082338646229729337L;
	
	@Id
	@Column(name = "id")
	@SequenceGenerator(name = "sg_report_pattern_asset", sequenceName = "sre_pattern_asset", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sg_report_pattern_asset")
	//@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "sg_report_pattern")
	private Long id;

	@Column(name = "name")
	private String name;
	
	@Column(name = "path")
	private String path;
	
	@Lob
	@Column(name = "content")
	@Basic(fetch = FetchType.LAZY)
	private byte[] content;
	
    @ManyToOne
    @JoinColumn(name="pattern_id")
	private ReportPattern pattern;
	
	public PatternAsset() {/* JPA */}
	
	public PatternAsset(String name, byte[] content) {
		//Validate.isTrue(content.length>0, "Content is empty!");
		this.name = name;
		this.content = content;
	}

	@Override
	public long getId() {
		return id;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public byte[] getContent() {
		return content;
	}

	@Override
	public String getPath() {
		return path;
	}
}
