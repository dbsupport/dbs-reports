/**
 * 
 */
package pl.com.dbs.reports.report.web.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import pl.com.dbs.reports.api.report.Report;
import pl.com.dbs.reports.report.service.ReportService;
import pl.com.dbs.reports.report.web.form.ReportArchivesForm;
import pl.com.dbs.reports.report.web.validator.ReportArchivesValidator;
import pl.com.dbs.reports.security.domain.SessionContext;
import pl.com.dbs.reports.support.utils.exception.Exceptions;
import pl.com.dbs.reports.support.web.alerts.Alerts;
import pl.com.dbs.reports.support.web.controller.DownloadController;


/**
 * Lista wykonanych raportow dostepna dla danego profilu.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
@Controller
@SessionAttributes({ReportArchivesForm.KEY})
@Scope("request")
public class ReportArchivesController {
	private static final Logger logger = Logger.getLogger(ReportArchivesController.class);
	
	@Autowired private Alerts alerts;
	@Autowired private ReportService reportService;
	
	@ModelAttribute(ReportArchivesForm.KEY)
    public ReportArchivesForm createForm() {
		ReportArchivesForm form = new ReportArchivesForm();
		return form;
    }
	
	@RequestMapping(value="/report/archives/{id}", method = RequestMethod.GET)
    public String archive(Model model, @PathVariable("id") Long id, @ModelAttribute(ReportArchivesForm.KEY) final ReportArchivesForm form) {
		form.reset(id);
		return "redirect:/report/archives";
    }		
	
	@RequestMapping(value="/report/archives/init", method = RequestMethod.GET)
    public String initarchives(Model model, @ModelAttribute(ReportArchivesForm.KEY) final ReportArchivesForm form) {
		form.reset();
		return "redirect:/report/archives";
    }
	
	@RequestMapping(value="/report/archives/current", method = RequestMethod.GET)
    public String myarchives(Model model, @ModelAttribute(ReportArchivesForm.KEY) final ReportArchivesForm form) {
		form.reset(SessionContext.getProfile());
		return "redirect:/report/archives";
    }	
	
	@RequestMapping(value="/report/archives", method = RequestMethod.GET)
    public String archives(Model model, @ModelAttribute(ReportArchivesForm.KEY) final ReportArchivesForm form) {
		model.addAttribute("reports", reportService.find(form.getFilter()));
		model.addAttribute("current", SessionContext.getProfile().getId().equals(form.getFilter().getProfileId()));
		return "report/report-archives";
    }	
	
	@RequestMapping(value= "/report/archives", method = RequestMethod.POST)
    public String archives(Model model, @Valid @ModelAttribute(ReportArchivesForm.KEY) final ReportArchivesForm form, BindingResult results, HttpServletRequest request, RedirectAttributes ra) {
		return "redirect:/report/archives";
	}
	
	@RequestMapping(value="/report/archives/display/{id}", method = RequestMethod.GET)
    public String display(Model model, @PathVariable("id") Long id,  @ModelAttribute(ReportArchivesForm.KEY) final ReportArchivesForm form,
    		RedirectAttributes ra, HttpServletRequest request, HttpServletResponse response) {
		if (id==null) {
			alerts.addError(ra, "report.archives.no.report");
			return "redirect:/report/archives";
		}

		Report report = reportService.findNoMatterWhat(id);
		if (report==null) {
			alerts.addError(ra, "report.archives.no.report");
			return "redirect:/report/archives";
		}
		
		try {
			DownloadController.download(report.getContent(), report.getName(), request, response);
		} catch (IOException e) {
			exception(e, request, ra);
			return "redirect:/report/archives";
		}
		
		return null;
    }
	
//	@RequestMapping(value="/report/archives/email/{id}", method = RequestMethod.GET)
//    public String email(Model model, @PathVariable("id") Long id,  @ModelAttribute(ReportArchivesForm.KEY) final ReportArchivesForm form,
//    		RedirectAttributes ra, HttpServletRequest request, HttpServletResponse response) {
//		if (id==null) {
//			alerts.addError(ra, "report.archives.no.report");
//			return "redirect:/report/archives";
//		}
//
//		Report report = reportService.find(id);
//		if (report==null) {
//			alerts.addError(ra, "report.archives.no.report");
//			return "redirect:/report/archives";
//		}
//		return null;
//	}
	
	@RequestMapping(value="/report/archives/archive/{id}", method = RequestMethod.GET)
    public String archive(Model model, @PathVariable("id") Long id, HttpServletRequest request, RedirectAttributes ra) {
		try {
			Report report = reportService.archive(id);
			alerts.addSuccess(request, "report.archive.success", report.getName());
		} catch (Exception e) {
			alerts.addError(ra, "report.archive.error", e.getMessage());
			logger.error("report.archive.error:"+Exceptions.stack(e));
			return "redirect:/profile";
		}
		
		return "redirect:/report/archives/init";
	}	
	
	
	@RequestMapping(value="/report/archives/delete/{id}", method = RequestMethod.GET)
    public String delete1(Model model, @PathVariable("id") Long id,  RedirectAttributes ra, HttpServletRequest request) {
		if (id==null) {
			alerts.addError(ra, "report.archive.no.report");
			return "redirect:/report/archives/init";
		}

		deleteArchive(id, ra);
		
		return "redirect:/report/archives";
	}
	
	@RequestMapping(value="/report/archives/temporary/delete/{id}", method = RequestMethod.GET)
    public String delete2(Model model, @RequestParam(required=false) String site, @PathVariable("id") Long id, RedirectAttributes ra, HttpServletRequest request) {
		if (id==null) {
			alerts.addError(ra, "report.archive.no.report");
			return "redirect:/report/archives/temporary";
		}

		deleteArchive(id, ra);
		
		return StringUtils.isBlank(site)?"redirect:/report/archives/temporary":"redirect:/"+site;
	}
	
	void deleteArchive(long id, RedirectAttributes ra) {
		try {
			Report report = reportService.findNoMatterWhat(id);
			reportService.delete(id);
			alerts.addSuccess(ra, "report.archive.delete.success", report.getName());
		} catch (Exception e) {
			alerts.addError(ra, "report.archive.delete.error", e.getMessage());
		}		
	}
	
	
	@RequestMapping(value="/report/archives/temporary", method = RequestMethod.GET)
    public String temporary(Model model, HttpServletRequest request, RedirectAttributes ra) {
		model.addAttribute("reports", reportService.findTemporary());
		model.addAttribute("maxtemp", ReportService.MAX_TEMPORARY_REPORTS);
		return "report/report-archives-temporary";
	}

	
	private void exception(Exception e, HttpServletRequest request, RedirectAttributes ra) {
		if (e instanceof IOException) {
			alerts.addError(request, "report.archive.ioexception", e.getMessage());
			logger.error("report.archive.ioexception:"+e.getMessage());
		} else {
			alerts.addError(request, "report.archive.read.error", e.getMessage());
			logger.error("report.archive.read.error:"+e.getMessage());
		}
	}	
	
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		if (binder.getTarget() instanceof ReportArchivesForm) binder.setValidator(new ReportArchivesValidator());
	}
}
