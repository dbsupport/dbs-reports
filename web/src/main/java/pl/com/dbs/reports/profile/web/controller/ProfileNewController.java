/**
 * 
 */
package pl.com.dbs.reports.profile.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
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

import pl.com.dbs.reports.profile.domain.ProfileAddException;
import pl.com.dbs.reports.profile.service.ProfileService;
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
	private static final Logger logger = Logger.getLogger(ProfileNewController.class);
	@Autowired private Alerts alerts;
	@Autowired private MessageSource messageSource;
	@Autowired private ProfileService profileService;
	
	@ModelAttribute(ProfileNewForm.KEY)
    public ProfileNewForm createForm() {
		ProfileNewForm form = new ProfileNewForm();
		return form;
    }		
	
	@RequestMapping(value="/profile/new", method = RequestMethod.GET)
    public String init(Model model, @ModelAttribute(ProfileNewForm.KEY) final ProfileNewForm form) {
		form.reset();
		return "redirect:/profile/new/form";
    }
	
	@RequestMapping(value="/profile/new/form", method = RequestMethod.GET)
    public String form(Model model, @ModelAttribute(ProfileNewForm.KEY) final ProfileNewForm form) {
		return "profile/profile-new-form";
    }
	
	@RequestMapping(value="/profile/new/summary", method = RequestMethod.GET)
    public String summary(Model model, @ModelAttribute(ProfileNewForm.KEY) final ProfileNewForm form) {
		return "profile/profile-new-summary";
    }
	
	
	
	@RequestMapping(value= "/profile/new/form", method = RequestMethod.POST)
    public String form(@Valid @ModelAttribute(ProfileNewForm.KEY) final ProfileNewForm form, BindingResult results, HttpServletRequest request, RedirectAttributes ra) {
		if (!results.hasErrors()) {
			return "redirect:/profile/new/summary";
		}
		return "profile/profile-new-form";
	}
	
	@RequestMapping(value= "/profile/new/summary", method = RequestMethod.POST)
    public String summary(@Valid @ModelAttribute(ProfileNewForm.KEY) final ProfileNewForm form, BindingResult results, HttpServletRequest request, RedirectAttributes ra) {
		if (!results.hasErrors()) {
			try {
				profileService.add(form);
			} catch (Exception e) {
				exception(e, request, ra);
				return "redirect:/profile/new/summary";
			}
			
			alerts.addSuccess(ra, "profile.new.added", form.getFirstName()+" "+form.getLastName());
			return "redirect:/profile/list";
		}
		return "redirect:/profile/new/form";
	}
	
	private void exception(Exception e, HttpServletRequest request, RedirectAttributes ra) {
		if (e instanceof ProfileAddException) {
			String msg = e.getMessage();
			if (((ProfileAddException) e).getCode()!=null) {
				if (!((ProfileAddException) e).getParams().isEmpty()) 
					msg = messageSource.getMessage(((ProfileAddException) e).getCode(), ((ProfileAddException) e).getParams().toArray(), null);
				else msg = messageSource.getMessage(((ProfileAddException) e).getCode(), null, null);
				alerts.addError(ra, msg);
			}
		} else {
			alerts.addError(ra, "profile.add.unknown.error", e.getMessage());
			logger.error("profile.add.unknown.error:"+e.getMessage());
		}
	}	
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		if (binder.getTarget() instanceof ProfileNewForm) binder.setValidator(new ProfileNewValidator(profileService));
	}
}
