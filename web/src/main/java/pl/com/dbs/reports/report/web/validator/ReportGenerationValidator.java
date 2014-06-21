/**
 * 
 */
package pl.com.dbs.reports.report.web.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import pl.com.dbs.reports.report.web.form.ReportGenerationForm;

/**
 * Dynamic report form validator.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
public class ReportGenerationValidator implements Validator {
	public ReportGenerationValidator() {}
	
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
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "pattern", "report.execute.no.pattern");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "format", "report.execute.no.format");
		if (errors.hasErrors()) return;
		
		switch (form.getPage()) {
		case 3:
		case 2:
		case 1:
			
			form.validate(errors);
		break;
		}
	}
}
