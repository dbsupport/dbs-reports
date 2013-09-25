/**
 * 
 */
package pl.com.dbs.reports.profile.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import pl.com.dbs.reports.support.web.alerts.Alerts;

/**
 * Obsluga uzytkownikow
 * 
 * @author krzysztof.kaziura@gmail.com
 */
@Controller
@Scope("request")
public class ProfileListController {
	@Autowired private Alerts alerts;
	
//	@ModelAttribute(UserNewForm.KEY)
//    public UserNewForm createForm() {
//		UserNewForm form = new UserNewForm();
//		return form;
//    }		
	
	@RequestMapping(value="/profile/list", method = RequestMethod.GET)
    public String get(Model model) {
		return "profile/profile-list";
    }
	
	
//	@InitBinder
//	protected void initBinder(WebDataBinder binder) {
//		if (binder.getTarget() instanceof UserNewForm) binder.setValidator(new UserNewValidator());
//	}
}
