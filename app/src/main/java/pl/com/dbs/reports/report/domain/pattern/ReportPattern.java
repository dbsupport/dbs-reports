/**
 * 
 */
package pl.com.dbs.reports.report.domain.pattern;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang.Validate;

import pl.com.dbs.reports.profile.domain.Profile;
import pl.com.dbs.reports.report.api.Pattern;
import pl.com.dbs.reports.report.domain.pattern.defaultt.PatternManifestDefault;
import pl.com.dbs.reports.support.db.domain.AEntity;

/**
 * Encja z wzocem raportu.
 * Obiekt zawiera paczke danych tak jak zostala dostarczona (czyli spakowane).
 * Dla optymalizacji dane z manifestu sa dostepne "od reki".
 *
 * 
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
@Entity
@Table(name = "tre_pattern")
public class ReportPattern extends AEntity implements Pattern {
	private static final long serialVersionUID = 8993097483507616245L;
	
	@Id
	@Column(name = "id")
	@SequenceGenerator(name = "sg_report_pattern", sequenceName = "sre_pattern", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sg_report_pattern")
	//@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "sg_report_pattern")
	private Long id;
	
	@Column(name = "upload_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date uploadDate;
	private Profile creator;
	private PatternAsset assets;
	
	@Embedded
	private PatternManifestDefault manifest;
	
	public ReportPattern() {/* JPA */}
	
	public ReportPattern(PatternManifestDefault manifest, PatternAsset assets) {
		Validate.notNull(assets, "Assets are no more!");
		Validate.notNull(manifest, "Manifest ise no more!");
		this.uploadDate = new Date();
		this.assets = assets;
		this.manifest = manifest;
	}
	
	public Long getId() {
		return id;
	}

	public Date getUploadDate() {
		return uploadDate;
	}

	public Profile getCreator() {
		return creator;
	}

	public PatternAsset getAssets() {
		return assets;
	}

	@Override
	public PatternManifestDefault getManifest() {
		return manifest;
	}

}
