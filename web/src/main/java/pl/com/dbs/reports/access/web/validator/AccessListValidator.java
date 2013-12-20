/**
 * 
 */
package pl.com.dbs.reports.access.web.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import pl.com.dbs.reports.access.web.form.AccessListForm;

/**
 * TODO
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
public class AccessListValidator implements Validator {
	public AccessListValidator() {}
	
	/* (non-Javadoc)
	 * @see org.springframework.validation.Validator#supports(java.lang.Class)
	 */
	@Override
	public boolean supports(Class<?> clazz) {
		return AccessListForm.class.equals(clazz);
	}
	
	@Override
	public void validate(Object target, Errors errors) {
		AccessListForm form = (AccessListForm)target;

		if (errors.hasErrors()) return;
		
		form.getFilter().putName(form.getName());

	}

}
