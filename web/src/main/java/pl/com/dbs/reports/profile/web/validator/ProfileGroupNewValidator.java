/**
 * 
 */
package pl.com.dbs.reports.profile.web.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import pl.com.dbs.reports.profile.domain.ProfileGroup;
import pl.com.dbs.reports.profile.service.ProfileGroupService;
import pl.com.dbs.reports.profile.web.form.ProfileGroupNewForm;

import java.util.regex.Matcher;

/**
 * New profile group validation.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2015
 */
public class ProfileGroupNewValidator implements Validator {
    static final int NAME_MIN = 3;
    static final int NAME_MAX = 64;
    static final int DESC_MIN = 3;
    static final int DESC_MAX = 512;

	private ProfileGroupService profileGroupService;

	public ProfileGroupNewValidator(ProfileGroupService profileGroupService) {
		this.profileGroupService = profileGroupService;
	}
	
	/* (non-Javadoc)
	 * @see org.springframework.validation.Validator#supports(java.lang.Class)
	 */
	@Override
	public boolean supports(Class<?> clazz) {
		return ProfileGroupNewForm.class.equals(clazz);
	}
	
	@Override
	public void validate(Object target, Errors errors) {
        ProfileGroupNewForm form = (ProfileGroupNewForm)target;

        if (errors.hasErrors()) return;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "errors.required");

        if (errors.hasErrors()) return;

        Matcher m = ProfileGroup.NAME_PATTERN.matcher(form.getName());
        if (!m.matches()) errors.rejectValue("name", "profile.group.new.name.invalid");

        String name = form.getName()==null?"":form.getName();
        if (name.length()<NAME_MIN) errors.rejectValue("name", "errors.min.text", new Integer[]{NAME_MIN}, "errors.min.text");
        if (name.length()>NAME_MAX) errors.rejectValue("name", "errors.max.text", new Integer[]{NAME_MAX}, "errors.max.text");

        String desc = form.getDescription()==null?"":form.getDescription();
        if (desc.length() > 0 && desc.length()<DESC_MIN) errors.rejectValue("description", "errors.min.text", new Integer[]{DESC_MIN}, "errors.min.text");
        if (desc.length() > 0 && desc.length()>DESC_MAX) errors.rejectValue("description", "errors.max.text", new Integer[]{DESC_MAX}, "errors.max.text");

        if (errors.hasErrors()) return;

        if (profileGroupService.findByName(name) != null) {
            errors.rejectValue("name", "profile.group.already.exists");
        }
	}

}
