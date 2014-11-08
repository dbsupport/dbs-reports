/**
 * 
 */
package pl.com.dbs.reports.report.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import pl.com.dbs.reports.api.report.ReportLoggings;
import pl.com.dbs.reports.logging.dao.LoggingEventDao;
import pl.com.dbs.reports.logging.dao.LoggingEventFilter;
import pl.com.dbs.reports.logging.domain.LoggingEvent;
import pl.com.dbs.reports.report.dao.ReportFilter;
import pl.com.dbs.reports.report.domain.Report;
import pl.com.dbs.reports.report.service.ReportService;
import pl.com.dbs.reports.support.web.controller.DownloadController;


/**
 * Pobiera logi (logback) dla danego raportu.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2014
 */
@Controller
public class ReportsLogsController {
	private static final Logger logger = LoggerFactory.getLogger(ReportsLogsController.class);
	private static final DateTimeFormatter FORMAT = DateTimeFormat.forPattern("dd.MM.yyyy HH:mm:ss");
	private static final String SUFFIX = ".log";
	private static final String DELIM = " ";
	@Autowired private LoggingEventDao loggingEventDao;
	@Autowired private ReportService reportService;
	
	@RequestMapping(value="/report/{id}/logs", method = RequestMethod.GET)
    public @ResponseBody ReportLogs logs(Model model, @PathVariable Long id, HttpServletRequest request) {
		LoggingEventFilter filter = new LoggingEventFilter().rid(id).marked(ReportLoggings.MRK_USER);
		return new ReportLogs(loggingEventDao.find(filter), filter, request.isUserInRole("Admin"));
	}

	@RequestMapping(value="/report/{id}/logs/download", method = RequestMethod.GET)
    public String download(Model model, @PathVariable Long id, HttpSession session, HttpServletRequest request, HttpServletResponse response) {
		Report report = reportService.findSingle(new ReportFilter().onlyFor(id));
		if (report==null) return null;
		
		List<LoggingEvent> logs = loggingEventDao.find(new LoggingEventFilter().rid(id).limitless());
		if (logs.isEmpty()) return null;
		
		try {
			StringBuffer sb = new StringBuffer();
			for (LoggingEvent log : logs) {
				sb.append(log.getLevel()).append(DELIM).append(FORMAT.print(new DateTime(log.getDate()))).append(System.getProperty("line.separator"))
				.append(log.getMsg()).append(System.getProperty("line.separator"));
			}
			DownloadController.download(sb.toString().getBytes(), report.getName()+SUFFIX, request, response);
		} catch (Exception e) {
			logger.error("Error downloading logs for:{}, {}", id, e.getMessage());
		}
		
		return null;
    }
	
	public static class ReportLogs {
		private Long rid;
		private List<ReportLog> logs = new ArrayList<ReportLog>();
		private int count = 0;
		private boolean downloadable = false;
		
		ReportLogs(final List<LoggingEvent> logs, final LoggingEventFilter filter, boolean downloadable) {
			this.rid = filter.getRid();
			this.count = filter.getPager().getDataSize();
			this.downloadable = downloadable;
			for (LoggingEvent log : logs) {
				this.logs.add(new ReportLog(log));
			}
		}

		public Long getRid() {
			return rid;
		}

		public List<ReportLog> getLogs() {
			return logs;
		}

		public int getCount() {
			return count;
		}

		public boolean isDownloadable() {
			return downloadable;
		}
	}
	
	public static class ReportLog {
		private String level;
		private String date;
		private String msg;
		
		ReportLog(LoggingEvent log) {
			level = log.getLevel();
			date = FORMAT.print(new DateTime(log.getDate()));
			msg = log.getMsg();
		}

		public String getLevel() {
			return level;
		}

		public String getDate() {
			return date;
		}

		public String getMsg() {
			return msg;
		}
		
	}
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
	}	
}
