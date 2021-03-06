/**
 * 
 */
package pl.com.dbs.reports.report.web.controller;

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

import pl.com.dbs.reports.report.dao.ReportFilter;
import pl.com.dbs.reports.report.domain.Report;
import pl.com.dbs.reports.report.service.ReportService;
import pl.com.dbs.reports.report.web.form.ReportsUnarchivedForm;
import pl.com.dbs.reports.report.web.form.ReportsUnarchivedForm.Action;
import pl.com.dbs.reports.report.web.validator.ReportsUnarchivedValidator;
import pl.com.dbs.reports.security.domain.SessionContext;
import pl.com.dbs.reports.support.utils.exception.Exceptions;
import pl.com.dbs.reports.support.web.alerts.Alerts;
import pl.com.dbs.reports.support.web.controller.DownloadController;

import com.google.inject.internal.Lists;


/**
 * Lista wykonanych raportow dostepna dla danego profilu.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2013
 */
@Controller
@SessionAttributes({ReportsUnarchivedForm.KEY})
public class ReportsUnarchivedController {
	private static final Logger logger = LoggerFactory.getLogger(ReportsUnarchivedController.class);
	
	@Autowired private Alerts alerts;
	@Autowired private ReportService reportService;
	@Autowired private ReportsUnarchivedValidator validator;
	
	@ModelAttribute(ReportsUnarchivedForm.KEY)
    public ReportsUnarchivedForm form() {
		return new ReportsUnarchivedForm();
    }

	@RequestMapping(value="/report/unarchived", method = RequestMethod.GET)
    public String init(Model model, @ModelAttribute(ReportsUnarchivedForm.KEY) final ReportsUnarchivedForm form) {
		form.reset();
		return "redirect:/report/unarchived/list";
	}
	
	@RequestMapping(value="/report/unarchived/order/{ids}", method = RequestMethod.GET)
    public String order(Model model, @PathVariable("ids") Long[] ids, @ModelAttribute(ReportsUnarchivedForm.KEY) final ReportsUnarchivedForm form) {
		form.reset(ids);
		return "redirect:/report/unarchived/list";
	}	
	
	@RequestMapping(value="/report/unarchived/list", method = RequestMethod.GET)
    public String unarchiveds(Model model, @ModelAttribute(ReportsUnarchivedForm.KEY) final ReportsUnarchivedForm form) {
		List<Report> reports = reportService.find(form.getFilter());
		model.addAttribute("reports", reports);
		model.addAttribute("max", ReportService.MAX_UNARCHIVED);
		model.addAttribute("phases", Lists.newArrayList(ReportsUnarchivedForm.Phase.values()));
		model.addAttribute("statuses", Lists.newArrayList(ReportsUnarchivedForm.Status.values()));

		return "report/report-unarchived";
    }	
	
	@RequestMapping(value= "/report/unarchived/list", method = RequestMethod.POST)
    public String unarchiveds(Model model, @Valid @ModelAttribute(ReportsUnarchivedForm.KEY) final ReportsUnarchivedForm form, BindingResult results, HttpSession session) {
		if (results.hasErrors()) {
			return "redirect:/report/unarchived/list";
		}
		
		if (Action.ARCHIVE.equals(form.getAction())) {
			archive(model, null, form.getIdAsArray(), session);
		} else if (Action.REMOVE.equals(form.getAction())) {
			delete(model, null, form.getIdAsArray(), session);
		} else if (Action.CONFIRM.equals(form.getAction())) {
			confirm(model, null, form.getIdAsArray(), session);
		}
		form.reset();
		
		return "redirect:/report/unarchived/list";
	}
	
	/**
	 * Przenies do archiwum niezarchiwizowany raport.
	 */
	@RequestMapping(value="/report/unarchived/archive/{ids}", method = RequestMethod.GET)
    public String archive(Model model, @RequestParam(required=false) String site, @PathVariable("ids") Long[] ids, HttpSession session) {
		List<Report> reports = new ArrayList<Report>();
		for (Long id : ids) { 
			try {
				reports.add(reportService.archive(id));
			} catch (Exception e) {
				logger.error("report.unarchive.archive.error:"+Exceptions.stack(e));
			}
		}
			
		if (reports.size()>1&&reports.size()<5) alerts.addSuccess(session, "report.unarchive.archive.multi234.success", String.valueOf(reports.size()));
		else if (reports.size()>4) alerts.addSuccess(session, "report.unarchive.archive.multi5.success", String.valueOf(reports.size()));
		else if (reports.size()==1) alerts.addSuccess(session, "report.unarchive.archive.success", reports.get(0).getName());
		else alerts.addError(session, "report.unarchive.archive.error", "");
		
		return StringUtils.isBlank(site)?"redirect:/report/unarchived/list":"redirect:/"+site;
	}
	
