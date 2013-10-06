/**
 * 
 */
package pl.com.dbs.reports.report.web.controller;

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

import pl.com.dbs.reports.report.pattern.application.PatternService;
import pl.com.dbs.reports.report.web.form.ReportPatternListForm;
import pl.com.dbs.reports.report.web.validator.ReportPatternListValidator;
import pl.com.dbs.reports.report.web.view.ReportPatternView;
import pl.com.dbs.reports.support.web.alerts.Alerts;


/**
 * Lista dostepnych raportow dla profilu.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
@Controller
@SessionAttributes({ReportPatternListForm.KEY})
@Scope("request")
public class ReportPatternListController {
	@Autowired private Alerts alerts;
	@Autowired private PatternService patternService;
	
	@ModelAttribute(ReportPatternListForm.KEY)
    public ReportPatternListForm createForm() {
		ReportPatternListForm form = new ReportPatternListForm();
		return form;
    }		
	
	@RequestMapping(value="/report/pattern/list", method = RequestMethod.GET)
    public String get(Model model, @ModelAttribute(ReportPatternListForm.KEY) final ReportPatternListForm form) {
		model.addAttribute("patterns", ReportPatternView.build(patternService.find(form.getFilter())));
		return "report/report-pattern-list";
    }
	
	@RequestMapping(value= "/report/pattern/list", method = RequestMethod.POST)
    public String submit(Model model, @Valid @ModelAttribute(ReportPatternListForm.KEY) final ReportPatternListForm form, BindingResult results, HttpServletRequest request, RedirectAttributes ra) {
		if (!results.hasErrors()) {
		}
		
		return "redirect:/report/pattern/list";
	}	
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		if (binder.getTarget() instanceof ReportPatternListForm) binder.setValidator(new ReportPatternListValidator());
	}
}
