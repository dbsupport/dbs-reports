/**
 * 
 */
package pl.com.dbs.reports.report.pattern.web.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.xml.bind.JAXBException;

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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import pl.com.dbs.reports.api.report.pattern.PatternFactoryNotFoundException;
import pl.com.dbs.reports.api.report.pattern.PatternValidationException;
import pl.com.dbs.reports.report.pattern.domain.ReportPattern;
import pl.com.dbs.reports.report.pattern.domain.ReportPatternForm;
import pl.com.dbs.reports.report.pattern.service.PatternService;
import pl.com.dbs.reports.report.web.form.ReportGenerationForm;
import pl.com.dbs.reports.report.web.validator.ReportGenerationValidator;
import pl.com.dbs.reports.support.web.alerts.Alerts;
import pl.com.dbs.reports.support.web.form.DFormBuilder;


/**
 * Lista dostepnych raportow dla profilu.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
@Controller
@SessionAttributes({ReportGenerationForm.KEY})
@Scope("request")
public class PatternController {
	private static final Logger logger = Logger.getLogger(PatternController.class);
	@Autowired private Alerts alerts;
	@Autowired private PatternService patternService;
	@Autowired private MessageSource messageSource;
	
	@ModelAttribute(ReportGenerationForm.KEY)
    public ReportGenerationForm createForm(@PathVariable("id") Long id, HttpServletRequest request, RedirectAttributes ra) {
		//..build test form only for form mapping..
		ReportPattern pattern = patternService.find(id);
		try {
			ReportPatternForm rpf = pattern.getForm();
			DFormBuilder<ReportGenerationForm> builder = new DFormBuilder<ReportGenerationForm>(rpf.getContent(), ReportGenerationForm.class);
			ReportGenerationForm tform = builder.build().getForm();
			tform.reset(pattern);
			return tform;
		} catch (Exception e) {
			exception(e, request, ra);
		}
		return null;
    }		

	@RequestMapping(value="/report/pattern/details/{id}", method = RequestMethod.GET)
    public String details(Model model, @PathVariable("id") Long id, @ModelAttribute(ReportGenerationForm.KEY) final ReportGenerationForm form, HttpServletRequest request) {
		model.addAttribute("pattern", patternService.find(id));
		return "report/pattern/pattern-details";
    }
	
	@RequestMapping(value= "/report/pattern/details/{id}", method = RequestMethod.POST)
    public String form(Model model, @PathVariable("id") Long id, @Valid @ModelAttribute(ReportGenerationForm.KEY) final ReportGenerationForm form, 
    		BindingResult results, HttpServletRequest request, RedirectAttributes ra) {
		if (results.hasErrors()) {
			model.addAttribute("pattern", patternService.find(id));
			return "report/pattern/pattern-details";
		}
		
		alerts.addSuccess(ra, "report.pattern.import.form.valid");
		return "redirect:/report/pattern/details/"+id;
	}
	
	private void exception(Exception e, HttpServletRequest request, RedirectAttributes ra) {
		if (e instanceof PatternFactoryNotFoundException) {
			alerts.addError(request, "report.pattern.import.factory.error", ((PatternFactoryNotFoundException)e).getFactory());
			logger.error("report.pattern.import.factory.error:"+e.getMessage());
		} else if (e instanceof IOException) {
			alerts.addError(request, "report.pattern.import.file.ioexception", e.getMessage());
			logger.error("report.pattern.import.file.ioexception"+e.getMessage());
		} else if (e instanceof PatternValidationException) {
			String msg = e.getMessage();
			if (((PatternValidationException) e).getCode()!=null) {
				if (!((PatternValidationException) e).getParams().isEmpty()) 
					msg = messageSource.getMessage(((PatternValidationException) e).getCode(), ((PatternValidationException) e).getParams().toArray(), null);
				else msg = messageSource.getMessage(((PatternValidationException) e).getCode(), null, null);
				
				alerts.addError(request, msg);
				logger.error(msg);
			}
		} else if (e instanceof JAXBException) {
				alerts.addError(request, "report.execute.jaxbexception", ((JAXBException)e).getLinkedException().getMessage());
				logger.error("report.execute.jaxbexception:"+((JAXBException)e).getLinkedException().getMessage());			
		} else {
			alerts.addError(request, "report.pattern.download.error", e.getMessage());
			logger.error("report.pattern.download.error:"+e.getMessage());
		}
	}		
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		if (binder.getTarget() instanceof ReportGenerationForm) binder.setValidator(new ReportGenerationValidator());
	}
}
