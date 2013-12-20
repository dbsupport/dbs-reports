/**
 * 
 */
package pl.com.dbs.reports.report.web.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import pl.com.dbs.reports.report.web.form.ReportExecuteForm;

/**
 * TODO
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
public class ReportExecuteValidator implements Validator {
	public ReportExecuteValidator() {}
	
	/* (non-Javadoc)
	 * @see org.springframework.validation.Validator#supports(java.lang.Class)
	 */
	@Override
	public boolean supports(Class<?> clazz) {
		return ReportExecuteForm.class.equals(clazz);
	}
	
	@Override
	public void validate(Object target, Errors errors) {
		ReportExecuteForm form = (ReportExecuteForm)target;
		
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "pattern", "report.execute.no.pattern");
		if (errors.hasErrors()) return;
		
		switch (form.getPage()) {
		case 1:
		break;
		case 2:
		break;
		case 3:
		break;
		}
	}
}
