/**
 * 
 */
package pl.com.dbs.reports.report.pattern.domain;

import java.util.List;

import pl.com.dbs.reports.api.report.pattern.PatternManifest;
import pl.com.dbs.reports.profile.domain.Profile;

/**
 * Creation interface.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2014
 */
public interface ReportPatternCreation {
	String getFilename();
	byte[] getContent(); 
	Profile getCreator(); 
	String getName(); 
	String getVersion(); 
	String getAuthor(); 
	String getFactory(); 
	PatternManifest getManifest();
	List<String> getAccesses();
	List<ReportPatternTransformate> getTransformates();
	List<ReportPatternForm> getForms();
}
