/**
 * 
 */
package pl.com.dbs.reports.report.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import pl.com.dbs.reports.support.web.alerts.Alerts;

/**
 * 
 * 
 * @author krzysztof.kaziura@gmail.com
 */
@Controller
@Scope("request")
public class ReportTemplateImportController {
	@Autowired private Alerts alerts;
	
//	@ModelAttribute(UserNewForm.KEY)
//    public UserNewForm createForm() {
//		UserNewForm form = new UserNewForm();
//		return form;
//    }		
	
	@RequestMapping(value="/report/template/import", method = RequestMethod.GET)
    public String list(Model model) {
		return "report/template-import";
    }
	
	
//	@InitBinder
//	protected void initBinder(WebDataBinder binder) {
//		if (binder.getTarget() instanceof UserNewForm) binder.setValidator(new UserNewValidator());
//	}
}
