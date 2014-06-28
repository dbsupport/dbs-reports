/**
 * 
 */
package pl.com.dbs.reports.support.web.form.inflater;

import java.util.regex.Pattern;

import pl.com.dbs.reports.support.web.form.field.IFieldInflatable;

/**
 * Dynamic form field inflater.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2014
 */
public interface FieldInflater {
	static final Pattern PREFIX_PATTERN = Pattern.compile("^[a-zA-Z]+:", Pattern.CASE_INSENSITIVE);
	
	/**
	 * Is this field going to be inflated?
	 */
	boolean supports(IFieldInflatable field);

	/**
	 * Ok, lets inflate..
	 */
	FieldInflater inflate(IFieldInflatable field);
}
