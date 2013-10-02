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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import pl.com.dbs.reports.report.api.Pattern;
import pl.com.dbs.reports.report.api.PatternManifestValidationException;
import pl.com.dbs.reports.report.application.PatternService;
import pl.com.dbs.reports.report.domain.ManifestNotFoundException;
import pl.com.dbs.reports.report.domain.PatternFactoryNotFoundException;
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
@Scope("request")
public class ReportPatternImportController {
	private static final Logger logger = Logger.getLogger(ReportPatternImportController.class);
	@Autowired private Alerts alerts;
	@Autowired private PatternService patternService;
	
//	@ModelAttribute(ReportPatternImportForm.KEY)
//    public ReportPatternImportForm createForm() {
//		ReportPatternImportForm form = new ReportPatternImportForm();
//		return form;
//    }		
	
	@RequestMapping(value="/report/pattern/import", method = RequestMethod.GET)
    public String get(Model model, @ModelAttribute(ReportPatternImportForm.KEY) ReportPatternImportForm form) {
		return "report/report-pattern-import";
    }
	
	@RequestMapping(value= "/report/pattern/import", method = RequestMethod.POST)
    public String submit(@Valid @ModelAttribute(ReportPatternImportForm.KEY) final ReportPatternImportForm form, 
    		BindingResult results, HttpServletRequest request, RedirectAttributes ra) {
		if (!results.hasErrors()) {
			try {
				Pattern pattern = patternService.upload(FileService.multipartToFile(form.getFile()));
				alerts.addSuccess(request, "report.import.file.success", pattern.getAttribute(Pattern.ATTRIBUTE_NAME), pattern.getAttribute(Pattern.ATTRIBUTE_VERSION), pattern.getAttribute(Pattern.ATTRIBUTE_ROLES));
			} catch (ManifestNotFoundException e) {
				alerts.addError(request, "report.import.manifest.error");
				logger.error("report.import.manifest.error"+e.getMessage());
			} catch (PatternFactoryNotFoundException e) {
				alerts.addError(request, "report.import.factory.error", e.getFactory());
				logger.error("report.import.factory.error"+e.getMessage());
			} catch (PatternManifestValidationException e) {
				alerts.addError(request, "report.manifest.validation.error", e.getAttr());
				logger.error("report.manifest.validation.error:"+e.getMessage());
			} catch (IOException e) {
				alerts.addError(request, "report.import.file.ioexception", e.getMessage());
				logger.error("report.import.file.ioexception:"+e.getMessage());
			}
			
//			try {
//				byte[] fileByteArray = form.getFile().getBytes();
//			} catch (IOException e) {}
//			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
//	        MultipartFile multipartFile = multipartRequest.getFile("filename");
//	        Files file = new Files();
//	        file.setFilename(multipartFile.getOriginalFilename());
//	        file.setNotes(ServletRequestUtils.getStringParameter(request, "notes"));
//	        file.setType(multipartFile.getContentType());
//	        file.setFile(multipartFile.getBytes());
		}
		
		return "report/report-pattern-import";
	}	
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		if (binder.getTarget() instanceof ReportPatternImportForm) binder.setValidator(new ReportPatternImportValidator());
	}
}
