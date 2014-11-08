/**
 * 
 */
package pl.com.dbs.reports.support.web.form.field;

import java.util.List;

import pl.com.dbs.reports.support.web.form.option.FieldOption;

/**
 * Marks fields with many options.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2014
 */
public interface IFieldSelectable {
	List<FieldOption> getOptions();
}
