/**
 * 
 */
package pl.com.dbs.reports.report.web.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

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
import pl.com.dbs.reports.report.pattern.service.PatternService;
import pl.com.dbs.reports.report.web.form.ReportPatternImportForm;
import pl.com.dbs.reports.report.web.validator.ReportPatternImportValidator;
import pl.com.dbs.reports.support.web.alerts.Alerts;
import pl.com.dbs.reports.support.web.file.FileService;


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
@SessionAttributes({ReportPatternImportForm.KEY})
@Scope("request")
public class ReportPatternImportController {
	private static final Logger logger = Logger.getLogger(ReportPatternImportController.class);
	@Autowired private Alerts alerts;
	@Autowired private MessageSource messageSource;
	@Autowired private PatternService patternService;
	
	@ModelAttribute(ReportPatternImportForm.KEY)
    public ReportPatternImportForm createForm() {
		return new ReportPatternImportForm();
    }		
	
	@RequestMapping(value="/report/pattern/import", method = RequestMethod.GET)
    public String init(Model model, @ModelAttribute(ReportPatternImportForm.KEY) ReportPatternImportForm form, RedirectAttributes ra) {
		form.reset();
		return "redirect:/report/pattern/import/read";
    }
	
	@RequestMapping(value="/report/pattern/import/read", method = RequestMethod.GET)
    public String read(Model model, @ModelAttribute(ReportPatternImportForm.KEY) ReportPatternImportForm form, HttpServletRequest request) {
		return "report/report-pattern-import-read";
    }
	
	@RequestMapping(value="/report/pattern/import/write", method = RequestMethod.GET)
    public String write(Model model, @ModelAttribute(ReportPatternImportForm.KEY) ReportPatternImportForm form, HttpServletRequest request) {
		if (form.getFile()==null||form.getPattern()==null) return "redirect:/report/pattern/import";
		return "report/report-pattern-import-write";
    }		
	
	@RequestMapping(value= "/report/pattern/import/read", method = RequestMethod.POST)
    public String read(@Valid @ModelAttribute(ReportPatternImportForm.KEY) final ReportPatternImportForm form, 
    		BindingResult results, HttpServletRequest request, RedirectAttributes ra) {
		if (!results.hasErrors()) {
			try {
				ReportPattern pattern = patternService.read(FileService.multipartToFile(form.getFile()));
				form.setup(pattern);
				return "redirect:/report/pattern/import/write";
			} catch (Exception e) {
				exception(e, request, ra);
			}
		}
		
		return "report/report-pattern-import-read";
	}
	
	@RequestMapping(value= "/report/pattern/import/write", method = RequestMethod.POST)
    public String write(@Valid @ModelAttribute(ReportPatternImportForm.KEY) final ReportPatternImportForm form, 
    		BindingResult results, HttpServletRequest request, RedirectAttributes ra) {
		if (!results.hasErrors()) {
			try {
				ReportPattern pattern = patternService.upload(FileService.multipartToFile(form.getFile()));
				alerts.addSuccess(ra, "report.import.file.success", pattern.getName(), pattern.getVersion(), pattern.getAccessesAsString());
				form.reset();
				return "redirect:/report/pattern/list";
			} catch (Exception e) {
				exception(e, request, ra);
			}
		}
		
		return "report/report-pattern-import-write";
	}
	
	private void exception(Exception e, HttpServletRequest request, RedirectAttributes ra) {
		if (e instanceof PatternFactoryNotFoundException) {
			alerts.addError(request, "report.import.factory.error", ((PatternFactoryNotFoundException)e).getFactory());
			logger.error("report.import.factory.error:"+e.getMessage());
		} else if (e instanceof IOException) {
			alerts.addError(request, "report.import.file.ioexception", e.getMessage());
			logger.error("report.import.file.ioexception"+e.getMessage());
		} else if (e instanceof PatternValidationException) {
			String msg = e.getMessage();
			if (((PatternValidationException) e).getCode()!=null) {
				if (!((PatternValidationException) e).getParams().isEmpty()) 
					msg = messageSource.getMessage(((PatternValidationException) e).getCode(), ((PatternValidationException) e).getParams().toArray(), null);
				else msg = messageSource.getMessage(((PatternValidationException) e).getCode(), null, null);
				
				alerts.addError(request, msg);
				logger.error(msg);
			}
		} else {
			alerts.addError(request, "report.import.file.unknown", e.getMessage());
			logger.error("report.import.file.unknown:"+e.getMessage());
		}
	}
	
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		if (binder.getTarget() instanceof ReportPatternImportForm) binder.setValidator(new ReportPatternImportValidator());
	}
}
