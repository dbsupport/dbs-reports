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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import pl.com.dbs.reports.api.report.pattern.PatternFactoryNotFoundException;
import pl.com.dbs.reports.api.report.pattern.PatternValidationException;
import pl.com.dbs.reports.report.pattern.domain.ReportPattern;
import pl.com.dbs.reports.report.pattern.domain.ReportPatternForm;
import pl.com.dbs.reports.report.pattern.service.PatternService;
import pl.com.dbs.reports.report.pattern.web.form.PatternImportForm;
import pl.com.dbs.reports.report.pattern.web.validator.PatternImportValidator;
import pl.com.dbs.reports.report.web.form.ReportExecuteForm;
import pl.com.dbs.reports.report.web.validator.ReportExecuteValidator;
import pl.com.dbs.reports.support.web.alerts.Alerts;
import pl.com.dbs.reports.support.web.file.FileMeta;
import pl.com.dbs.reports.support.web.form.DFormBuilder;


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
@Scope("request")
public class PatternImportController {
	private static final Logger logger = Logger.getLogger(PatternImportController.class);
	@Autowired private Alerts alerts;
	@Autowired private MessageSource messageSource;
	@Autowired private PatternService patternService;
	
	@ModelAttribute(PatternImportForm.KEY)
    public PatternImportForm createForm() {
		return new PatternImportForm();
    }
	
	@ModelAttribute(ReportExecuteForm.KEY)
    public ReportExecuteForm createForm(@ModelAttribute(PatternImportForm.KEY) final PatternImportForm form, HttpServletRequest request, RedirectAttributes ra) {
		//..build test form only for form mapping..
		if (form.getPattern()!=null&&form.getPattern().getForm()!=null) {//&&request.getServletPath().contains("/report/pattern/import/form")) {
			try {
				ReportPatternForm rpf = form.getPattern().getForm();
				DFormBuilder<ReportExecuteForm> builder = new DFormBuilder<ReportExecuteForm>(rpf.getContent(), ReportExecuteForm.class);
				ReportExecuteForm tform = builder.build().getForm();
				tform.reset(form.getPattern());
				return tform;
			} catch (Exception e) {
				exception(e, request, ra);
			}
		}
		return null;
    }		
	
	@RequestMapping(value="/report/pattern/import", method = RequestMethod.GET)
    public String init(Model model, @ModelAttribute(PatternImportForm.KEY) PatternImportForm form, RedirectAttributes ra) {
		form.reset();
		return "redirect:/report/pattern/import/read";
    }
	
	@RequestMapping(value="/report/pattern/import/read", method = RequestMethod.GET)
    public String read(Model model, @ModelAttribute(PatternImportForm.KEY) PatternImportForm form, HttpServletRequest request) {
		return "report/pattern/pattern-import-read";
    }
	
	@RequestMapping(value="/report/pattern/import/summary", method = RequestMethod.GET)
    public String summary(Model model, @ModelAttribute(PatternImportForm.KEY) PatternImportForm form, HttpServletRequest request) {
		if (form.getFile()==null||form.getPattern()==null) return "redirect:/report/pattern/import";
		return "report/pattern/pattern-import-summary";
    }		

	@RequestMapping(value="/report/pattern/import/form", method = RequestMethod.GET)
    public String form(Model model, @ModelAttribute(ReportExecuteForm.KEY) final ReportExecuteForm form, HttpServletRequest request, RedirectAttributes ra) {
		return "report/pattern/pattern-import-form";
    }
	
	
	
	@RequestMapping(value= "/report/pattern/import/read", method = RequestMethod.POST)
    public String read(@Valid @ModelAttribute(PatternImportForm.KEY) final PatternImportForm form, 
    		BindingResult results, HttpServletRequest request, RedirectAttributes ra) {
		if (!results.hasErrors()) {
			try {
				ReportPattern pattern = patternService.read(FileMeta.multipartToFile(form.getFile()));
				form.setup(pattern);
				return "redirect:/report/pattern/import/summary";
			} catch (Exception e) {
				exception(e, request, ra);
			}
		}
		
		return "report/pattern/pattern-import-read";
	}

	@RequestMapping(value= "/report/pattern/import/summary", method = RequestMethod.POST)
    public String summary(@Valid @ModelAttribute(PatternImportForm.KEY) final PatternImportForm form, 
    		BindingResult results, HttpServletRequest request, RedirectAttributes ra) {
		if (!results.hasErrors()) {
			try {
				//..upload and save..
				ReportPattern pattern = patternService.upload(FileMeta.multipartToFile(form.getFile()));
				alerts.addSuccess(ra, "report.pattern.import.file.success", pattern.getName(), pattern.getVersion(), pattern.getAccessesAsString());
				form.reset();	
				return "redirect:/report/pattern/list";
			} catch (Exception e) {
				exception(e, request, ra);
			}
		}
		
		return "report/pattern/pattern-import-summary";
	}

	@RequestMapping(value= "/report/pattern/import/form", method = RequestMethod.POST)
    public String form(@Valid @ModelAttribute(ReportExecuteForm.KEY) final ReportExecuteForm form, 
    		BindingResult results, HttpServletRequest request, RedirectAttributes ra) {
		return "report/pattern/pattern-import-form";
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
			alerts.addError(request, "report.pattern.import.file.unknown", e.getMessage());
			logger.error("report.pattern.import.file.unknown:"+e.getMessage());
		}
	}
	
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		if (binder.getTarget() instanceof PatternImportForm) binder.setValidator(new PatternImportValidator());
		if (binder.getTarget() instanceof ReportExecuteForm) binder.setValidator(new ReportExecuteValidator());
	}
}
