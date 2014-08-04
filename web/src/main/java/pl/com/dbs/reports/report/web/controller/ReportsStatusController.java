/**
 * 
 */
package pl.com.dbs.reports.report.web.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
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

import pl.com.dbs.reports.report.domain.Report;
import pl.com.dbs.reports.report.domain.ReportOrder;
import pl.com.dbs.reports.report.domain.ReportOrder.ReportOrderStatus;
import pl.com.dbs.reports.report.service.ReportOrderService;
import pl.com.dbs.reports.security.domain.SessionContext;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.inject.internal.Lists;


/**
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2014
 */
@Controller
public class ReportsStatusController {
	private static final Logger logger = Logger.getLogger(ReportsStatusController.class);
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
		try {
			reportOrderService.notifi(SessionContext.getProfile());
		} catch (Exception e) {
			logger.error("Error while notofiaction orders..", e);
		}
		return notification;
    }	

	@InitBinder
	protected void initBinder(WebDataBinder binder) {
	}
	
	public class ReportsNotification {
		/**
		 * how many new order are finished?
		 * 
		 */
		private int brandnew = 0;
		/**
		 * orders
		 */
		private List<ReportsNotificationOrder> orders;
		/**
		 * how many NOT archived reports in all orders
		 */
		private int reports = 0;
		
		ReportsNotification() {
			this.orders = Lists.newArrayList();
			for (ReportOrder order : reportOrderService.findReady(SessionContext.getProfile())) {
				this.orders.add(new ReportsNotificationOrder(order));
				for (Report report : order.getReports()) {
					if (report.getPhase().isFinishedUnarchived())
						this.reports++;
				}
				if (ReportOrderStatus.UNNOTIFIED.equals(order.getStatus())) 
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
		private static final int NAME_LENGTH = 35;
		private long id;
		private String name;
		private String time;
		private List<Long> reports;
		
		ReportsNotificationOrder(ReportOrder order) {
			this.id = order.getId();
			this.name = order.getName();
			this.name = this.name.length()>NAME_LENGTH?(this.name.substring(0, NAME_LENGTH-2)+".."):this.name;
			DateTime start = new DateTime(order.getStart());
			DateTime end = new DateTime(order.getEnd()!=null?order.getEnd():new Date());
			Period period = new Period(start, end);
			this.time = FORMAT.print(period);
			this.reports = Lists.newArrayList(
					Iterables.transform(order.getReports(), new Function<Report, Long>() {
							@Override
							public Long apply(Report input) {
								return input.getId();
							}
						}));
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

		public List<Long> getReports() {
			return reports;
		}

	}
	
}
