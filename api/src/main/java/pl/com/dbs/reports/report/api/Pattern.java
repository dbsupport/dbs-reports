/**
 * 
 */
package pl.com.dbs.reports.report.api;

import java.util.List;
import java.util.jar.Manifest;

import pl.com.dbs.reports.asset.api.Asset;

/**
 * Interface each Reports' pattern (wzorzec defnicji).
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
public interface Pattern {
	static final String ATTRIBUTE_SECTION = "Database-Support-Reports";
	static final String ATTRIBUTE_NAME = "Reports-Pattern-Name";
	static final String ATTRIBUTE_VERSION = "Reports-Pattern-Version";
	static final String ATTRIBUTE_FACTORY = "Reports-Pattern-Factory";
	static final String ATTRIBUTE_AUTHOR = "Reports-Pattern-Author";
	static final String ATTRIBUTE_ROLES = "Reports-Pattern-Roles";
	
	/**
	 * Manifest file.
	 */
	Manifest getManifest();
	
	/**
	 * Get manifest attribute by name.
	 */
	String getAttribute(String key);

	/**
	 * Get all assets.
	 */
	List<? extends Asset> getAssets();
}
