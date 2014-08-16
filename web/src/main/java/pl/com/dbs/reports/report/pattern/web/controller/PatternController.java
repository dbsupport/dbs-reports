/**
 * 
 */
package pl.com.dbs.reports.report.pattern.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import pl.com.dbs.reports.report.pattern.service.PatternService;
import pl.com.dbs.reports.report.web.controller.ReportGenerationException;
import pl.com.dbs.reports.report.web.controller.ReportGenerationHandler;
import pl.com.dbs.reports.report.web.controller.ReportGenerationHelper;
import pl.com.dbs.reports.report.web.form.ReportGenerationForm;
import pl.com.dbs.reports.report.web.validator.ReportGenerationValidator;
import pl.com.dbs.reports.support.web.alerts.Alerts;


/**
 * Lista dostepnych raportow dla profilu.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
@Controller
@SessionAttributes({ReportGenerationForm.KEY})
public class PatternController {
	@Autowired private Alerts alerts;
	@Autowired private PatternService patternService;
	@Autowired private MessageSource messageSource;
	@Autowired private ReportGenerationHelper reportGenerationHelper;
	@Autowired private ReportGenerationHandler reportGenerationHandler;
	
	@ModelAttribute(ReportGenerationForm.KEY)
    public ReportGenerationForm createForm(@PathVariable("id") Long id) {
		//..build form..
		return reportGenerationHelper.constructFullForm(id);
    }		

	@RequestMapping(value="/report/pattern/details/{id}", method = RequestMethod.GET)
    public String details(Model model, @PathVariable("id") Long id, SessionStatus sessionStatus) {
		//..end this session to create NEW form..
		sessionStatus.setComplete();
		return "redirect:/report/pattern/details/init/"+id;
    }
	
	@RequestMapping(value="/report/pattern/details/init/{id}", method = RequestMethod.GET)
    public String init(Model model, @PathVariable("id") Long id, @ModelAttribute(ReportGenerationForm.KEY) final ReportGenerationForm form) {
		return "redirect:/report/pattern/details";
	}
	
	@RequestMapping(value="/report/pattern/details", method = RequestMethod.GET)
    public String details(Model model, @ModelAttribute(ReportGenerationForm.KEY) final ReportGenerationForm form, HttpServletRequest request) {
		model.addAttribute("pattern", patternService.find(form.getPattern()));
		return "report/pattern/pattern-details";
	}
	
	@RequestMapping(value= "/report/pattern/details", method = RequestMethod.POST)
    public String form(Model model, @Valid @ModelAttribute(ReportGenerationForm.KEY) final ReportGenerationForm form, 
    		BindingResult results, HttpSession session) {
		if (!results.hasErrors()) {
			alerts.addSuccess(session, "report.pattern.import.form.valid");
		}
		
		return "redirect:/report/pattern/details";
	}
	
	/**
	 * 
	 * overwrite..
	 */
	@ExceptionHandler(ReportGenerationException.class)
	private void exception(Exception e, HttpSession session) {
		reportGenerationHelper.exception(e, session);
	}		
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		if (binder.getTarget() instanceof ReportGenerationForm) binder.setValidator(new ReportGenerationValidator());
	}
}
