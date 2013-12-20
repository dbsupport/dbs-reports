/**
 * 
 */
package pl.com.dbs.reports.profile.web.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import pl.com.dbs.reports.profile.web.form.ProfileListForm;

/**
 * TODO
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
public class ProfileListValidator implements Validator {
	public ProfileListValidator() {}
	
	/* (non-Javadoc)
	 * @see org.springframework.validation.Validator#supports(java.lang.Class)
	 */
	@Override
	public boolean supports(Class<?> clazz) {
		return ProfileListForm.class.equals(clazz);
	}
	
	@Override
	public void validate(Object target, Errors errors) {
		ProfileListForm form = (ProfileListForm)target;

		if (errors.hasErrors()) return;
		
		form.getFilter().putName(form.getName());
	}
	

}
