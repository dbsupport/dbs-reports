/**
 * 
 */
package pl.com.dbs.reports.report.web.form;

import org.springframework.web.multipart.MultipartFile;

import pl.com.dbs.reports.report.domain.ReportPattern;
import pl.com.dbs.reports.report.web.view.ReportPatternView;


/**
 * TODO
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
public class ReportPatternImportForm {
	public static final String KEY = "reportPatternImportForm";
	private MultipartFile  file;
	private ReportPatternView pattern;
	private int page = 1;
	
	public ReportPatternImportForm() {}
	
	public void reset() {
		this.file = null;
		this.pattern = null;
		this.page = 1;
	}
	
	public void setup(ReportPattern pattern) {
		this.pattern = new ReportPatternView(pattern);
	}
	
	public void setPage(int page) {
		this.page = page;
	}

	public int getPage() {
		return page;
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
