/**
 * 
 */
package pl.com.dbs.reports.report.domain;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;

import pl.com.dbs.reports.asset.api.Asset;
import pl.com.dbs.reports.profile.domain.Profile;
import pl.com.dbs.reports.report.api.Pattern;
import pl.com.dbs.reports.support.db.domain.AEntity;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

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
	@SequenceGenerator(name = "sg_pattern", sequenceName = "sre_pattern", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sg_pattern")
	//@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "sg_report_pattern")
	private Long id;
	
	@Column(name = "upload_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date uploadDate;
	//private Profile owner;
	
	/**
	 * Unpacked files (without manifest file).
	 */
	@OneToMany(mappedBy="pattern", cascade=CascadeType.ALL)
	private List<PatternAsset> assets;
	
	/**
	 * Manifest file.  
	 */
	@Column(name = "manifest")
	private String manifest;
	
	@Transient
	private Manifest _manifest;
	
	public ReportPattern() {/* JPA */}
	
	public ReportPattern(Manifest manifest, List<PatternAsset> assets, Profile owner) {
		Validate.notNull(manifest, "Manifest is no more!");
		//Validate.notNull(owner, "Owner ise no more!");
		this.uploadDate = new Date();
		//this.owner = owner;
		
		try {
			OutputStream os = new ByteArrayOutputStream();
			manifest.write(os);
			os.close();
			this.manifest = os.toString();
		} catch (IOException e) {}
		
		this.assets = assets;
		for (PatternAsset asset : assets) asset.addPattern(this);
	}
	
	public void addAsset(final PatternAsset asset) {
		Validate.notNull(asset, "Asset is no more!");
		Validate.notNull(asset.getPath(), "Asset path is no more!");
		if (Iterables.find(assets, new Predicate<Asset>() { @Override public boolean apply(Asset input) { return asset.getPath().equals(input.getPath()); } }, null)!=null)
			throw new IllegalStateException("Asset "+asset.getPath()+" already exists!");
		this.assets.add(asset);
	}

	public List<String> getRoles() {
		String roles = getAttribute(Pattern.ATTRIBUTE_ROLES);
		List<String> result = new ArrayList<String>();
		if (!StringUtils.isBlank(roles)) {
			StringTokenizer t = new StringTokenizer(roles, ",");
			for (String role = StringUtils.trimToEmpty(t.nextToken()); t.hasMoreTokens();) 
				if (!result.contains(role)) result.add(role);
		}
		return result;			
	}

	@Override
	public Manifest getManifest() {
		try {
			if (this._manifest==null) 
				this._manifest = new Manifest(new ByteArrayInputStream(this.manifest.getBytes()));
		} catch (IOException e) {}
		
		return this._manifest;
	}

	@Override
	public List<? extends Asset> getAssets() {
		return this.assets;
	}

	@Override
	public String getAttribute(String key) {
		Attributes attrs = getManifest().getAttributes(Pattern.ATTRIBUTE_SECTION);
		return attrs.getValue(key);
	}

}
