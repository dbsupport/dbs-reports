/**
 * 
 */
package pl.com.dbs.reports.report.pattern.web.form;

import javax.xml.bind.JAXBException;

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
	public static final String KEY = "patternImportForm";
	private MultipartFile  file;
	private ReportPattern pattern;
	
	public PatternImportForm() {}
	
	public void reset() {
		super.reset();
		this.file = null;
		this.pattern = null;
	}
	
	public void setup(ReportPattern pattern) throws JAXBException {
		this.pattern = pattern;
	}
	
	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}

	public ReportPattern getPattern() {
		return pattern;
	}
	
	public boolean isForm() {
		return this.pattern!=null&&this.pattern.getForm()!=null;
	}
}
