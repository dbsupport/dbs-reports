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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import pl.com.dbs.reports.report.pattern.domain.ReportPattern;
import pl.com.dbs.reports.report.pattern.service.PatternService;
import pl.com.dbs.reports.report.pattern.web.form.PatternImportForm;
import pl.com.dbs.reports.report.pattern.web.validator.PatternImportFormValidator;
import pl.com.dbs.reports.report.pattern.web.validator.PatternImportValidator;
import pl.com.dbs.reports.report.web.controller.ReportGenerationException;
import pl.com.dbs.reports.report.web.controller.ReportGenerationHelper;
import pl.com.dbs.reports.report.web.form.ReportGenerationForm;
import pl.com.dbs.reports.support.web.alerts.Alerts;
import pl.com.dbs.reports.support.web.file.FileMeta;


/**
 * Import report pattern.
 * 
 * http://www.beingjavaguys.com/2013/08/spring-mvc-file-upload-example.html
 * 
 * http://hmkcode.com/spring-mvc-jquery-file-upload-multiple-dragdrop-progress/
 * 
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
@Controller
@SessionAttributes({PatternImportForm.KEY})
public class PatternImportController {
	@Autowired private Alerts alerts;
	@Autowired private MessageSource messageSource;
	@Autowired private PatternService patternService;
	@Autowired private ReportGenerationHelper reportGenerationHelper;
	
	@ModelAttribute(PatternImportForm.KEY)
    public PatternImportForm createForm() {
		return new PatternImportForm();
    }
	
	@ModelAttribute(ReportGenerationForm.KEY)
    public ReportGenerationForm createForm(@ModelAttribute(PatternImportForm.KEY) final PatternImportForm form) {
		//..build test form only for form mapping..
		return reportGenerationHelper.constructMockForm(form.retrievePattern());
    }		
	
	@RequestMapping(value="/report/pattern/import", method = RequestMethod.GET)
    public String importe(Model model, SessionStatus sessionStatus) {
		sessionStatus.setComplete();
		return "redirect:/report/pattern/import/init";
    }
	
	@RequestMapping(value="/report/pattern/import/init", method = RequestMethod.GET)
    public String init(Model model, final @ModelAttribute(PatternImportForm.KEY) PatternImportForm form) {
		return "redirect:/report/pattern/import/read";
    }
	
	@RequestMapping(value="/report/pattern/import/read", method = RequestMethod.GET)
    public String read(Model model, final @ModelAttribute(PatternImportForm.KEY) PatternImportForm form, HttpServletRequest request) {
		return "report/pattern/pattern-import-read";
    }
	
	@RequestMapping(value= "/report/pattern/import/read", method = RequestMethod.POST)
    public String read(@Valid @ModelAttribute(PatternImportForm.KEY) final PatternImportForm form, 
    		BindingResult results, HttpServletRequest request, HttpSession session) {
		if (!results.hasErrors()) {
				try {
				ReportPattern pattern = patternService.read(FileMeta.multipartToFile(form.getFile()));
				form.reset(pattern);
				return "redirect:/report/pattern/import/summary";
			} catch (Throwable e) {
				reportGenerationHelper.exception(e, session);
			}
		}
		
		return "redirect:/report/pattern/import/read";
	}	
	
	
	@RequestMapping(value="/report/pattern/import/summary", method = RequestMethod.GET)
    public String summary(Model model, @ModelAttribute(PatternImportForm.KEY) PatternImportForm form, HttpServletRequest request) {
		model.addAttribute("pattern", form.retrievePattern());
		
		return "report/pattern/pattern-import-summary";
    }		
	
	@RequestMapping(value= "/report/pattern/import/summary", method = RequestMethod.POST)
    public String summary(@Valid @ModelAttribute(PatternImportForm.KEY) final PatternImportForm form, 
    		BindingResult results, HttpServletRequest request, HttpSession session) {
		if (!results.hasErrors()) {
			try {
				//..upload and save..
				ReportPattern pattern = patternService.upload(FileMeta.multipartToFile(form.getFile()));
				alerts.addSuccess(session, "report.pattern.import.file.success", pattern.getName(), pattern.getVersion(), pattern.getAccessesAsString());
				return "redirect:/report/pattern/list";
			} catch (Exception e) {
				reportGenerationHelper.exception(e, session);
			}
		}
		
		return "redirect:/report/pattern/import/summary";
	}	
	

	@RequestMapping(value="/report/pattern/import/form", method = RequestMethod.GET)
    public String form(Model model, 
    		@ModelAttribute(ReportGenerationForm.KEY) final ReportGenerationForm form, HttpServletRequest request) {
		return "report/pattern/pattern-import-form";
    }
	
	@RequestMapping(value= "/report/pattern/import/form", method = RequestMethod.POST)
    public String form(@Valid @ModelAttribute(ReportGenerationForm.KEY) final ReportGenerationForm form, 
    		BindingResult results, HttpSession session) {
		if (!results.hasErrors())
			alerts.addSuccess(session, "report.pattern.import.form.valid");
		
		return "redirect:/report/pattern/import/form";
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
		if (binder.getTarget() instanceof PatternImportForm) binder.setValidator(new PatternImportValidator());
		if (binder.getTarget() instanceof ReportGenerationForm) binder.setValidator(new PatternImportFormValidator());
	}
}
