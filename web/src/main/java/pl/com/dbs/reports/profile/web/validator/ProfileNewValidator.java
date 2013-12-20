/**
 * 
 */
package pl.com.dbs.reports.profile.web.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import pl.com.dbs.reports.profile.service.ProfileService;
import pl.com.dbs.reports.profile.web.form.ProfileNewForm;

/**
 * TODO
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
public class ProfileNewValidator implements Validator {
	private ProfileService profileService;
	
	public ProfileNewValidator(ProfileService profileService) {
		this.profileService = profileService;
	}
	
	/* (non-Javadoc)
	 * @see org.springframework.validation.Validator#supports(java.lang.Class)
	 */
	@Override
	public boolean supports(Class<?> clazz) {
		return ProfileNewForm.class.equals(clazz);
	}
	
	@Override
	public void validate(Object target, Errors errors) {
		ProfileNewForm form = (ProfileNewForm)target;
		
		switch (form.getPage()) {
		case 1:
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "login", "errors.required");
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "passwd", "errors.required");
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "firstName", "errors.required");
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "lastName", "errors.required");
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "city", "errors.required");
			
			if (errors.hasErrors()) return;
			
			if (profileService.find(form.getLogin())!=null) {
				errors.rejectValue("login", "profile.already.exists");
			}
		break;
		case 2:
		}

	}

}
