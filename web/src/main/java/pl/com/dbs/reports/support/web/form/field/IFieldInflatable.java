/**
 * 
 */
package pl.com.dbs.reports.support.web.form.field;

import java.util.List;

import pl.com.dbs.reports.support.web.form.option.FieldOption;

/**
 * Field provide source (sql) that can inject many options for selection in field. 
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2014
 */
public interface IFieldInflatable {
	/**
	 * What kind of source?
	 */
	String getSource();

	/**
	 * Inflate with option.
	 */
	IFieldInflatable inflate(FieldOption option);
	
	/**
	 * Has some inflated values?
	 */
	List<FieldOption> getOptions();
}
