/**
 * 
 */
package pl.com.dbs.reports.activedirectory.web.controller;

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

import pl.com.dbs.reports.activedirectory.service.ActiveDirectoryService;
import pl.com.dbs.reports.activedirectory.web.form.ActionDirectoryListAction;
import pl.com.dbs.reports.activedirectory.web.form.ActiveDirectoryListForm;
import pl.com.dbs.reports.activedirectory.web.validator.ActiveDirectoryValidator;
import pl.com.dbs.reports.support.web.alerts.Alerts;

/**
 * AD actions..
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2014
 */
@Controller
@SessionAttributes({ActiveDirectoryListForm.KEY})
@Scope("request")
public class ActiveDirectoryController {
	@Autowired private Alerts alerts;
	@Autowired private ActiveDirectoryService adService;

	
	@ModelAttribute(ActiveDirectoryListForm.KEY)
    public ActiveDirectoryListForm createProfileListForm() {
		ActiveDirectoryListForm form = new ActiveDirectoryListForm();
		return form;
    }

	@RequestMapping(value="/activedirectory/list/init", method = RequestMethod.GET)
    public String init(Model model, @ModelAttribute(ActiveDirectoryListForm.KEY) final ActiveDirectoryListForm form) {
		form.reset();
		return "redirect:/activedirectory/list";
	}
	
	@RequestMapping(value="/activedirectory/list", method = RequestMethod.GET)
    public String profiles(Model model, @ModelAttribute(ActiveDirectoryListForm.KEY) final ActiveDirectoryListForm form) {
		model.addAttribute("profiles", adService.find(form.getFilter()));
		return "activedirectory/activedirectory-list";
    }	
	
	@RequestMapping(value= "/activedirectory/list", method = RequestMethod.POST)
    public String profiles(Model model, @Valid @ModelAttribute(ActiveDirectoryListForm.KEY) final ActiveDirectoryListForm form, BindingResult results, HttpServletRequest request, RedirectAttributes ra) {
		if (results.hasErrors()) {
			model.addAttribute("profiles", adService.find(form.getFilter()));
			return "activedirectory/activedirectory-list";
		}
		
		if (ActionDirectoryListAction.INSERT.equals(form.getAction())) {
			try {
				adService.update(form.getId(), form.getDate());
				alerts.addSuccess(ra, 
						form.getId().size()==1?"activedirectory.profile.updated":"activedirectory.profiles.updated",
						form.getDateFormated(),
						String.valueOf(form.getId().size()));
			} catch (Exception e) {
				alerts.addError(ra, "activedirectory.profiles.not.updated", e.getMessage());
			}
		}
		return "redirect:/activedirectory/list";
	}
	
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		if (binder.getTarget() instanceof ActiveDirectoryListForm) binder.setValidator(new ActiveDirectoryValidator());
	}	
	
}