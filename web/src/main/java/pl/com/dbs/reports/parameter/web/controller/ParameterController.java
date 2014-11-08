/**
 * 
 */
package pl.com.dbs.reports.parameter.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import pl.com.dbs.reports.api.support.db.ClientDataSource;
import pl.com.dbs.reports.parameter.domain.ParameterScope;
import pl.com.dbs.reports.parameter.service.ParameterService;
import pl.com.dbs.reports.parameter.web.form.ParameterEditForm;
import pl.com.dbs.reports.profile.web.ProfileSession;
import pl.com.dbs.reports.support.web.alerts.Alerts;


/**
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2013
 */
@Controller
@SessionAttributes({ParameterController.FORM_DB_KEY, ParameterController.FORM_APP_KEY})
public class ParameterController {
	private static final Logger logger = LoggerFactory.getLogger(ParameterController.class);
	static final String FORM_DB_KEY = "parametersdbeditform";
	static final String FORM_APP_KEY = "parametersappeditform";
	
	@Autowired private Alerts alerts;
	@Autowired private ParameterService parameterService;
	@Autowired @Qualifier(ClientDataSource.DATASOURCE) private ClientDataSource datasource;	

	@ModelAttribute(FORM_DB_KEY)
    public ParameterEditForm formdb() {
		ParameterEditForm form = new ParameterEditForm(ParameterScope.DB);
		return form;
    }		
	
	@ModelAttribute(FORM_APP_KEY)
    public ParameterEditForm formapp() {
		ParameterEditForm form = new ParameterEditForm(ParameterScope.APP);
		return form;
    }	
	
	@RequestMapping(value="/param/edit", method = RequestMethod.GET)
    public String edit(Model model, @ModelAttribute(FORM_DB_KEY) final ParameterEditForm formdb, 
    								@ModelAttribute(FORM_APP_KEY) final ParameterEditForm formapp) {
		formdb.reset(parameterService.find(formdb.getFilter()));
		formapp.reset(parameterService.find(formapp.getFilter()));
		return "parameter/parameter-edit";
    }		
	
	@RequestMapping(value= "/param/edit/db", method = RequestMethod.POST)
    public String editdb(@Valid @ModelAttribute(FORM_DB_KEY) final ParameterEditForm formdb, 
    					BindingResult results, 
    				    HttpServletRequest request,
    				    HttpSession session) {
		edit(formdb, session);
		try {
			datasource.reconnect(parameterService.getConnectionContext());
			alerts.addSuccess(session, "parameter.edit.connection.success");
		} catch (Exception e) {
			alerts.addWarning(session, "parameter.edit.connection.error");
		}		
		
		return "redirect:/param/edit";
	}
	
	@RequestMapping(value= "/param/edit/app", method = RequestMethod.POST)
    public String edit(@Valid @ModelAttribute(FORM_APP_KEY) final ParameterEditForm formapp, 
    					BindingResult results, 
    				    HttpServletRequest request,
    				    HttpSession session) {
		edit(formapp, session);
		ProfileSession.update(parameterService.find(ParameterService.APP_HELP_FILE), request);
		
		return "redirect:/param/edit";
	}	
	
	
	private void edit(final ParameterEditForm form, HttpSession session) {
		for (ParameterEditForm.Param param : form.getParams()) {
			try {
				if (parameterService.edit(param.getKey(), param.getValueAsBytes(), param.getDesc())) {
					alerts.addSuccess(session, "parameter.edit.edited", param.getKey());
				}
			} catch (Exception e) {
				alerts.addError(session, "parameter.edit.error", param.getKey(), e.getMessage());
				logger.error("parameter.edit.error:"+e.getMessage());			
			}
		}
	}

	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {}
	
}
