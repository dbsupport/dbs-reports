/**
 * 
 */
package pl.com.dbs.reports.report.pattern.web.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import pl.com.dbs.reports.report.pattern.web.form.PatternImportForm;

/**
 * Report pattern validator.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2013
 */
public class PatternImportValidator implements Validator {
	public PatternImportValidator() {}
	
	/* (non-Javadoc)
	 * @see org.springframework.validation.Validator#supports(java.lang.Class)
	 */
	@Override
	public boolean supports(Class<?> clazz) {
		return PatternImportForm.class.equals(clazz);
	}
	
	@Override
	public void validate(Object target, Errors errors) {
		PatternImportForm form = (PatternImportForm)target;
		
		switch (form.getPage()) {
		case 2:
		case 1:
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "file", "errors.required");
			if (errors.hasErrors()) return;
			if (form.getFile().getSize()<=0) errors.rejectValue("file", "report.pattern.import.file.empty");
			break;
		}
		
		if (errors.hasErrors()) return;
	}
}
