/**
 * 
 */
package pl.com.dbs.reports.parameter.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import pl.com.dbs.reports.api.support.db.ClientDataSource;
import pl.com.dbs.reports.parameter.service.ParameterService;
import pl.com.dbs.reports.parameter.web.form.ParameterEditForm;
import pl.com.dbs.reports.profile.service.ProfileScheduler;
import pl.com.dbs.reports.support.web.alerts.Alerts;


/**
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
@Controller
@SessionAttributes({ParameterEditForm.KEY})
@Scope("request")
public class ParameterController {
	private static final Logger logger = Logger.getLogger(ParameterController.class);
	
	@Autowired private Alerts alerts;
	@Autowired private ParameterService parameterService;
	@Autowired private ProfileScheduler profileScheduler;
	@Autowired @Qualifier(ClientDataSource.DATASOURCE) private ClientDataSource datasource;	

	@ModelAttribute(ParameterEditForm.KEY)
    public ParameterEditForm createListForm() {
		ParameterEditForm form = new ParameterEditForm();
		return form;
    }		
	
	@RequestMapping(value="/param/edit", method = RequestMethod.GET)
    public String edit(Model model, @ModelAttribute(ParameterEditForm.KEY) final ParameterEditForm form) {
		form.reset(parameterService.find());
		return "parameter/parameter-edit";
    }		
	
	@RequestMapping(value= "/param/edit", method = RequestMethod.POST)
    public String accesses(@Valid @ModelAttribute(ParameterEditForm.KEY) final ParameterEditForm form, BindingResult results, HttpServletRequest request, RedirectAttributes ra) {
		for (ParameterEditForm.Param param : form.getParams()) {
			try {
				if (parameterService.edit(param.getKey(), param.getValue())) {
					alerts.addSuccess(ra, "parameter.edit.edited", param.getKey());
				}
			} catch (Exception e) {
				alerts.addError(ra, "parameter.edit.error", param.getKey(), e.getMessage());
				logger.error("parameter.edit.error:"+e.getMessage());			
			}
		}
		try {
			datasource.reconnect(parameterService.getConnectionContext());
			alerts.addSuccess(ra, "parameter.edit.connection.success");
		} catch (Exception e) {
			alerts.addWarning(ra, "parameter.edit.connection.error");
		}		
		return "redirect:/param/edit";
	}
	
	@RequestMapping(value="/param/synchronize", method = RequestMethod.GET)
    public String synchronize(Model model, RedirectAttributes ra) {
		try {
			profileScheduler.synchronize();
			alerts.addSuccess(ra, "profile.synchronization");
		} catch (Exception e) {
			alerts.addError(ra, "profile.synchronization.error", e.getMessage());
		}
		return "redirect:/param/edit";
    }
	
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
	}
}
