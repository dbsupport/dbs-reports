/**
 * 
 */
package pl.com.dbs.reports.report.web.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import pl.com.dbs.reports.report.web.form.ReportPatternImportForm;

/**
 * TODO
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
public class ReportPatternImportValidator implements Validator {
	public ReportPatternImportValidator() {}
	
	/* (non-Javadoc)
	 * @see org.springframework.validation.Validator#supports(java.lang.Class)
	 */
	@Override
	public boolean supports(Class<?> clazz) {
		return ReportPatternImportForm.class.equals(clazz);
	}
	
	@Override
	public void validate(Object target, Errors errors) {
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "file", "errors.required");
		
		if (errors.hasErrors()) return;
		
		ReportPatternImportForm form = (ReportPatternImportForm)target;
		if (form.getFile().getSize()<=0) errors.rejectValue("file", "report.import.file.empty");
		if (errors.hasErrors()) return;

		
		
	}

}
