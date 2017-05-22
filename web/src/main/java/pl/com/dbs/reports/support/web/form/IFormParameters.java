/**
 * 
 */
package pl.com.dbs.reports.support.web.form;

import java.util.List;

/**
 * Interface for report generation.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2017
 */
public interface IFormParameters {
		List<IFormParameter> getParameters();
		boolean isDivisibled();
		String getName();
		String getValue();
}
