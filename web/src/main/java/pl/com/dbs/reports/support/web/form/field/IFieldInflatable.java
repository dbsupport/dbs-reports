/**
 * 
 */
package pl.com.dbs.reports.support.web.form.field;

import pl.com.dbs.reports.support.web.form.option.FieldOption;

/**
 * Field provide source (sql) that can inject many options for selection in field. 
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2014
 */
public interface IFieldInflatable {
	String getSource();
	
	IFieldInflatable inflate(FieldOption option);
}
