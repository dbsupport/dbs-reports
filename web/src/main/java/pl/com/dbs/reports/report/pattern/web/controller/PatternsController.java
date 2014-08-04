/**
 * 
 */
package pl.com.dbs.reports.report.pattern.web.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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

import pl.com.dbs.reports.report.pattern.domain.ReportPattern;
import pl.com.dbs.reports.report.pattern.service.PatternService;
import pl.com.dbs.reports.report.pattern.web.form.PatternListForm;
import pl.com.dbs.reports.report.pattern.web.validator.PatternListValidator;
import pl.com.dbs.reports.security.domain.SessionContext;
import pl.com.dbs.reports.support.web.alerts.Alerts;
import pl.com.dbs.reports.support.web.controller.DownloadController;


/**
 * Lista dostepnych raportow dla profilu.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
@Controller
@SessionAttributes({PatternListForm.KEY})
public class PatternsController {
	private static final Logger logger = Logger.getLogger(PatternsController.class);
	@Autowired private Alerts alerts;
	@Autowired private PatternService patternService;
	
	@ModelAttribute(PatternListForm.KEY)
    public PatternListForm createForm() {
		PatternListForm form = new PatternListForm();
		return form;
    }
	
	@RequestMapping(value="/report/pattern/list/init", method = RequestMethod.GET)
    public String initpatterns(Model model, @ModelAttribute(PatternListForm.KEY) final PatternListForm form, HttpServletRequest request) {
		form.reset();
		return "redirect:/report/pattern/list";
    }	
	
	@RequestMapping(value="/report/pattern/list", method = RequestMethod.GET)
    public String patterns(Model model, @Valid @ModelAttribute(PatternListForm.KEY) final PatternListForm form, HttpServletRequest request) {
		model.addAttribute("patterns", patternService.find(form.getFilter()));
		model.addAttribute("current", SessionContext.getProfile().getId().equals(form.getFilter().getProfileId()));
		return "report/pattern/pattern-list";
    }
	
	@RequestMapping(value="/report/pattern/list/current", method = RequestMethod.GET)
    public String mypatterns(Model model, @ModelAttribute(PatternListForm.KEY) final PatternListForm form, HttpServletRequest request) {
		form.reset(SessionContext.getProfile());
		return "redirect:/report/pattern/list";
    }
	
	
	@RequestMapping(value= "/report/pattern/list", method = RequestMethod.POST)
    public String patterns(Model model, @Valid @ModelAttribute(PatternListForm.KEY) final PatternListForm form, BindingResult results, HttpServletRequest request) {
		return "redirect:/report/pattern/list";
	}	
	
	@RequestMapping(value="/report/pattern/delete/{id}", method = RequestMethod.GET)
    public String delete(Model model, @PathVariable("id") Long id, HttpSession session) {
		ReportPattern pattern = patternService.find(id);
		if (pattern==null) {
			alerts.addError(session, "report.pattern.wrong.id");
			return "redirect:/report/pattern/list";
		}
		
		if (!pattern.isAccessible()) {
			alerts.addError(session, "report.pattern.not.accessible");
			return "redirect:/report/pattern/list";
		}

		patternService.delete(id);
		alerts.addSuccess(session, "report.pattern.deleted", pattern.getName(), pattern.getVersion(), pattern.getAccessesAsString());
		
		return "redirect:/report/pattern/list";
    }
	
	@RequestMapping(value="/report/pattern/download/{id}", method = RequestMethod.GET)
    public String download(Model model, @PathVariable("id") Long id, HttpSession session,  HttpServletRequest request, HttpServletResponse response) {
		if (id==null) {
			alerts.addError(session, "report.pattern.no.pattern");
			return "redirect:/report/pattern/list";
		}

		ReportPattern pattern = patternService.find(id);
		if (pattern==null) {
			alerts.addError(session, "report.pattern.no.pattern");
			return "redirect:/report/pattern/list";
		}
		
		try {
			DownloadController.download(pattern.getContent(), pattern.getFilename(), request, response);
		} catch (IOException e) {
			exception(e, session);
			return "redirect:/report/pattern/list";
		}
		
		return null;
    }	
	
	private void exception(Exception e, HttpSession session) {
		if (e instanceof IOException) {
			alerts.addError(session, "report.pattern.download.error", e.getMessage());
			logger.error("report.pattern.download.error:"+e.getMessage());
		} else {
			alerts.addError(session, "report.pattern.download.error", e.getMessage());
			logger.error("report.pattern.download.error:"+e.getMessage());
		}
	}		
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		if (binder.getTarget() instanceof PatternListForm) binder.setValidator(new PatternListValidator());
	}
}
