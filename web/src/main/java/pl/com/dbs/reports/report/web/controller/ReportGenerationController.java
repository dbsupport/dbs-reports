/**
 * 
 */
package pl.com.dbs.reports.report.web.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.xml.bind.JAXBException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
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
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import pl.com.dbs.reports.api.report.Report;
import pl.com.dbs.reports.api.report.ReportValidationException;
import pl.com.dbs.reports.report.pattern.domain.ReportPattern;
import pl.com.dbs.reports.report.pattern.domain.ReportPatternForm;
import pl.com.dbs.reports.report.pattern.service.PatternService;
import pl.com.dbs.reports.report.service.ReportService;
import pl.com.dbs.reports.report.web.form.ReportGenerationForm;
import pl.com.dbs.reports.report.web.validator.ReportGenerationValidator;
import pl.com.dbs.reports.support.utils.exception.Exceptions;
import pl.com.dbs.reports.support.web.alerts.Alerts;
import pl.com.dbs.reports.support.web.form.DFormBuilder;


/**
 * Report generation.
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
@SessionAttributes({ReportGenerationForm.KEY})
@Scope("request")
public class ReportGenerationController {
	private static final Logger logger = Logger.getLogger(ReportGenerationController.class);
	@Autowired private Alerts alerts;
	@Autowired private PatternService patternService;
	@Autowired private ReportService reportService;
	
	@ModelAttribute(ReportGenerationForm.KEY)
    public ReportGenerationForm createForm(Model model, HttpServletRequest request, RedirectAttributes ra) {
		ReportGenerationForm result = null;
		
		Integer id = request.getSession().getAttribute("id")!=null?(Integer)request.getSession().getAttribute("id"):null;
		if (id==null) return result;
		request.getSession().removeAttribute("id");
		ReportPattern pattern = patternService.find(id);
		
		//FIXME: only one form allowed..
		ReportPatternForm form = pattern.getForm();
		if (form!=null) {
			try {
				DFormBuilder<ReportGenerationForm> builder = new DFormBuilder<ReportGenerationForm>(form.getContent(), ReportGenerationForm.class);
				
				result = builder.build().getForm();
				result.reset(pattern);
			} catch (JAXBException e) {
				result = null;
				//FIXME: not seen..
				alerts.addError(request, "report.execute.jaxbexception", ((JAXBException)e).getLinkedException().getMessage());
				logger.error("report.execute.jaxbexception:"+((JAXBException)e).getLinkedException().getMessage());
			}
		} else 
			result = new ReportGenerationForm();
		
		return result; 
    }		
	
	@RequestMapping(value="/report/execute/{id}", method = RequestMethod.GET)
    public String init(Model model,
    		@PathVariable("id") Integer id,
    		HttpServletRequest request, 
    		RedirectAttributes ra, 
    		SessionStatus sessionStatus) {
		//..end this session to create NEW form..
		sessionStatus.setComplete();
		//..remember id for a while..
		request.getSession().setAttribute("id", id);
		return "redirect:/report/execute/form";
    }
	
	@RequestMapping(value="/report/execute/form", method = RequestMethod.GET)
    public String form(Model model, @ModelAttribute(ReportGenerationForm.KEY) ReportGenerationForm form, 
    		BindingResult results, HttpServletRequest request, RedirectAttributes ra) {
		//..if it was no able to create form redirect and show error.. 
		if (form==null) return "redirect:/report/pattern/list";
		
		if (reportService.exceededsTemporaryReports()) {
			alerts.addWarning(request, "report.archive.maximum.exceeded", String.valueOf(reportService.findTemporary().size()));
		}
		
		return "/report/report-execute-form";
    }
	
	
	@RequestMapping(value= "/report/execute/form", method = RequestMethod.POST)
    public String read(@Valid @ModelAttribute(ReportGenerationForm.KEY) final ReportGenerationForm form, 
    		BindingResult results, HttpServletRequest request, RedirectAttributes ra) {
		if (!results.hasErrors()) {
			try {
				return "redirect:/report/execute/generate";
			} catch (Exception e) {
				exception(e, form, ra, request);
			}
		}
		
		return "/report/report-execute-form";
	}
	
	@RequestMapping(value="/report/execute/generate", method = RequestMethod.GET)
    public String generate(Model model, @Valid @ModelAttribute(ReportGenerationForm.KEY) ReportGenerationForm form, 
    		BindingResult results, HttpServletRequest request, RedirectAttributes ra) {
		
		if (reportService.exceededsTemporaryReports()) {
			alerts.addWarning(request, "report.archive.maximum.exceeded", String.valueOf(reportService.findTemporary().size()));
		}
		
		return "/report/report-execute-generate";
    }
	
	@RequestMapping(value= "/report/execute/generate", method = RequestMethod.POST)
    public String generate(@Valid @ModelAttribute(ReportGenerationForm.KEY) final ReportGenerationForm form, 
    		BindingResult results, HttpServletRequest request, RedirectAttributes ra) {
		if (!results.hasErrors()) {
			try {
				form.addReport(reportService.generate(form));
				alerts.addSuccess(ra, "report.execute.success", form.getPattern().getName(), form.getPattern().getVersion());
				return "redirect:/report/execute/summary";
			} catch (Exception e) {
				exception(e, form, ra, request);
			}
		}
		
		return "/report/report-execute-generate";
	}	
	
	@RequestMapping(value="/report/execute/summary", method = RequestMethod.GET)
    public String summary(Model model, @Valid @ModelAttribute(ReportGenerationForm.KEY) ReportGenerationForm form, 
    		BindingResult results, HttpServletRequest request, RedirectAttributes ra) {
		if (results.hasErrors()) {
			
		}
		
		model.addAttribute("maxtemp", ReportService.MAX_TEMPORARY_REPORTS);
		model.addAttribute("reports", reportService.findTemporary());
		return "/report/report-execute-summary";
    }
	
	@RequestMapping(value= "/report/execute/summary", method = RequestMethod.POST)
    public String summary(@Valid @ModelAttribute(ReportGenerationForm.KEY) final ReportGenerationForm form, 
    		BindingResult results, HttpServletRequest request, RedirectAttributes ra) {
		return "redirect:/report/execute/form";
	}		
	
//	@RequestMapping(value="/report/execute/summary/archive", method = RequestMethod.GET)
//    public String archive(Model model, @Valid @ModelAttribute(ReportExecuteForm.KEY) ReportExecuteForm form, 
//    		BindingResult results, HttpServletRequest request, RedirectAttributes ra) {
//
//		Report report = form.getReport();
//		if (report==null) {
//			alerts.addError(ra, "report.execute.no.report");
//			return "redirect:/report/execute/form";
//		}
//		
//		try {
//			reportService.archive(form.getReport().getId());
//			alerts.addSuccess(ra, "report.execute.archive.file.success", report.getName());
//		} catch (Exception e) {
//			exception(e, form, ra, request);
//			return "redirect:/report/execute/summary";
//		}
//		
//		
//		return "redirect:/report/archives";
//	}
	
	@RequestMapping(value="/report/execute/archive/delete/{id}", method = RequestMethod.GET)
    public String delete(Model model, @PathVariable("id") Long id,  RedirectAttributes ra, HttpServletRequest request) {
		if (id==null) {
			alerts.addError(ra, "report.archive.no.report");
			return "redirect:/report/execute/summary";
		}

		try {
			Report report = reportService.findNoMatterWhat(id);
			reportService.delete(id);
			alerts.addSuccess(ra, "report.archive.delete.success", report.getName());
		} catch (Exception e) {
			alerts.addError(ra, "report.archive.delete.error", e.getMessage());
		}
		
		return "redirect:/report/execute/summary";
	}		
	
	private void exception(Exception e, ReportGenerationForm form, RedirectAttributes ra, HttpServletRequest request) {
		if (e instanceof IOException) {
			alerts.addError(request, "report.execute.ioexception", e.getMessage());
			logger.error("report.execute.ioexception:"+Exceptions.stack(e));
		} else if (e instanceof JAXBException) {
			alerts.addError(request, "report.execute.jaxbexception", ((JAXBException)e).getLinkedException().getMessage());
			logger.error("report.execute.jaxbexception:"+Exceptions.stack(e));
		} else if (e instanceof ReportValidationException) {
			alerts.addError(request, "report.execute.detailed.error", form.getName(), e.getMessage());
			logger.error("report.execute.detailed.error:"+Exceptions.stack(e));
		} else if (e instanceof DataAccessException) {
			alerts.addError(request, "client.datasource.error.detailed", e.getMessage());
			logger.error("client.datasource.error.detailed:"+Exceptions.stack(e));
		} else {
			alerts.addError(request, "report.execute.detailed.error", form.getName(), Exceptions.stack(e));
			logger.error("report.execute.detailed.error:"+Exceptions.stack(e));
		}
	}
	
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		if (binder.getTarget() instanceof ReportGenerationForm) binder.setValidator(new ReportGenerationValidator());
	}
}
