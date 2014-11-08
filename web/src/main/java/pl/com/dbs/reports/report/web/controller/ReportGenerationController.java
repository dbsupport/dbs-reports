/**
 * 
 */
package pl.com.dbs.reports.report.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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

import pl.com.dbs.reports.report.domain.ReportOrder;
import pl.com.dbs.reports.report.pattern.domain.ReportPattern;
import pl.com.dbs.reports.report.pattern.service.PatternService;
import pl.com.dbs.reports.report.service.ReportOrderService;
import pl.com.dbs.reports.report.service.ReportService;
import pl.com.dbs.reports.report.web.form.ReportGenerationForm;
import pl.com.dbs.reports.report.web.validator.ReportGenerationValidator;
import pl.com.dbs.reports.support.web.alerts.Alerts;


/**
 * Report generation.
 * 
 * http://www.beingjavaguys.com/2013/08/spring-mvc-file-upload-example.html
 * 
 * http://hmkcode.com/spring-mvc-jquery-file-upload-multiple-dragdrop-progress/
 * 
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2013
 */
@Controller
@SessionAttributes({ReportGenerationForm.KEY})
public class ReportGenerationController {
	@Autowired private Alerts alerts;
	@Autowired private PatternService patternService;
	@Autowired private ReportService reportService;
	@Autowired private ReportOrderService reportOrderService;
	@Autowired private ReportsUnarchivedHelper reportsUnarchivedHelper;
	@Autowired private ReportGenerationHelper reportGenerationHelper;
	
	@ModelAttribute(ReportGenerationForm.KEY)
    public ReportGenerationForm createForm(Model model, @PathVariable("id") Long id) {
		return reportGenerationHelper.constructFullForm(id);
    }		
	
	@RequestMapping(value="/report/execute/{id}", method = RequestMethod.GET)
    public String execute(Model model, @PathVariable("id") Long id, SessionStatus sessionStatus) {
		//..end this session to create NEW form..
		sessionStatus.setComplete();
		return "redirect:/report/execute/init/"+id;
    }
	
	@RequestMapping(value="/report/execute/init/{id}", method = RequestMethod.GET)
    public String init(Model model, @PathVariable("id") Long id, final @ModelAttribute(ReportGenerationForm.KEY) ReportGenerationForm form) {
		return "redirect:/report/execute/form";
	}	
	
	@RequestMapping(value="/report/execute/form", method = RequestMethod.GET)
    public String form(Model model, @ModelAttribute(ReportGenerationForm.KEY) ReportGenerationForm form, 
    		BindingResult results, HttpSession session) {
		//..if it was no able to create form redirect and show error.. 
		if (form==null) return "redirect:/report/pattern/list";
		
		warnAboutMaximum(session);
		model.addAttribute("pattern", patternService.find(form.getPattern()));
		
		return "/report/report-execute-form";
    }

	private void warnAboutMaximum(HttpSession session) {
		int count = reportService.countUnarchived();
		if (count>=ReportService.MAX_UNARCHIVED) {
			alerts.addWarning(session, "report.archive.maximum.exceeded", String.valueOf(count));
		}
	}
	
	
	@RequestMapping(value= "/report/execute/form", method = RequestMethod.POST)
    public String read(Model model, @Valid @ModelAttribute(ReportGenerationForm.KEY) final ReportGenerationForm form, 
    		BindingResult results, HttpServletRequest request) {
		if (!results.hasErrors()) {
			return "redirect:/report/execute/generate";
		}
		
		return "redirect:/report/execute/form";
	}
	
	@RequestMapping(value="/report/execute/generate", method = RequestMethod.GET)
    public String generate(Model model, @Valid @ModelAttribute(ReportGenerationForm.KEY) ReportGenerationForm form, 
    		HttpSession session) {
		
		warnAboutMaximum(session);
		model.addAttribute("pattern", patternService.find(form.getPattern()));
		
		return "/report/report-execute-generate";
    }
	
	@RequestMapping(value= "/report/execute/generate", method = RequestMethod.POST)
    public String generate(Model model, @Valid @ModelAttribute(ReportGenerationForm.KEY) final ReportGenerationForm form, 
    		BindingResult results, HttpSession session) {
		if (!results.hasErrors()) {
			try {
				ReportOrder order = reportService.order(form);
				ReportPattern pattern = patternService.find(form.getPattern());
				if (order.count()>1) {
					alerts.addSuccess(session, "report.execute.multi.success", String.valueOf(order.count()), pattern.getName(), pattern.getVersion(), order.getName());
				} else {
					alerts.addSuccess(session, "report.execute.single.success", pattern.getName(), pattern.getVersion(), order.getName());
				}
				return "redirect:/report/unarchived";
			} catch (Exception e) {
				reportGenerationHelper.exception(e, session);
			}
		}

		return "redirect:/report/execute/generate";
	}	
	
//	@RequestMapping(value="/report/execute/summary", method = RequestMethod.GET)
//    public String summary(Model model, @Valid @ModelAttribute(ReportGenerationForm.KEY) ReportGenerationForm form, 
//    		BindingResult results, HttpServletRequest request, RedirectAttributes ra) {
//		if (results.hasErrors()) {}
//		
//		model.addAttribute("pattern", patternService.find(form.getPattern()));
//		
//		reportsUnarchivedHelper.unarchivedLimited(model);
//		
//		return "/report/report-execute-summary";
//    }
//	
//	@RequestMapping(value= "/report/execute/summary", method = RequestMethod.POST)
//    public String summary(@Valid @ModelAttribute(ReportGenerationForm.KEY) final ReportGenerationForm form, 
//    		BindingResult results, HttpServletRequest request, RedirectAttributes ra) {
//		return "redirect:/report/execute/form";
//	}		
	
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
