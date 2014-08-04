/**
 * 
 */
package pl.com.dbs.reports.access.web.controller;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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

import pl.com.dbs.reports.access.domain.Access;
import pl.com.dbs.reports.access.service.AccessService;
import pl.com.dbs.reports.access.web.form.AccessEditForm;
import pl.com.dbs.reports.access.web.form.AccessListForm;
import pl.com.dbs.reports.access.web.form.AccessNewForm;
import pl.com.dbs.reports.access.web.validator.AccessEditValidator;
import pl.com.dbs.reports.access.web.validator.AccessListValidator;
import pl.com.dbs.reports.access.web.validator.AccessNewValidator;
import pl.com.dbs.reports.report.pattern.dao.PatternFilter;
import pl.com.dbs.reports.report.pattern.service.PatternService;
import pl.com.dbs.reports.support.web.alerts.Alerts;


/**
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
@Controller
@SessionAttributes({AccessListForm.KEY, AccessNewForm.KEY, AccessEditForm.KEY})
public class AccessController {
	private static final Logger logger = Logger.getLogger(AccessController.class);
	
	@Autowired private Alerts alerts;
	@Autowired private AccessService accessService;
	@Autowired private PatternService patternService;

	@ModelAttribute(AccessListForm.KEY)
    public AccessListForm createListForm() {
		AccessListForm form = new AccessListForm();
		return form;
    }		
	
	@ModelAttribute(AccessNewForm.KEY)
    public AccessNewForm createNewForm() {
		AccessNewForm form = new AccessNewForm();
		return form;
    }	
	
	@ModelAttribute(AccessEditForm.KEY)
    public AccessEditForm createEditForm() {
		AccessEditForm form = new AccessEditForm();
		return form;
    }		
	
	
	@RequestMapping(value="/access/list", method = RequestMethod.GET)
    public String accesses(Model model, @ModelAttribute(AccessListForm.KEY) final AccessListForm form) {
		model.addAttribute("accesses", accessService.find(form.getFilter()));
		return "access/access-list";
    }		
	
	@RequestMapping(value="/access/new", method = RequestMethod.GET)
    public String add(Model model, @ModelAttribute(AccessNewForm.KEY) final AccessNewForm form) {
		form.reset();
		return "access/access-new";
    }
	
	@RequestMapping(value="/access/edit", method = RequestMethod.GET)
    public String edit(Model model, @ModelAttribute(AccessEditForm.KEY) final AccessEditForm form, HttpSession session) {
		if (form.getId()==null) {
			alerts.addError(session, "access.edit.wrong.id");
			return "redirect:/access/list";
		}
		
		Access access = accessService.find(form.getId());
		//..is there any ACTIVE pattern with this access?
		PatternFilter filter = new PatternFilter(access);
		int count = patternService.find(filter).size();
		if (count>0) alerts.addWarning(session, "access.edit.access.in.use", access.getName(), String.valueOf(count));
		
		return "access/access-edit";
	}

	
	@RequestMapping(value="/access/edit/{id}", method = RequestMethod.GET)
    public String edit(Model model, @PathVariable("id") Long id, @ModelAttribute(AccessEditForm.KEY) final AccessEditForm form, HttpSession session) {
		Access access = accessService.find(id);
		if (access==null) {
			alerts.addError(session, "access.edit.wrong.id");
			return "redirect:/access/list";
		}
		form.reset(access);
		return "redirect:/access/edit";
    }
	
	@RequestMapping(value="/access/delete/{id}", method = RequestMethod.GET)
    public String delete(Model model, @PathVariable("id") Long id, HttpSession session) {
		Access access = accessService.find(id);
		if (access==null) {
			alerts.addError(session, "access.edit.wrong.id");
			return "redirect:/access/list";
		}
		//..is there any ACTIVE pattern with this access?
		PatternFilter filter = new PatternFilter(access);
		int count = patternService.find(filter).size();
		if (count>0) {
			alerts.addError(session, "access.delete.access.in.use", access.getName(), String.valueOf(count));	
			return "redirect:/access/list";
		}
		
		//..delete..
		try {
			accessService.delete(id);
			alerts.addSuccess(session, "access.delete.deleted", access.getName());
		} catch (Exception e) {
			alerts.addError(session, "access.delete.error", access.getName(), e.getMessage());
			logger.error("access.delete.error:"+e.getMessage());					
		}
		
		return "redirect:/access/list";
    }
	

	@RequestMapping(value= "/access/list", method = RequestMethod.POST)
    public String accesses(@Valid @ModelAttribute(AccessListForm.KEY) final AccessListForm form, BindingResult results) {
		return "redirect:/access/list";
	}
	
	
	@RequestMapping(value= "/access/new", method = RequestMethod.POST)
    public String add(@Valid @ModelAttribute(AccessNewForm.KEY) final AccessNewForm form, BindingResult results, HttpSession session) {
		if (results.hasErrors()) return "access/access-new";
		
		Access access = accessService.add(form);
		try {
			alerts.addSuccess(session, "access.new.added", access.getName());
		} catch (Exception e) {
			alerts.addError(session, "access.new.error", access.getName(), e.getMessage());
			logger.error("access.new.error:"+e.getMessage());			
		}
		return "redirect:/access/list";
	}
	
	@RequestMapping(value="/access/edit", method = RequestMethod.POST)
    public String edit(@Valid @ModelAttribute(AccessEditForm.KEY) final AccessEditForm form,  BindingResult results, 
    		HttpSession session) {
		if (results.hasErrors()) return "access/access-edit";
		
		try {
			Access access = accessService.find(form.getId());
			accessService.edit(form);
			alerts.addSuccess(session, "access.edit.edited", access.getName());
		} catch (Exception e) {
			alerts.addError(session, "access.edit.error", e.getMessage());
			logger.error("access.edit.error:"+e.getMessage());			
		}		
		return "redirect:/access/list";
    }
	
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		if (binder.getTarget() instanceof AccessEditForm) binder.setValidator(new AccessEditValidator());
		else if (binder.getTarget() instanceof AccessNewForm) binder.setValidator(new AccessNewValidator());
		else if (binder.getTarget() instanceof AccessListForm) binder.setValidator(new AccessListValidator());
	}
}
