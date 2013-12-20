/**
 * 
 */
package pl.com.dbs.reports.report.pattern.web.controller;

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

import pl.com.dbs.reports.report.pattern.domain.ReportPattern;
import pl.com.dbs.reports.report.pattern.service.PatternService;
import pl.com.dbs.reports.report.pattern.web.form.PatternListForm;
import pl.com.dbs.reports.report.pattern.web.validator.PatternListValidator;
import pl.com.dbs.reports.security.domain.SessionContext;
import pl.com.dbs.reports.support.web.alerts.Alerts;


/**
 * Lista dostepnych raportow dla profilu.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
@Controller
@SessionAttributes({PatternListForm.KEY})
@Scope("request")
public class PatternsController {
	@Autowired private Alerts alerts;
	@Autowired private PatternService patternService;
	
	@ModelAttribute(PatternListForm.KEY)
    public PatternListForm createForm() {
		PatternListForm form = new PatternListForm();
		return form;
    }		

	@RequestMapping(value="/report/pattern/list/init", method = RequestMethod.GET)
    public String initpatterns(Model model, @ModelAttribute(PatternListForm.KEY) final PatternListForm form, HttpServletRequest request) {
		form.reset();
		return "redirect:/report/pattern/list";
    }	
	
	@RequestMapping(value="/report/pattern/list", method = RequestMethod.GET)
    public String patterns(Model model, @ModelAttribute(PatternListForm.KEY) final PatternListForm form, HttpServletRequest request) {
		model.addAttribute("patterns", patternService.find(form.getFilter()));
		return "report/pattern/pattern-list";
    }
	
	@RequestMapping(value="/report/pattern/list/current", method = RequestMethod.GET)
    public String mypatterns(Model model, @ModelAttribute(PatternListForm.KEY) final PatternListForm form, HttpServletRequest request) {
		form.reset(SessionContext.getProfile().getName());
		return "redirect:/report/pattern/list";
    }
	
	
	@RequestMapping(value= "/report/pattern/list", method = RequestMethod.POST)
    public String patterns(Model model, @Valid @ModelAttribute(PatternListForm.KEY) final PatternListForm form, BindingResult results, HttpServletRequest request, RedirectAttributes ra) {
		return "redirect:/report/pattern/list";
	}	
	
	@RequestMapping(value="/report/pattern/delete/{id}", method = RequestMethod.GET)
    public String delete(Model model, @PathVariable("id") Long id, RedirectAttributes ra) {
		ReportPattern pattern = patternService.find(id);
		if (pattern==null) {
			alerts.addError(ra, "report.pattern.wrong.id");
			return "redirect:/report/pattern/list";
		}
		
		if (!pattern.isAccessible()) {
			alerts.addError(ra, "report.pattern.not.accessible");
			return "redirect:/report/pattern/list";
		}

		patternService.delete(id);
		alerts.addSuccess(ra, "report.pattern.deleted", pattern.getName(), pattern.getVersion(), pattern.getAccessesAsString());
		
		return "redirect:/report/pattern/list";
    }
	
	@RequestMapping(value="/report/pattern/details/{id}", method = RequestMethod.GET)
    public String details(Model model, @PathVariable("id") Long id, HttpServletRequest request) {
		model.addAttribute("pattern", patternService.find(id));
		return "report/pattern/pattern-details";
    }	
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		if (binder.getTarget() instanceof PatternListForm) binder.setValidator(new PatternListValidator());
	}
}
