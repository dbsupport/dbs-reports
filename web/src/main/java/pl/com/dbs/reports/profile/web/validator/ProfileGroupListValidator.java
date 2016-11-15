/**
 * 
 */
package pl.com.dbs.reports.profile.web.validator;

import com.google.common.collect.Sets;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import pl.com.dbs.reports.access.domain.Access;
import pl.com.dbs.reports.profile.dao.ProfileGroupsFilter;
import pl.com.dbs.reports.profile.web.form.ProfileGroupListForm;

/**
 * Profiles validation.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2013
 */
public class ProfileGroupListValidator implements Validator {
	public ProfileGroupListValidator() {}
	
	/* (non-Javadoc)
	 * @see org.springframework.validation.Validator#supports(java.lang.Class)
	 */
	@Override
	public boolean supports(Class<?> clazz) {
		return ProfileGroupListForm.class.equals(clazz);
	}
	
	@Override
	public void validate(Object target, Errors errors) {
        ProfileGroupListForm form = (ProfileGroupListForm)target;

		if (errors.hasErrors()) return;

        ProfileGroupsFilter filter = form.getFilter();
        if (form.getAction()==null) {
            filter.name(form.getName());
            filter.accesses(form.getAccesses());
        }
//        else if (ProfileGroupListForm.Action.REMOVE.equals(form.getAction())) {
//            filter.name(null);
//            filter.accesses(Sets.<Access>newHashSet());
//        }
	}
}
