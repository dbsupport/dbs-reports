/**
 * 
 */
package pl.com.dbs.reports.user.web.controller;

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

import pl.com.dbs.reports.support.web.alerts.Alerts;
import pl.com.dbs.reports.user.web.form.UserNewForm;
import pl.com.dbs.reports.user.web.validator.UserNewValidator;

/**
 * Obsluga uzytkownikow
 * 
 * @author krzysztof.kaziura@gmail.com
 */
@Controller
@SessionAttributes({UserNewForm.KEY})
@Scope("request")
public class UserNewController {
	@Autowired private Alerts alerts;
	
	@ModelAttribute(UserNewForm.KEY)
    public UserNewForm createForm() {
		UserNewForm form = new UserNewForm();
		return form;
    }		
	
	@RequestMapping(value="/user/new", method = RequestMethod.GET)
    public String userNew(Model model, @ModelAttribute(UserNewForm.KEY) final UserNewForm form) {
		return "user/new";
    }
	
	@RequestMapping(value= "/user/new", method = RequestMethod.POST)
    public String userNewSubmit(@Valid @ModelAttribute(UserNewForm.KEY) final UserNewForm form, BindingResult results, HttpServletRequest request, RedirectAttributes ra) {
		if (!results.hasErrors()) {
			alerts.addSuccess(request, "user.new.profile.added");
		}
		
		return "user/new";
		//return "redirect:/user/new"; 
	}
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		if (binder.getTarget() instanceof UserNewForm) binder.setValidator(new UserNewValidator());
	}
}
