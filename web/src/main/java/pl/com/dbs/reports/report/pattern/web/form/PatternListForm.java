/**
 * 
 */
package pl.com.dbs.reports.report.pattern.web.form;

import pl.com.dbs.reports.report.pattern.dao.PatternFilter;
import pl.com.dbs.reports.support.web.form.AForm;


/**
 * TODO
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
public class PatternListForm extends AForm {
	public static final String KEY = "patternListForm";
	private String name;
	private String version;
	private String uploadDate;
	private String author;
	private String creator;	
	
	private PatternFilter filter = new PatternFilter();
	
	public PatternListForm() {}
	
	public void reset(String name) {
		filter = new PatternFilter();
		filter.putName(name);
	}

	public PatternFilter getFilter() {
		return filter;
	}

	public void setFilter(PatternFilter filter) {
		this.filter = filter;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public void setUploadDate(String uploadDate) {
		this.uploadDate = uploadDate;
		
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getName() {
		return name;
	}

	public String getVersion() {
		return version;
	}

	public String getUploadDate() {
		return uploadDate;
	}

	public String getAuthor() {
		return author;
	}

	public String getCreator() {
		return creator;
	}
	
}
