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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import pl.com.dbs.reports.report.service.ReportService;
import pl.com.dbs.reports.report.web.form.ReportArchivesForm;
import pl.com.dbs.reports.report.web.validator.ReportArchivesValidator;
import pl.com.dbs.reports.support.web.alerts.Alerts;


/**
 * Lista wykonanych raportow dostepna dla danego profilu.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
@Controller
@SessionAttributes({ReportArchivesForm.KEY})
@Scope("request")
public class ReportArchivesController {
	@Autowired private Alerts alerts;
	@Autowired private ReportService reportService;
	
	@ModelAttribute(ReportArchivesForm.KEY)
    public ReportArchivesForm createForm() {
		ReportArchivesForm form = new ReportArchivesForm();
		return form;
    }
	
	@RequestMapping(value="/report/archives/{id}", method = RequestMethod.GET)
    public String get(Model model, @PathVariable("id") Long id, @ModelAttribute(ReportArchivesForm.KEY) final ReportArchivesForm form) {
		//todo
		return "redirect:/report/archives";
    }		
	
	@RequestMapping(value="/report/archives", method = RequestMethod.GET)
    public String get(Model model, @ModelAttribute(ReportArchivesForm.KEY) final ReportArchivesForm form) {
		model.addAttribute("reports", reportService.find(form.getFilter()));
		return "report/report-archives";
    }
	
	@RequestMapping(value= "/report/archives", method = RequestMethod.POST)
    public String submit(@Valid @ModelAttribute(ReportArchivesForm.KEY) final ReportArchivesForm form, BindingResult results, HttpServletRequest request, RedirectAttributes ra) {
		if (!results.hasErrors()) {

		}
		
		return "report/report-archives";
	}
	
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		if (binder.getTarget() instanceof ReportArchivesForm) binder.setValidator(new ReportArchivesValidator());
	}
}
