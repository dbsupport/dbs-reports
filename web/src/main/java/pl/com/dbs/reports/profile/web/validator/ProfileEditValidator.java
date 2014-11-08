/**
 * 
 */
package pl.com.dbs.reports.profile.web.validator;

import java.util.regex.Matcher;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import pl.com.dbs.reports.profile.service.ProfileService;
import pl.com.dbs.reports.profile.web.form.ProfileEditForm;
import pl.com.dbs.reports.security.domain.SessionContext;

/**
 * Edit profile validation.
 * @see ProfileNewValidator
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2013
 */
public class ProfileEditValidator extends ProfileNewValidator implements Validator {
	public ProfileEditValidator(ProfileService profileService) {
		super(profileService);
	}
	
	/* (non-Javadoc)
	 * @see org.springframework.validation.Validator#supports(java.lang.Class)
	 */
	@Override
	public boolean supports(Class<?> clazz) {
		return ProfileEditForm.class.equals(clazz);
	}
	
	@Override
	public void validate(Object target, Errors errors) {
		ProfileEditForm form = (ProfileEditForm)target;
		
		
		switch (form.getPage()) {
		case 3:
			if (!form.getAccepted()&&form.getId().equals(SessionContext.getProfile().getId())) {
				errors.rejectValue("accepted", "profile.edit.unaccept.session.profile");
			}
		case 2:
		case 1:
			validateSyntax(target, errors);
	
			if (!form.isGlobal()&&!StringUtils.isBlank(form.getPasswd())) {
				if (form.getPasswd().length()<PASSWD_MIN) errors.rejectValue("passwd", "errors.min.text", new Integer[]{PASSWD_MIN}, "errors.min.text");
				if (form.getPasswd().length()>PASSWD_MAX) errors.rejectValue("passwd", "errors.max.text", new Integer[]{PASSWD_MAX}, "errors.max.text");				
			}
			
			if (errors.hasErrors()) return;
			
			if (form.getLogin().length()<LOGIN_MIN) errors.rejectValue("login", "errors.min.text", new Integer[]{LOGIN_MIN}, "errors.min.text");
			if (form.getLogin().length()>LOGIN_MAX) errors.rejectValue("login", "errors.max.text", new Integer[]{LOGIN_MAX}, "errors.max.text");
			
			Matcher ml = LOGIN_PATTERN.matcher(form.getLogin());
			if (!ml.matches()) errors.rejectValue("login", "errors.regexp");
		}
		
	}

}
