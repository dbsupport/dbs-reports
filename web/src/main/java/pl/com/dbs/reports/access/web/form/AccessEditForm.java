/**
 * 
 */
package pl.com.dbs.reports.access.web.form;

import pl.com.dbs.reports.access.domain.Access;
import pl.com.dbs.reports.access.domain.AccessModification;



/**
 * TODO
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
public class AccessEditForm extends AccessNewForm implements AccessModification {
	public static final String KEY = "accessEditForm";
	private Long id;

	public AccessEditForm() {
		super();
	}
	
	public void reset(Access access) {
		super.reset();
		this.id = access.getId();
		setName(access.getName());
		setDescription(access.getDescription());
	}
	
	@Override
	public Long getId() {
		return id;
	}	
	
}
