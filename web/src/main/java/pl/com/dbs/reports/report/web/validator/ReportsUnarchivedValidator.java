/**
 * 
 */
package pl.com.dbs.reports.report.web.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import pl.com.dbs.reports.report.web.form.ReportsUnarchivedForm;

/**
 * Archives listing validator.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
@Component
public class ReportsUnarchivedValidator implements Validator {
	/* (non-Javadoc)
	 * @see org.springframework.validation.Validator#supports(java.lang.Class)
	 */
	@Override
	public boolean supports(Class<?> clazz) {
		return ReportsUnarchivedForm.class.equals(clazz);
	}
	
	@Override
	public void validate(Object target, Errors errors) {
		ReportsUnarchivedForm form = (ReportsUnarchivedForm)target;
		
		if (errors.hasErrors()) return;
		
		if (form.getAction()!=null) {
			if (!form.anyIDselected()) {
				errors.reject("report.unarchived.no.id.selected");
				return;
			}
		}
		
		if (errors.hasErrors()) return;
		
		if (form.getAction()==null) {
			form.getFilter().withName(form.getValue());
			form.getFilter().inStatuses(form.getStatus().getStatuses());
			form.getFilter().inPhases(form.getPhase().getPhases());
		}
	}

}
