/**
 * 
 */
package pl.com.dbs.reports.report.web.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import pl.com.dbs.reports.report.domain.ReportOrderNotification;
import pl.com.dbs.reports.report.domain.ReportOrderNotification.PreReportOrderNotificationStatus;
import pl.com.dbs.reports.report.service.ReportOrderService;

import com.google.inject.internal.Lists;


/**
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2014
 */
@Controller
public class ReportsStatusController {
	private static final PeriodFormatter FORMAT = new PeriodFormatterBuilder()
																			.printZeroAlways()
																			.minimumPrintedDigits(2)
																			.appendHours()
																			.appendSeparator(":")
																			.appendMinutes()
																			.appendSeparator(":")
																			.appendSeconds()
																			.toFormatter();	
	
	@Autowired private ReportOrderService reportOrderService;
	/**
	 * Get list of NEWLY generated reports.
	 * Mark them as UNSEEN - user has been notified.
	 * ajax
	 */
	@RequestMapping(value="/report/order/notications", method = RequestMethod.GET)
    public @ResponseBody ReportsNotification ready(Model model, HttpServletRequest request) {
		ReportsNotification notification = new ReportsNotification();
		reportOrderService.notiftyAll();
		return notification;
    }	

	@InitBinder
	protected void initBinder(WebDataBinder binder) {
	}
	
	public class ReportsNotification {
		private int brandnew = 0;
		private List<ReportsNotificationOrder> orders;
		private int reports = 0;
		
		ReportsNotification() {
			List<ReportOrderNotification> notifications = reportOrderService.get();
			this.orders = Lists.newArrayList();
			for (ReportOrderNotification notification : notifications) {
				this.orders.add(new ReportsNotificationOrder(notification));
				this.reports += notification.getOrder().getReports().size();
				if (PreReportOrderNotificationStatus.UNNOTIFIED.equals(notification.getStatus())) 
					brandnew++;
			}
		}

		public int getBrandnew() {
			return brandnew;
		}

		public List<ReportsNotificationOrder> getOrders() {
			return orders;
		}

		public int getReports() {
			return reports;
		}

	}
	
	
	/**
	 * Reports that are announced for the very first time! (one only)
	 */
	@JsonIgnoreProperties({"NAME_LENGTH"})
	public class ReportsNotificationOrder {
		private static final int NAME_LENGTH = 30;
		private long id;
		private String name;
		private String time;
		private int count = 0;
		
		ReportsNotificationOrder(ReportOrderNotification notification) {
			this.id = notification.getOrder().getId();
			this.name = notification.getOrder().getReports().get(0).getName();
			this.name = this.name.length()>NAME_LENGTH?(this.name.substring(0, NAME_LENGTH-2)+".."):this.name;
			DateTime start = new DateTime(notification.getOrder().getDate());
			DateTime now = new DateTime();
			Period period = new Period(start, now);
			this.time = FORMAT.print(period);
			this.count = notification.getOrder().getReports().size();
		}

		public long getId() {
			return id;
		}

		public String getName() {
			return name;
		}

		public String getTime() {
			return time;
		}

		public int getCount() {
			return count;
		}

	}
	
}
