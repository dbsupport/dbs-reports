/**
 * 
 */
package pl.com.dbs.reports.profile.web.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import pl.com.dbs.reports.profile.web.form.ProfileForm;

/**
 * Profile data.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
public class ProfileValidator implements Validator {
	private static final int NOTE_MAX = 2000;
	public ProfileValidator() {}
	
	/* (non-Javadoc)
	 * @see org.springframework.validation.Validator#supports(java.lang.Class)
	 */
	@Override
	public boolean supports(Class<?> clazz) {
		return ProfileForm.class.equals(clazz);
	}
	
	@Override
	public void validate(Object target, Errors errors) {
		ProfileForm form = (ProfileForm)target;

		if (errors.hasErrors()) return;
		
		if (form.getNote().length()>NOTE_MAX) errors.rejectValue("note", "errors.max.text", new Integer[]{NOTE_MAX}, "errors.max.text");
	}
}
