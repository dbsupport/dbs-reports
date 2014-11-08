/**
 * 
 */
package pl.com.dbs.reports.report.pattern.web.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import pl.com.dbs.reports.report.pattern.web.form.PatternListForm;

/**
 * Patterns listing validator.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2013
 */
public class PatternListValidator implements Validator {
	public PatternListValidator() {}
	
	/* (non-Javadoc)
	 * @see org.springframework.validation.Validator#supports(java.lang.Class)
	 */
	@Override
	public boolean supports(Class<?> clazz) {
		return PatternListForm.class.equals(clazz);
	}
	
	@Override
	public void validate(Object target, Errors errors) {
		PatternListForm form = (PatternListForm)target;

		if (errors.hasErrors()) return;
		
		
		form.getFilter().forName(form.getValue()).forAccess(form.getAccess());
	}

}
