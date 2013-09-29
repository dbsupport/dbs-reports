/**
 * 
 */
package pl.com.dbs.reports.report.api;

import java.util.List;

/**
 * Manifest file interface.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
public interface PatternManifest {
	static final String SECTION = "Database-Support-Reports";
	static final String NAME = "Reports-Pattern-Name";
	static final String VERSION = "Reports-Pattern-Version";
	static final String FACTORY = "Reports-Pattern-Factory";
	static final String ROLES = "Reports-Pattern-Roles";	
	
	String getFactory();
	String getName();
	String getVersion();
	List<String> getRoles();	
}
