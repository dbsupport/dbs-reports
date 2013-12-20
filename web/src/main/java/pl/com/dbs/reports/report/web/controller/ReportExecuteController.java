/**
 * 
 */
package pl.com.dbs.reports.report.web.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.xml.bind.JAXBException;

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
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import pl.com.dbs.reports.api.report.Report;
import pl.com.dbs.reports.api.report.ReportValidationException;
import pl.com.dbs.reports.report.pattern.domain.ReportPattern;
import pl.com.dbs.reports.report.pattern.domain.ReportPatternForm;
import pl.com.dbs.reports.report.pattern.service.PatternService;
import pl.com.dbs.reports.report.service.ReportService;
import pl.com.dbs.reports.report.web.form.ReportExecuteForm;
import pl.com.dbs.reports.report.web.validator.ReportExecuteValidator;
import pl.com.dbs.reports.support.web.alerts.Alerts;
import pl.com.dbs.reports.support.web.form.DFormBuilder;


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
    public ReportExecuteForm createForm(Model model, HttpServletRequest request, RedirectAttributes ra) {
		ReportExecuteForm result = null;
		
		Integer id = request.getSession().getAttribute("id")!=null?(Integer)request.getSession().getAttribute("id"):null;
		if (id==null) return result;
		request.getSession().removeAttribute("id");
		ReportPattern pattern = patternService.find(id);
		
		//FIXME: only one form allowed..
		ReportPatternForm form = pattern.getForm();
		if (form!=null) {
			try {
				DFormBuilder<ReportExecuteForm> builder = new DFormBuilder<ReportExecuteForm>(form.getContent(), ReportExecuteForm.class);
				
				result = builder.build().getForm();
				result.reset(pattern);
			} catch (JAXBException e) {
				result = null;
				//FIXME: not seen..
				alerts.addError(request, "report.execute.jaxbexception", ((JAXBException)e).getLinkedException().getMessage());
				logger.error("report.execute.jaxbexception:"+((JAXBException)e).getLinkedException().getMessage());
			}
		} else 
			result = new ReportExecuteForm();
		
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
    public String form(Model model, @ModelAttribute(ReportExecuteForm.KEY) ReportExecuteForm form, 
    		BindingResult results, HttpServletRequest request, RedirectAttributes ra) {
		//..if it was no able to create form redirect and show error.. 
		if (form==null) return "redirect:/report/pattern/list";
		
		return "/report/report-execute-form";
    }
	
	
	@RequestMapping(value= "/report/execute/form", method = RequestMethod.POST)
    public String read(@Valid @ModelAttribute(ReportExecuteForm.KEY) final ReportExecuteForm form, 
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
    public String generate(Model model, @Valid @ModelAttribute(ReportExecuteForm.KEY) ReportExecuteForm form, 
    		BindingResult results, HttpServletRequest request, RedirectAttributes ra) {
		return "/report/report-execute-generate";
    }
	
	@RequestMapping(value= "/report/execute/generate", method = RequestMethod.POST)
    public String generate(@Valid @ModelAttribute(ReportExecuteForm.KEY) final ReportExecuteForm form, 
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
    public String summary(Model model, @Valid @ModelAttribute(ReportExecuteForm.KEY) ReportExecuteForm form, 
    		BindingResult results, HttpServletRequest request, RedirectAttributes ra) {
		if (results.hasErrors()) {
			
		}
		return "/report/report-execute-summary";
    }
	
	@RequestMapping(value= "/report/execute/summary", method = RequestMethod.POST)
    public String summary(@Valid @ModelAttribute(ReportExecuteForm.KEY) final ReportExecuteForm form, 
    		BindingResult results, HttpServletRequest request, RedirectAttributes ra) {
		return "redirect:/report/execute/form";
	}		
	
	@RequestMapping(value="/report/execute/summary/display", method = RequestMethod.GET)
    public String display(Model model, @Valid @ModelAttribute(ReportExecuteForm.KEY) ReportExecuteForm form, 
    		BindingResult results, RedirectAttributes ra, HttpServletRequest request, HttpServletResponse response) {

		Report report = form.getReport();
		if (report==null) {
			alerts.addError(ra, "report.execute.no.report");
			return "redirect:/report/execute/form";
		}
		
		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition","attachment;filename="+report.getName());
	 
		ServletOutputStream out = null;
		InputStream in = null;
		try {
			out = response.getOutputStream();
			in = new ByteArrayInputStream(report.getContent());
			byte[] outputByte = new byte[4096];
			while(in.read(outputByte, 0, 4096) != -1) {
				out.write(outputByte, 0, 4096);
			}
			in.close();
			out.flush();
			out.close();
		} catch (IOException e) {
			exception(e, form, ra, request);
			return "redirect:/report/execute/summary";
		} finally {
			try {
				out.close();
				in.close();
			} catch (IOException e) {
				exception(e, form, ra, request);
				return "redirect:/report/execute/summary";
			}
		}
		
		return null;
    }
	
	@RequestMapping(value="/report/execute/summary/archive", method = RequestMethod.GET)
    public String archive(Model model, @Valid @ModelAttribute(ReportExecuteForm.KEY) ReportExecuteForm form, 
    		BindingResult results, HttpServletRequest request, RedirectAttributes ra) {

		Report report = form.getReport();
		if (report==null) {
			alerts.addError(ra, "report.execute.no.report");
			return "redirect:/report/execute/form";
		}
		
		try {
			reportService.archive(form.getReport());
			alerts.addSuccess(ra, "report.execute.archive.file.success", report.getName());
		} catch (Exception e) {
			exception(e, form, ra, request);
			return "redirect:/report/execute/summary";
		}
		
		
		return "redirect:/report/archives";
	}
	
	
	
	private void exception(Exception e, ReportExecuteForm form, RedirectAttributes ra, HttpServletRequest request) {
		if (e instanceof IOException) {
			alerts.addError(request, "report.execute.ioexception", e.getMessage());
			logger.error("report.execute.ioexception:"+e.getMessage());
		} else if (e instanceof JAXBException) {
			alerts.addError(request, "report.execute.jaxbexception", ((JAXBException)e).getLinkedException().getMessage());
			logger.error("report.execute.jaxbexception:"+e.getStackTrace());
		} else if (e instanceof ReportValidationException) {
			alerts.addError(request, "report.execute.detailed.error", form.getName(), e.getMessage());
			logger.error("report.execute.detailed.error:"+e.getStackTrace());
		} else {
			alerts.addError(request, "report.execute.error", e.getMessage());
			logger.error("report.execute.error:"+e.getMessage());
		}
	}
	
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		if (binder.getTarget() instanceof ReportExecuteForm) binder.setValidator(new ReportExecuteValidator());
	}
}
