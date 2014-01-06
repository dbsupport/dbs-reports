/**
 * 
 */
package pl.com.dbs.reports.report.domain;

import java.util.Map;

import pl.com.dbs.reports.api.report.pattern.PatternFormat;
import pl.com.dbs.reports.profile.domain.Profile;
import pl.com.dbs.reports.report.pattern.domain.ReportPattern;

/**
 * Report context
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2014
 */
public interface ReportCreation {
	ReportPattern getPattern();
	Profile getProfile();
	String getName();
	PatternFormat getFormat();
	byte[] getContent();
	Map<String, String> getParams();
	boolean getTemporary();
}
