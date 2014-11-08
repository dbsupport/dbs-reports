/**
 * 
 */
package pl.com.dbs.reports.activedirectory.web.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import pl.com.dbs.reports.activedirectory.web.form.ActiveDirectoryListForm;
import pl.com.dbs.reports.activedirectory.web.form.ActiveDirectoryListForm.Action;

/**
 * ActiveDirectory data.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2014
 */
public class ActiveDirectoryValidator implements Validator {
	public ActiveDirectoryValidator() {}
	
	/* (non-Javadoc)
	 * @see org.springframework.validation.Validator#supports(java.lang.Class)
	 */
	@Override
	public boolean supports(Class<?> clazz) {
		return ActiveDirectoryListForm.class.equals(clazz);
	}
	
	@Override
	public void validate(Object target, Errors errors) {
		ActiveDirectoryListForm form = (ActiveDirectoryListForm)target;

		if (errors.hasErrors()) return;
		
		if (Action.INSERT.equals(form.getAction())) {
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "date", "errors.required");
			if (errors.hasErrors()) return;
			if (!form.anyIDselected()) {
				errors.reject("activedirectory.no.id.selected");
			}
		}
		
		if (errors.hasErrors()) return;
		
		if (!Action.INSERT.equals(form.getAction())) {
			form.getFilter().putValue(form.getValue());
		}
	}
}
