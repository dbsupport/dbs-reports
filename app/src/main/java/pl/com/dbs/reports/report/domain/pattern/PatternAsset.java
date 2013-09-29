/**
 * 
 */
package pl.com.dbs.reports.report.domain.pattern;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.commons.lang.Validate;

import pl.com.dbs.reports.asset.domain.Asset;
import pl.com.dbs.reports.support.db.domain.AEntity;

/**
 * TODO
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
	
	@Lob
	@Column(name = "content")
	@Basic(fetch = FetchType.LAZY)
	private byte[] content;
	
	public PatternAsset() {/* JPA */}
	
	public PatternAsset(String name, byte[] content) {
		this(name);
		Validate.isTrue(content.length>0, "Content is empty!");
		this.content = content;
	}
	
//	public PatternAsset(String name, StringBuilder sb) {
//		this(name);
//		this.content = String.valueOf(sb).getBytes();
//	}
	
	private PatternAsset(String name) {
		this.name = name;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Long getId() {
		return id;
	}

	@Override
	public String getName() {
		return name;
	}

	public byte[] getContent() {
		return content;
	}
}
