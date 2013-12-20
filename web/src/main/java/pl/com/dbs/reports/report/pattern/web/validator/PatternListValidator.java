/**
 * 
 */
package pl.com.dbs.reports.report.web.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import pl.com.dbs.reports.report.web.form.ReportPatternListForm;

/**
 * TODO
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
public class ReportPatternListValidator implements Validator {
	public ReportPatternListValidator() {}
	
	/* (non-Javadoc)
	 * @see org.springframework.validation.Validator#supports(java.lang.Class)
	 */
	@Override
	public boolean supports(Class<?> clazz) {
		return ReportPatternListForm.class.equals(clazz);
	}
	
	@Override
	public void validate(Object target, Errors errors) {
		ReportPatternListForm form = (ReportPatternListForm)target;

		if (errors.hasErrors()) return;
	}

}
