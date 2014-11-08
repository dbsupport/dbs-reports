/**
 * 
 */
package pl.com.dbs.reports.report.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import pl.com.dbs.reports.report.dao.ReportFilter;
import pl.com.dbs.reports.report.domain.Report;
import pl.com.dbs.reports.report.domain.ReportPhase.ReportPhaseStatus;
import pl.com.dbs.reports.report.service.ReportService;

import com.google.inject.internal.Lists;

/**
 * 
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2014
 */
@Component
public class ReportsUnarchivedHelper {
	@Autowired private ReportService reportService;
	
	/**
	 * set model with unarchoved and info about if there is more that that.
	 */
	public void unarchivedLimited(Model model) {
		model.addAttribute("unarchiveds", new ReportsUnarchived());
	}
	
	public class ReportsUnarchived {
		private List<Report> reports;
		private long all;
		private int max;
		private boolean more = false;
		
		ReportsUnarchived() {
			ReportFilter filter = new ReportFilter().inPhases(Lists.newArrayList(ReportPhaseStatus.INIT, ReportPhaseStatus.START));
			reports = reportService.find(filter);
			all = filter.getPager().getDataSize();
			max = ReportService.MAX_UNARCHIVED;
			more = all>reports.size();
		}

		public List<Report> getReports() {
			return reports;
		}

		public long getAll() {
			return all;
		}

		public int getMax() {
			return max;
		}

		public boolean isMore() {
			return more;
		}
		
	}
}