	@RequestMapping(value="/report/unarchived/delete/{ids}", method = RequestMethod.GET)
    public String delete(Model model, @RequestParam(required=false) String site, @PathVariable("ids") Long[] ids, HttpSession session) {
		List<Report> reports = new ArrayList<Report>();
		for (Long id : ids) {
			try {
				//FIXME: temporary archived reports available only for users..
				//ReportFilter filter = new ReportFilter().onlyFor(id);//unarchived()
				ReportFilter filter = new ReportFilter().onlyFor(id).onlyFor(SessionContext.getProfile());
				Report report = reportService.findSingle(filter);
				if (report!=null) {
					reports.add(report);
					reportService.delete(id);
				}
			} catch (Exception e) {
				logger.error("report.unarchive.delete.error:"+Exceptions.stack(e));
			}
		}
			
		if (reports.size()>1&&reports.size()<5) alerts.addSuccess(session, "report.unarchive.multi234.delete.success", String.valueOf(reports.size()));
		else if (reports.size()>4) alerts.addSuccess(session, "report.unarchive.multi5.delete.success", String.valueOf(reports.size()));
		else if (reports.size()==1) alerts.addSuccess(session, "report.unarchive.delete.success", reports.get(0).getName());
		else alerts.addError(session, "report.unarchive.delete.error", "");
			
		return StringUtils.isBlank(site)?"redirect:/report/unarchived/list":"redirect:/"+site;
	}
	
	/**
	 * Przenies do archiwum niezarchiwizowany raport.
	 */
	@RequestMapping(value="/report/unarchived/confirm/{ids}", method = RequestMethod.GET)
    public String confirm(Model model, @RequestParam(required=false) String site, @PathVariable("ids") Long[] ids, HttpSession session) {
		List<Report> reports = new ArrayList<Report>();
		for (Long id : ids) { 
			try {
				reports.add(reportService.confirm(id));
			} catch (Exception e) {
				logger.error("report.unarchive.confirm.error:"+Exceptions.stack(e));
			}
		}
			
		if (reports.size()>1&&reports.size()<5) alerts.addSuccess(session, "report.unarchive.confirm.multi234.success", String.valueOf(reports.size()));
		else if (reports.size()>4) alerts.addSuccess(session, "report.unarchive.confirm.multi5.success", String.valueOf(reports.size()));
		else if (reports.size()==1) alerts.addSuccess(session, "report.unarchive.confirm.success", reports.get(0).getName());
		else alerts.addError(session, "report.archive.error");
		
		return StringUtils.isBlank(site)?"redirect:/report/unarchived/list":"redirect:/"+site;
	}	
	
	@RequestMapping(value="/report/unarchived/{id}/download", method = RequestMethod.GET)
    public String display(Model model, @PathVariable("id") Long id, 
    		HttpSession session, HttpServletRequest request, HttpServletResponse response) {

		try {
			//FIXME: temporary archived reports available only for users..
			//ReportFilter filter = new ReportFilter().unarchived().onlyFor(id).fine();
			ReportFilter filter = new ReportFilter().unarchived().onlyFor(id).onlyFor(SessionContext.getProfile()).fine();

			Report report = reportService.findSingle(filter);
			if (report==null||!report.isDownloadable()) {
				alerts.addError(session, "report.unarchived.no.report");
				return "redirect:/report/unarchived/list";	
			}
			
			DownloadController.download(report.getContent(), report.getName(), request, response);
		} catch (Exception e) {
			alerts.addError(session, "report.unarchived.read.error", e.getMessage());
			logger.error("report.unarchived.read.error:"+e.getMessage());
			return "redirect:/report/unarchived/list";
		}
		
		return null;
    }
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		if (binder.getTarget() instanceof ReportsUnarchivedForm) binder.setValidator(validator);
	}	
}
