/**
 * 
 */
package pl.com.dbs.reports.report.pattern.web.form;

import org.springframework.web.multipart.MultipartFile;

import pl.com.dbs.reports.report.pattern.domain.ReportPattern;
import pl.com.dbs.reports.support.web.form.AForm;


/**
 * Importing form.
 * Contains INSIDE form of imported pattern (for preview)
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
public class PatternImportForm extends AForm {
	private static final long serialVersionUID = 1911733990433293245L;
	public static final String KEY = "patternImportForm";
	private MultipartFile  file;
	/**
	 * file converted into pattern
	 */
	private ReportPattern pattern;
	
	public PatternImportForm() {}
	
	public void reset(ReportPattern pattern) {
		super.reset();
		this.pattern = pattern;
	}
	
	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}

	public ReportPattern retrievePattern() {
		return pattern;
	}
}
