/**
 * 
 */
package pl.com.dbs.reports.report.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import pl.com.dbs.reports.support.web.alerts.Alerts;


/**
 * TODO
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
@Controller
@Scope("request")
public class ReportPatternDetailsController {
	@Autowired private Alerts alerts;
	
	@RequestMapping(value="/report/pattern/details/{id}", method = RequestMethod.GET)
    public String get(Model model, @PathVariable("id") Integer id) {
		return "report/report-pattern-details";
    }
}
