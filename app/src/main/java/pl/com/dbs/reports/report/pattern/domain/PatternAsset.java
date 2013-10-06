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

import pl.com.dbs.reports.api.inner.asset.Asset;
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
	@SequenceGenerator(name = "sg_pattern_asset", sequenceName = "sre_pattern_asset", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sg_pattern_asset")
	//@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "sg_report_pattern")
	private Long id;

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
	
	public PatternAsset(String path, byte[] content) {
		//Validate.isTrue(content.length>0, "Content is empty!");
		this.path = path;
		this.content = content;
	}
	
	public void addPattern(ReportPattern pattern) {
		this.pattern = pattern;
	}

	@Override
	public long getId() {
		return id;
	}

	@Override
	public byte[] getContent() {
		return content;
	}

	@Override
	public String getPath() {
		return path;
	}
	
	public String getContentAsString() {
		return String.valueOf(content);	
	}
	
	public String getExtension() {
		return path.lastIndexOf(".")>=0?path.substring(path.lastIndexOf(".")):"";
	}
	
}
