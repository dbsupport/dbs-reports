/**
 * 
 */
package pl.com.dbs.reports.report.web.controller;

import java.io.IOException;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import pl.com.dbs.reports.api.inner.report.pattern.Pattern;
import pl.com.dbs.reports.api.inner.report.pattern.PatternValidationException;
import pl.com.dbs.reports.report.pattern.application.PatternService;
import pl.com.dbs.reports.report.pattern.domain.PatternFactoryNotFoundException;
import pl.com.dbs.reports.report.pattern.domain.PatternFactoryProduceException;
import pl.com.dbs.reports.report.pattern.domain.PatternManifestNotFoundException;
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
	@Autowired private PatternService patternService;
	
	@ModelAttribute(ReportPatternImportForm.KEY)
    public ReportPatternImportForm createForm() {
		return new ReportPatternImportForm();
    }		
	
	@RequestMapping(value="/report/pattern/import", method = RequestMethod.GET)
    public String init(Model model, @ModelAttribute(ReportPatternImportForm.KEY) ReportPatternImportForm form) {
		form.reset();
		return "redirect:/report/pattern/import/read";
    }
	
	@RequestMapping(value="/report/pattern/import/read", method = RequestMethod.GET)
    public String read(Model model, @ModelAttribute(ReportPatternImportForm.KEY) ReportPatternImportForm form, HttpServletRequest request) {
		form.setPage(1);
		return "report/report-pattern-import-01";
    }
	
	@RequestMapping(value="/report/pattern/import/write", method = RequestMethod.GET)
    public String write(Model model, @ModelAttribute(ReportPatternImportForm.KEY) ReportPatternImportForm form, HttpServletRequest request) {
		if (form.getFile()==null||form.getPattern()==null) return "redirect:/report/pattern/import";
		form.setPage(2);
		return "report/report-pattern-import-02";
    }		
	
	@RequestMapping(value= "/report/pattern/import/read", method = RequestMethod.POST)
    public String read(@Valid @ModelAttribute(ReportPatternImportForm.KEY) final ReportPatternImportForm form, 
    		BindingResult results, HttpServletRequest request, RedirectAttributes ra) {
		if (!results.hasErrors()) {
			try {
				form.setup(patternService.read(FileService.multipartToFile(form.getFile())));
				return "redirect:/report/pattern/import/write";
			} catch (Exception e) {
				exception(e, request, ra);
			}
		}
		
		return "report/report-pattern-import-01";
	}
	
	@RequestMapping(value= "/report/pattern/import/write", method = RequestMethod.POST)
    public String write(@Valid @ModelAttribute(ReportPatternImportForm.KEY) final ReportPatternImportForm form, 
    		BindingResult results, HttpServletRequest request, RedirectAttributes ra) {
		if (!results.hasErrors()) {
			try {
				Pattern pattern = patternService.upload(FileService.multipartToFile(form.getFile()));
				alerts.addSuccess(request, "report.import.file.success", pattern.getAttribute(Pattern.ATTRIBUTE_PATTERN_NAME), pattern.getAttribute(Pattern.ATTRIBUTE_PATTERN_VERSION), pattern.getAttribute(Pattern.ATTRIBUTE_ROLES));
				return "redirect:/report/pattern/import";
			} catch (Exception e) {
				exception(e, request, ra);
			}
		}
		
		return "report/report-pattern-import-02";
	}
	
	private void exception(Exception e, HttpServletRequest request, RedirectAttributes ra) {
		if (e instanceof PatternManifestNotFoundException) {
			alerts.addError(request, "report.import.manifest.error");
			logger.error("report.import.manifest.error:"+e.getMessage());
		} else if (e instanceof PatternFactoryNotFoundException) {
			alerts.addError(request, "report.import.factory.error", ((PatternFactoryNotFoundException)e).getFactory());
			logger.error("report.import.factory.error:"+e.getMessage());
		} else if (e instanceof PatternFactoryProduceException) {
			alerts.addError(request, "report.manifest.produce.error", ((PatternFactoryProduceException)e).getFactory().getName());
			logger.error("report.manifest.produce.error:"+e.getMessage());
		} else if (e instanceof PatternValidationException) {
			alerts.addError(request, "report.manifest.validation.error", ((PatternValidationException)e).getAttr());
			logger.error("report.manifest.validation.error:"+e.getMessage());
		} else if (e instanceof IOException) {
			alerts.addError(request, "report.import.file.ioexception", e.getMessage());
			logger.error("report.import.file.ioexception:"+e.getMessage());
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
