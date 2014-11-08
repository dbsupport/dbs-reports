/**
 * 
 */
package pl.com.dbs.reports.access.web.validator;

import java.util.regex.Matcher;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import pl.com.dbs.reports.access.domain.Access;
import pl.com.dbs.reports.access.web.form.AccessEditForm;

/**
 * Access edition validatior.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2013
 */
public class AccessEditValidator extends AccessNewValidator implements Validator {
	public AccessEditValidator() {}
	
	/* (non-Javadoc)
	 * @see org.springframework.validation.Validator#supports(java.lang.Class)
	 */
	@Override
	public boolean supports(Class<?> clazz) {
		return AccessEditForm.class.equals(clazz);
	}
	
	@Override
	public void validate(Object target, Errors errors) {
		AccessEditForm form = (AccessEditForm)target;

		if (errors.hasErrors()) return;
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "errors.required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "description", "errors.required");
		
		//if (errors.hasErrors()) return;
		
		Matcher m = Access.NAME_PATTERN.matcher(form.getName());
		if (!m.matches()) errors.rejectValue("name", "access.edit.name.invalid");		
		
		if (form.getName().length()<NAME_MIN) errors.rejectValue("name", "errors.min.text", new Integer[]{NAME_MIN}, "errors.min.text");
		if (form.getName().length()>NAME_MAX) errors.rejectValue("name", "errors.max.text", new Integer[]{NAME_MAX}, "errors.max.text");

		if (form.getDescription().length()<DESC_MIN) errors.rejectValue("description", "errors.min.text", new Integer[]{DESC_MIN}, "errors.min.text");
		if (form.getDescription().length()>DESC_MAX) errors.rejectValue("description", "errors.max.text", new Integer[]{DESC_MAX}, "errors.max.text");		
	}

}
