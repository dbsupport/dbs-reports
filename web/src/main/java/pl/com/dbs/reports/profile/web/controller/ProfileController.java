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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import pl.com.dbs.reports.profile.web.form.ProfileNewForm;
import pl.com.dbs.reports.profile.web.validator.ProfileNewValidator;
import pl.com.dbs.reports.support.web.alerts.Alerts;

/**
 * Obsluga uzytkownikow
 * 
 * @author krzysztof.kaziura@gmail.com
 */
@Controller
@Scope("request")
public class ProfileController {
	@Autowired private Alerts alerts;
	
//	@ModelAttribute(UserNewForm.KEY)
//    public UserNewForm createForm() {
//		UserNewForm form = new UserNewForm();
//		return form;
//    }		
	
	@RequestMapping(value="/profile", method = RequestMethod.GET)
    public String get(Model model) {
		return "profile/profile";
    }	
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		if (binder.getTarget() instanceof ProfileNewForm) binder.setValidator(new ProfileNewValidator());
	}
}
