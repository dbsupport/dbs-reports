/**
 * 
 */
package pl.com.dbs.reports.report.pattern.web.validator;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import pl.com.dbs.reports.report.pattern.web.form.PatternListForm;

/**
 * Patterns listing validator.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
public class PatternListValidator implements Validator {
	private static final DateFormat DATEFORMAT_DEFAULT = new SimpleDateFormat("dd-MM-yyyy");
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
		
		
		form.getFilter().putName(form.getName());
		form.getFilter().putAuthor(form.getAuthor());
		form.getFilter().putCreator(form.getCreator());
		try {
			form.getFilter().putUploadDate(DATEFORMAT_DEFAULT.parse(form.getUploadDate()));
		} catch (Exception e) {
			form.getFilter().putUploadDate(null);
		}
		form.getFilter().putVersion(form.getVersion());
		
	}

}
