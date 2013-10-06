/**
 * 
 */
package pl.com.dbs.reports.report.web.form;

import org.springframework.web.multipart.MultipartFile;

import pl.com.dbs.reports.report.pattern.domain.ReportPattern;
import pl.com.dbs.reports.report.web.view.ReportPatternView;
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
	private ReportPatternView pattern;
	
	public ReportPatternImportForm() {}
	
	public void reset() {
		super.reset();
		this.file = null;
		this.pattern = null;
	}
	
	public void setup(ReportPattern pattern) {
		this.pattern = new ReportPatternView(pattern);
	}
	
	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}

	public ReportPatternView getPattern() {
		return pattern;
	}
}
