/**
 * 
 */
package pl.com.dbs.reports.user.web.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import pl.com.dbs.reports.user.web.form.UserNewForm;

/**
 * @author krzysztof.kaziura@gmail.com
 *
 */
public class UserNewValidator implements Validator {
	public UserNewValidator() {}
	
	/* (non-Javadoc)
	 * @see org.springframework.validation.Validator#supports(java.lang.Class)
	 */
	@Override
	public boolean supports(Class<?> clazz) {
		return UserNewForm.class.equals(clazz);
	}
	
	@Override
	public void validate(Object target, Errors errors) {
		UserNewForm form = (UserNewForm)target;
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "firstName", "errors.required");

		if (errors.hasErrors()) return;
	}

}
