/**
 * 
 */
package pl.com.dbs.reports.report.web.form;

import org.springframework.web.multipart.MultipartFile;

import pl.com.dbs.reports.api.report.pattern.Pattern;
import pl.com.dbs.reports.support.web.form.AForm;


/**
 * TODO
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
public class ReportPatternImportForm extends AForm {
	public static final String KEY = "reportPatternImportForm";
	private MultipartFile  file;
	private Pattern pattern;
	
	public ReportPatternImportForm() {}
	
	public void reset() {
		super.reset();
		this.file = null;
		this.pattern = null;
	}
	
	public void setup(Pattern pattern) {
		this.pattern = pattern;
	}
	
	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}

	public Pattern getPattern() {
		return pattern;
	}
}
