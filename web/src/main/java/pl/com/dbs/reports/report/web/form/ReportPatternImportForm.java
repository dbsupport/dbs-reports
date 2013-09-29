/**
 * 
 */
package pl.com.dbs.reports.report.web.form;

import org.springframework.web.multipart.MultipartFile;


/**
 * TODO
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
public class ReportPatternImportForm {
	public static final String KEY = "reportPatternImportForm";
	private MultipartFile  file;
	
	public ReportPatternImportForm() {}

	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}
	
}
