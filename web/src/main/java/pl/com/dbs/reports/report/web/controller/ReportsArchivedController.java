/**
 * 
 */
package pl.com.dbs.reports.report.web.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import pl.com.dbs.reports.api.report.Report;
import pl.com.dbs.reports.report.dao.ReportFilter;
import pl.com.dbs.reports.report.service.ReportService;
import pl.com.dbs.reports.report.web.form.ReportsArchivedForm;
import pl.com.dbs.reports.report.web.validator.ReportsArchivedValidator;
import pl.com.dbs.reports.security.domain.SessionContext;
import pl.com.dbs.reports.support.utils.exception.Exceptions;
import pl.com.dbs.reports.support.web.alerts.Alerts;
import pl.com.dbs.reports.support.web.controller.DownloadController;


/**
 * Lista wykonanych raportow dostepna dla danego profilu.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2013
 */
@Controller
@SessionAttributes({ReportsArchivedForm.KEY})
public class ReportsArchivedController {
	private static final Logger logger = LoggerFactory.getLogger(ReportsArchivedController.class);
	
	@Autowired private Alerts alerts;
	@Autowired private ReportService reportService;
	@Autowired private ReportsArchivedValidator validator;
	
	@ModelAttribute(ReportsArchivedForm.KEY)
    public ReportsArchivedForm createForm() {
		ReportsArchivedForm form = new ReportsArchivedForm();
		return form;
    }
	
	@RequestMapping(value="/report/archived/{id}", method = RequestMethod.GET)
    public String archive(Model model, @PathVariable("id") Long id, @ModelAttribute(ReportsArchivedForm.KEY) final ReportsArchivedForm form) {
		form.reset(id);
		return "redirect:/report/archived";
    }		
	
	@RequestMapping(value="/report/archived/init", method = RequestMethod.GET)
    public String initarchives(Model model, @ModelAttribute(ReportsArchivedForm.KEY) final ReportsArchivedForm form) {
		form.reset();
		return "redirect:/report/archived";
    }
	
	@RequestMapping(value="/report/archived/current", method = RequestMethod.GET)
    public String myarchives(Model model, @ModelAttribute(ReportsArchivedForm.KEY) final ReportsArchivedForm form) {
		form.reset(SessionContext.getProfile());
		return "redirect:/report/archived";
    }	
	
	@RequestMapping(value="/report/archived", method = RequestMethod.GET)
    public String archives(Model model, @ModelAttribute(ReportsArchivedForm.KEY) final ReportsArchivedForm form) {
		model.addAttribute("reports", reportService.find(form.getFilter()));
		model.addAttribute("current", SessionContext.getProfile().getId().equals(form.getFilter().getProfileId()));
		return "report/report-archived";
    }	
	
	@RequestMapping(value= "/report/archived", method = RequestMethod.POST)
    public String archives(Model model, @Valid @ModelAttribute(ReportsArchivedForm.KEY) final ReportsArchivedForm form, BindingResult results) {
		return "redirect:/report/archived";
	}
	
	@RequestMapping(value="/report/archive/{id}/download", method = RequestMethod.GET)
    public String display(Model model, @PathVariable("id") Long id,  @ModelAttribute(ReportsArchivedForm.KEY) final ReportsArchivedForm form,
    		HttpServletRequest request, HttpSession session, HttpServletResponse response) {
		ReportFilter filter = new ReportFilter().archived().onlyFor(id);
		Report report = reportService.findSingle(filter);
		if (report==null) {
			alerts.addError(session, "report.archives.no.report");
			return "redirect:/report/archived";
		}
		
		try {
			DownloadController.download(report.getContent(), report.getName(), request, response);
		} catch (IOException e) {
			exception(e, session);
			return "redirect:/report/archived";
		}
		
		return null;
    }
	
	
	@RequestMapping(value="/report/archived/delete/{id}", method = RequestMethod.GET)
    public String delete(Model model, @RequestParam(required=false) String site, @PathVariable("ids") Long[] ids,  HttpSession session) {
		List<Report> reports = new ArrayList<Report>();
		for (Long id : ids) {
			try {
				ReportFilter filter = new ReportFilter().archived().onlyFor(id);
				Report report = reportService.findSingle(filter);
				if (report != null) {
					reportService.delete(id);
					reports.add(report);
				}
			} catch (Exception e) {
				logger.error("report.archive.delete.error:"+Exceptions.stack(e));
			}
		}
		
		if (reports.size()>1) alerts.addSuccess(session, "report.archive.multi.delete.success", String.valueOf(reports.size()));
		else if (reports.size()==1) alerts.addSuccess(session, "report.archive.delete.success", reports.get(0).getName());
		else alerts.addError(session, "report.archive.delete.error", "");
		
		return StringUtils.isBlank(site)?"redirect:/report/archived":"redirect:/"+site;
	}

	
	private void exception(Exception e, HttpSession session) {
		if (e instanceof IOException) {
			alerts.addError(session, "report.archive.ioexception", e.getMessage());
			logger.error("report.archive.ioexception:"+e.getMessage());
		} else {
			alerts.addError(session, "report.archive.read.error", e.getMessage());
			logger.error("report.archive.read.error:"+e.getMessage());
		}
	}	
	
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		if (binder.getTarget() instanceof ReportsArchivedForm) binder.setValidator(validator);
	}
}
