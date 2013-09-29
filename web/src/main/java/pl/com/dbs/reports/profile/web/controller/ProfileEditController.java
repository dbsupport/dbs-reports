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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import pl.com.dbs.reports.profile.web.form.ProfileEditForm;
import pl.com.dbs.reports.profile.web.validator.ProfileEditValidator;
import pl.com.dbs.reports.support.web.alerts.Alerts;


/**
 * Profil - edycja.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
@Controller
@SessionAttributes({ProfileEditForm.KEY})
@Scope("request")
public class ProfileEditController {
	@Autowired private Alerts alerts;
	
	@ModelAttribute(ProfileEditForm.KEY)
    public ProfileEditForm createForm() {
		ProfileEditForm form = new ProfileEditForm();
		return form;
    }		
	
	@RequestMapping(value="/profile/edit", method = RequestMethod.GET)
    public String get(Model model, @ModelAttribute(ProfileEditForm.KEY) final ProfileEditForm form) {
		return "profile/profile-edit";
    }
	
	@RequestMapping(value="/profile/edit/{id}", method = RequestMethod.GET)
    public String get(Model model, @ModelAttribute(ProfileEditForm.KEY) final ProfileEditForm form,  @PathVariable("id") Integer id) {
		return "profile/profile-edit";
    }	
	
	@RequestMapping(value= "/profile/edit", method = RequestMethod.POST)
    public String submit(@Valid @ModelAttribute(ProfileEditForm.KEY) final ProfileEditForm form, BindingResult results, HttpServletRequest request, RedirectAttributes ra) {
		if (!results.hasErrors()) {
			alerts.addSuccess(request, "profile.new.added");
		}
		
		return "profile/profile-edit";
		//return "redirect:/user/new"; 
	}
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		if (binder.getTarget() instanceof ProfileEditForm) binder.setValidator(new ProfileEditValidator());
	}
}
