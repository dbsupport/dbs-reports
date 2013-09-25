/**
 * 
 */
package pl.com.dbs.reports.profile.web.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import pl.com.dbs.reports.profile.web.form.ProfileNewForm;

/**
 * @author krzysztof.kaziura@gmail.com
 *
 */
public class ProfileNewValidator implements Validator {
	public ProfileNewValidator() {}
	
	/* (non-Javadoc)
	 * @see org.springframework.validation.Validator#supports(java.lang.Class)
	 */
	@Override
	public boolean supports(Class<?> clazz) {
		return ProfileNewForm.class.equals(clazz);
	}
	
	@Override
	public void validate(Object target, Errors errors) {
		ProfileNewForm form = (ProfileNewForm)target;
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "firstName", "errors.required");

		if (errors.hasErrors()) return;
	}

}
