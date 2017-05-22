/**
 * 
 */
package pl.com.dbs.reports.support.web.form;

import org.springframework.web.multipart.MultipartFile;

/**
 * Interface for report generation.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2017
 */
public interface IFormParameter {
		String getName();
		String getValue();
		MultipartFile getFile();
		String getDescription();
}
