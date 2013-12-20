/**
 * 
 */
package pl.com.dbs.reports.profile.web.validator;

import java.util.regex.Matcher;

import org.apache.commons.lang.StringUtils;
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
	static final java.util.regex.Pattern LOGIN_PATTERN = java.util.regex.Pattern.compile("^[a-zA-ząćęłńóśźżĄĆĘŁŃÓŚŹŻ0-9\\._\\-]+$",  java.util.regex.Pattern.CASE_INSENSITIVE);
	static final java.util.regex.Pattern NAME_PATTERN = java.util.regex.Pattern.compile("^[a-zA-ząćęłńóśźżĄĆĘŁŃÓŚŹŻ\\- ]+$",  java.util.regex.Pattern.CASE_INSENSITIVE);
	static final java.util.regex.Pattern EMAIL_PATTERN = java.util.regex.Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$",  java.util.regex.Pattern.CASE_INSENSITIVE);
	static final java.util.regex.Pattern PHONE_PATTERN = java.util.regex.Pattern.compile("^[0-9 \\+]{0,12}$",  java.util.regex.Pattern.CASE_INSENSITIVE);
	static final java.util.regex.Pattern CITY_PATTERN = java.util.regex.Pattern.compile("^[a-zA-ZąćęłńóśźżĄĆĘŁŃÓŚŹŻ0-9\\.\\- ]{0,64}$",  java.util.regex.Pattern.CASE_INSENSITIVE);
	static final java.util.regex.Pattern STREET_PATTERN = java.util.regex.Pattern.compile("^[a-zA-ZąćęłńóśźżĄĆĘŁŃÓŚŹŻ0-9\\.\\- \\\\/]{0,64}$",  java.util.regex.Pattern.CASE_INSENSITIVE);
	static final java.util.regex.Pattern STATE_PATTERN = java.util.regex.Pattern.compile("^[a-zA-ZąćęłńóśźżĄĆĘŁŃÓŚŹŻ0-9\\.\\- ]{0,64}$",  java.util.regex.Pattern.CASE_INSENSITIVE);
	static final java.util.regex.Pattern ZIP_PATTERN = java.util.regex.Pattern.compile("^[0-9-]{0,8}$",  java.util.regex.Pattern.CASE_INSENSITIVE);
	
	static final int LOGIN_MIN = 3;
	static final int LOGIN_MAX = 64;
	static final int PASSWD_MIN = 3;
	static final int PASSWD_MAX = 32;
	static final int NAME_MIN = 3;
	static final int NAME_MAX = 64;		
	
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
		case 2:
		case 1:
			validateSyntax(target, errors);
			
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "login", "errors.required");
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "passwd", "errors.required");
			
			if (errors.hasErrors()) return;
			
			if (form.getLogin().length()<LOGIN_MIN) errors.rejectValue("login", "errors.min.text", new Integer[]{LOGIN_MIN}, "errors.min.text");
			if (form.getLogin().length()>LOGIN_MAX) errors.rejectValue("login", "errors.max.text", new Integer[]{LOGIN_MAX}, "errors.max.text");
			
			Matcher ml = LOGIN_PATTERN.matcher(form.getLogin());
			if (!ml.matches()) errors.rejectValue("login", "errors.regexp");
			
			if (form.getPasswd().length()<PASSWD_MIN) errors.rejectValue("passwd", "errors.min.text", new Integer[]{PASSWD_MIN}, "errors.min.text");
			if (form.getPasswd().length()>PASSWD_MAX) errors.rejectValue("passwd", "errors.max.text", new Integer[]{PASSWD_MAX}, "errors.max.text");			

			if (errors.hasErrors()) return;
			
			if (profileService.find(form.getLogin())!=null) {
				errors.rejectValue("login", "profile.already.exists");
			}
		}

	}
	
	void validateSyntax(Object target, Errors errors) {
		ProfileNewForm form = (ProfileNewForm)target;
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "firstName", "errors.required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "lastName", "errors.required");
		
		if (errors.hasErrors()) return;
		
		if (form.getFirstName().length()<NAME_MIN) errors.rejectValue("firstName", "errors.min.text", new Integer[]{NAME_MIN}, "errors.min.text");
		if (form.getFirstName().length()>NAME_MAX) errors.rejectValue("firstName", "errors.max.text", new Integer[]{NAME_MAX}, "errors.max.text");

		Matcher mfn = NAME_PATTERN.matcher(form.getFirstName());
		if (!mfn.matches()) errors.rejectValue("firstName", "errors.regexp");
		
		if (form.getLastName().length()<NAME_MIN) errors.rejectValue("lastName", "errors.min.text", new Integer[]{NAME_MIN}, "errors.min.text");
		if (form.getLastName().length()>NAME_MAX) errors.rejectValue("lastName", "errors.max.text", new Integer[]{NAME_MAX}, "errors.max.text");

		Matcher mln = NAME_PATTERN.matcher(form.getLastName());
		if (!mln.matches()) errors.rejectValue("lastName", "errors.regexp");
		
		if (!StringUtils.isBlank(form.getEmail())) {
			Matcher me = EMAIL_PATTERN.matcher(form.getEmail());
			if (!me.matches()) errors.rejectValue("email", "errors.regexp");
		}	

		Matcher mp = PHONE_PATTERN.matcher(form.getPhone());
		if (!mp.matches()) errors.rejectValue("phone", "errors.regexp");
		
		if (!StringUtils.isBlank(form.getCity())
			||!StringUtils.isBlank(form.getStreet())
			||!StringUtils.isBlank(form.getState())
			||!StringUtils.isBlank(form.getZipCode())) {
			
			Matcher mc = CITY_PATTERN.matcher(form.getCity());
			if (!mc.matches()) errors.rejectValue("city", "errors.regexp");
			
			Matcher ms = STREET_PATTERN.matcher(form.getStreet());
			if (!ms.matches()) errors.rejectValue("street", "errors.regexp");				
			
			Matcher mst = STATE_PATTERN.matcher(form.getState());
			if (!mst.matches()) errors.rejectValue("state", "errors.regexp");				

			Matcher mz = STATE_PATTERN.matcher(form.getZipCode());
			if (!mz.matches()) errors.rejectValue("zipCode", "errors.regexp");				
		}		
	}

}
