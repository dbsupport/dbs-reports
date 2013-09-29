/**
 * 
 */
package pl.com.dbs.reports.profile.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import pl.com.dbs.reports.profile.web.form.ProfileNewForm;
import pl.com.dbs.reports.profile.web.validator.ProfileNewValidator;
import pl.com.dbs.reports.support.web.alerts.Alerts;

/**
 * TODO
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
@Controller
@SessionAttributes({ProfileNewForm.KEY})
@Scope("request")
public class ProfileNewController {
	@Autowired private Alerts alerts;
	
	@ModelAttribute(ProfileNewForm.KEY)
    public ProfileNewForm createForm() {
		ProfileNewForm form = new ProfileNewForm();
		return form;
    }		
	
	@RequestMapping(value="/profile/new", method = RequestMethod.GET)
    public String get(Model model, @ModelAttribute(ProfileNewForm.KEY) final ProfileNewForm form) {
		return "profile/profile-new";
    }
	
	@RequestMapping(value= "/profile/new", method = RequestMethod.POST)
    public String submit(@Valid @ModelAttribute(ProfileNewForm.KEY) final ProfileNewForm form, BindingResult results, HttpServletRequest request, RedirectAttributes ra) {
		if (!results.hasErrors()) {
			alerts.addSuccess(request, "profile.new.added");
		}
		
		return "profile/profile-new";
	}
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		if (binder.getTarget() instanceof ProfileNewForm) binder.setValidator(new ProfileNewValidator());
	}
}
