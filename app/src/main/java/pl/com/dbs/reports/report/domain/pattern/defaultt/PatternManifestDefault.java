/**
 * 
 */
package pl.com.dbs.reports.report.domain.pattern.defaultt;

import java.util.ArrayList;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import pl.com.dbs.reports.report.api.PatternManifest;

/**
 * TODO
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
@Embeddable
public class PatternManifestDefault implements PatternManifest {
	@Column(name = "factory")
	private String factory;

	@Column(name = "name")
	private String name;

	@Column(name = "version")
	private String version;

	@Column(name = "roles")
	private String roles;

	/*package*/ PatternManifestDefault() {/* JPA */}
	
	public PatternManifestDefault(Manifest manifest) {
		Attributes attrs = manifest.getAttributes(PatternManifest.SECTION);
		this.factory = attrs.getValue(FACTORY);		
		this.name = attrs.getValue(NAME);
		this.version = attrs.getValue(VERSION);
		this.roles = attrs.getValue(ROLES);
//		if (!StringUtils.isBlank(roles)) {
//			StringTokenizer t = new StringTokenizer(roles, ",");
//			for (String role = StringUtils.trimToEmpty(t.nextToken()); t.hasMoreTokens();) {
//				if (!result.contains(role)) result.add(role);
//			}
//		}		
	}
	
	@Override
	public String getFactory() {
		return factory;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getVersion() {
		return version;
	}

	@Override
	public List<String> getRoles() {
		//TODO:
		return new ArrayList<String>();
	}

}
