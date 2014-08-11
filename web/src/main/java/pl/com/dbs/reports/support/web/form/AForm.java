/**
 * 
 */
package pl.com.dbs.reports.support.web.form;

import java.io.Serializable;

/**
 * Abstract form with page number.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
public abstract class AForm implements Serializable {
	private static final long serialVersionUID = 1647091363895341839L;
	private int page = 1;
	
	public AForm() {}
	
	public void reset() {
		this.page = 1;
	}
	
	public void setPage(int page) {
		this.page = page;
	}

	public int getPage() {
		return page;
	}	
}
