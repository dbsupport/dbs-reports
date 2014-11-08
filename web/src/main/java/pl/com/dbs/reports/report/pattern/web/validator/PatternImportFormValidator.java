/**
 * 
 */
package pl.com.dbs.reports.report.pattern.web.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import pl.com.dbs.reports.report.web.form.ReportGenerationForm;

/**
 * Dynamic report form validator.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2013
 */
public class PatternImportFormValidator implements Validator {
	public PatternImportFormValidator() {}
	
	/* (non-Javadoc)
	 * @see org.springframework.validation.Validator#supports(java.lang.Class)
	 */
	@Override
	public boolean supports(Class<?> clazz) {
		return ReportGenerationForm.class.equals(clazz);
	}
	
	@Override
	public void validate(Object target, Errors errors) {
		ReportGenerationForm form = (ReportGenerationForm)target;
		form.validate(errors);
	}
}
