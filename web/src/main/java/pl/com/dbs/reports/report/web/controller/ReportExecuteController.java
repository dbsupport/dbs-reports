/**
 * 
 */
package pl.com.dbs.reports.report.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.log4j.Logger;
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

import pl.com.dbs.reports.report.pattern.service.PatternService;
import pl.com.dbs.reports.report.service.ReportService;
import pl.com.dbs.reports.report.web.form.ReportExecuteForm;
import pl.com.dbs.reports.report.web.validator.ReportExecuteValidator;
import pl.com.dbs.reports.support.web.alerts.Alerts;


/**
 * TODO
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
@SessionAttributes({ReportExecuteForm.KEY})
@Scope("request")
public class ReportExecuteController {
	private static final Logger logger = Logger.getLogger(ReportExecuteController.class);
	@Autowired private Alerts alerts;
	@Autowired private PatternService patternService;
	@Autowired private ReportService reportService;
	
	@ModelAttribute(ReportExecuteForm.KEY)
    public ReportExecuteForm createForm() {
		return new ReportExecuteForm();
    }		
	
	@RequestMapping(value="/report/execute/{id}", method = RequestMethod.GET)
    public String init(Model model, @ModelAttribute(ReportExecuteForm.KEY) ReportExecuteForm form, @PathVariable("id") Integer id, HttpServletRequest request) {
		form.reset(patternService.find(id));
		
		return "redirect:/report/execute/form";
    }
	
	@RequestMapping(value="/report/execute/form", method = RequestMethod.GET)
    public String form(Model model, @Valid @ModelAttribute(ReportExecuteForm.KEY) ReportExecuteForm form, 
    		BindingResult results, HttpServletRequest request, RedirectAttributes ra) {
		if (results.hasErrors()) {
			
		}
		return "/report/report-execute-form";
    }
	
	
	@RequestMapping(value= "/report/execute/form", method = RequestMethod.POST)
    public String read(@Valid @ModelAttribute(ReportExecuteForm.KEY) final ReportExecuteForm form, 
    		BindingResult results, HttpServletRequest request, RedirectAttributes ra) {
		if (!results.hasErrors()) {
			try {
				return "redirect:/report/execute/generate";
			} catch (Exception e) {
				//exception(e, request, ra);
			}
		}
		
		return "/report/report-execute-form";
	}
	
	@RequestMapping(value="/report/execute/generate", method = RequestMethod.GET)
    public String generate(Model model, @Valid @ModelAttribute(ReportExecuteForm.KEY) ReportExecuteForm form, 
    		BindingResult results, HttpServletRequest request, RedirectAttributes ra) {
		if (results.hasErrors()) {
			
		}
		return "/report/report-execute-generate";
    }
	
	@RequestMapping(value= "/report/execute/generate", method = RequestMethod.POST)
    public String generate(@Valid @ModelAttribute(ReportExecuteForm.KEY) final ReportExecuteForm form, 
    		BindingResult results, HttpServletRequest request, RedirectAttributes ra) {
		if (!results.hasErrors()) {
			try {
				reportService.generate(form);
				return "redirect:/report/execute/summary";
			} catch (Exception e) {
				//exception(e, request, ra);
			}
		}
		
		return "/report/report-execute-generate";
	}	
	
	@RequestMapping(value="/report/execute/summary", method = RequestMethod.GET)
    public String summary(Model model, @Valid @ModelAttribute(ReportExecuteForm.KEY) ReportExecuteForm form, 
    		BindingResult results, HttpServletRequest request, RedirectAttributes ra) {
		if (results.hasErrors()) {
			
		}
		return "/report/report-execute-summary";
    }
	
	@RequestMapping(value= "/report/execute/summary", method = RequestMethod.POST)
    public String summary(@Valid @ModelAttribute(ReportExecuteForm.KEY) final ReportExecuteForm form, 
    		BindingResult results, HttpServletRequest request, RedirectAttributes ra) {
		if (!results.hasErrors()) {
			try {
				return "redirect:/report/execute/"+form.getPattern().getId();
			} catch (Exception e) {
				//exception(e, request, ra);
			}
		}
		
		return "/report/report-execute-summary";
	}		
	
//	private void exception(Exception e, HttpServletRequest request, RedirectAttributes ra) {
//		if (e instanceof PatternManifestNotFoundException) {
//			alerts.addError(request, "report.import.manifest.error");
//			logger.error("report.import.manifest.error:"+e.getMessage());
//		} else if (e instanceof PatternFactoryNotFoundException) {
//			alerts.addError(request, "report.import.factory.error", ((PatternFactoryNotFoundException)e).getFactory());
//			logger.error("report.import.factory.error:"+e.getMessage());
//		} else if (e instanceof PatternFactoryProduceException) {
//		} else if (e instanceof PatternValidationException) {
//			alerts.addError(request, "report.manifest.validation.error", ((PatternValidationException)e).getAttr());
//			logger.error("report.manifest.validation.error:"+e.getMessage());
//		} else if (e instanceof IOException) {
//			alerts.addError(request, "report.import.file.ioexception", e.getMessage());
//			logger.error("report.import.file.ioexception:"+e.getMessage());
//		} else {
//			alerts.addError(request, "report.import.file.unknown", e.getMessage());
//			logger.error("report.import.file.unknown:"+e.getMessage());
//		}
//	}
	
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		if (binder.getTarget() instanceof ReportExecuteForm) binder.setValidator(new ReportExecuteValidator());
	}
}
