/**
 * 
 */
package pl.com.dbs.reports.profile.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import pl.com.dbs.reports.profile.web.form.ProfileListForm;
import pl.com.dbs.reports.profile.web.validator.ProfileListValidator;
import pl.com.dbs.reports.support.web.alerts.Alerts;

/**
 * TODO
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
@Controller
@SessionAttributes({ProfileListForm.KEY})
@Scope("request")
public class ProfileListController {
	@Autowired private Alerts alerts;
	
	@ModelAttribute(ProfileListForm.KEY)
    public ProfileListForm createForm() {
		ProfileListForm form = new ProfileListForm();
		return form;
    }		
	
	@RequestMapping(value="/profile/list", method = RequestMethod.GET)
    public String get(Model model) {
		return "profile/profile-list";
    }
	
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		if (binder.getTarget() instanceof ProfileListForm) binder.setValidator(new ProfileListValidator());
	}
}
