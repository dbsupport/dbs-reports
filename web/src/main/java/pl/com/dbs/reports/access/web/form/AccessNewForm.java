/**
 * 
 */
package pl.com.dbs.reports.access.web.form;

import pl.com.dbs.reports.access.domain.AccessCreation;
import pl.com.dbs.reports.support.web.form.AForm;



/**
 * Access add new form.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2013
 */
public class AccessNewForm extends AForm implements AccessCreation {
	private static final long serialVersionUID = 481960559409383168L;
	public static final String KEY = "accessNewForm";
	private String name;
	private String description;
	
	public AccessNewForm() {
		super();
	}
	
	public void reset() {
		super.reset();
		this.name="";
		this.description="";
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	
}
