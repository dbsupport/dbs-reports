/**
 * 
 */
package pl.com.dbs.reports.report.web.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import pl.com.dbs.reports.report.web.form.ReportsArchivedForm;

/**
 * Archives listing validator.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
@Component
public class ReportsArchivedValidator implements Validator {
	public ReportsArchivedValidator() {}
	
	/* (non-Javadoc)
	 * @see org.springframework.validation.Validator#supports(java.lang.Class)
	 */
	@Override
	public boolean supports(Class<?> clazz) {
		return ReportsArchivedForm.class.equals(clazz);
	}
	
	@Override
	public void validate(Object target, Errors errors) {
		ReportsArchivedForm form = (ReportsArchivedForm)target;

		if (errors.hasErrors()) return;
		
		form.getFilter().withName(form.getName());
	}

}
