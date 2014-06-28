/**
 * 
 */
package pl.com.dbs.reports.support.web.form.field;

import java.util.List;



/**
 * Field can provide many values and user can check if they divide results on many or not.
 * i.e. if checked - many reports will be generated if many values were provided.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2014
 */
public interface IFieldDivisible {

	/**
	 * Split results onto many?
	 */
	boolean divides();
	
	String getName();
	
	List<String> getValue();
}
