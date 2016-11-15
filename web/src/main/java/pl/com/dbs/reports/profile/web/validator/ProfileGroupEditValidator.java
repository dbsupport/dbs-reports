/**
 * 
 */
package pl.com.dbs.reports.profile.web.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import pl.com.dbs.reports.profile.domain.ProfileGroup;
import pl.com.dbs.reports.profile.service.ProfileGroupService;
import pl.com.dbs.reports.profile.web.form.ProfileGroupEditForm;
import pl.com.dbs.reports.profile.web.form.ProfileGroupNewForm;

import java.util.regex.Matcher;

/**
 * Edit profile group validation.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2015
 */
public class ProfileGroupEditValidator implements Validator {
    static final int DESC_MIN = 3;
    static final int DESC_MAX = 512;

	public ProfileGroupEditValidator() {
	}
	
	/* (non-Javadoc)
	 * @see org.springframework.validation.Validator#supports(java.lang.Class)
	 */
	@Override
	public boolean supports(Class<?> clazz) {
		return ProfileGroupEditForm.class.equals(clazz);
	}
	
	@Override
	public void validate(Object target, Errors errors) {
        ProfileGroupEditForm form = (ProfileGroupEditForm)target;

        if (errors.hasErrors()) return;

        String desc = form.getDescription()==null?"":form.getDescription();
        if (desc.length() > 0 && desc.length()<DESC_MIN) errors.rejectValue("description", "errors.min.text", new Integer[]{DESC_MIN}, "errors.min.text");
        if (desc.length() > 0 && desc.length()>DESC_MAX) errors.rejectValue("description", "errors.max.text", new Integer[]{DESC_MAX}, "errors.max.text");
	}

}
