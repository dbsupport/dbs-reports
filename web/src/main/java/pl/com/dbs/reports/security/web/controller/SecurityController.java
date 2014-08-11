/**
 * 
 */
package pl.com.dbs.reports.security.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import pl.com.dbs.reports.parameter.service.ParameterService;
import pl.com.dbs.reports.profile.domain.Profile;
import pl.com.dbs.reports.profile.service.ProfileService;
import pl.com.dbs.reports.profile.web.ProfileSession;
import pl.com.dbs.reports.security.domain.SessionContext;
import pl.com.dbs.reports.support.web.alerts.Alerts;

/**
 * Security entrypoints.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
@Controller
public class SecurityController {
	private static final Logger logger = Logger.getLogger(SecurityController.class);
	@Autowired private Alerts alerts;
	@Autowired private ProfileService profileService;
	@Autowired private ParameterService parameterService;
	
	@RequestMapping(value="/security/login", method = RequestMethod.GET)
    public String login(RedirectAttributes ra) {
		logger.debug("OS:"+System.getProperty("os.name").toLowerCase());
		return "security/login";
    }
	
	@RequestMapping(value="/security/loginfailed", method = RequestMethod.GET)
    public String loginfailed(Model model, @RequestParam(value="code", required=false) String code, HttpSession session) {
		if (!StringUtils.isBlank(code)) alerts.addError(session, code);	
		return "redirect:/security/login";
    }
	
	@RequestMapping(value="/security/noaccess", method = RequestMethod.GET)
    public String noaccess(ModelMap model, @RequestParam(value="code", required=false) String code, HttpSession session) {
		if (!StringUtils.isBlank(code)) alerts.addError(session, code);
		model.addAttribute("profile", SessionContext.getProfile());
		return "security/noaccess";
	}	
	
	@RequestMapping(value="/security/entrypoint", method = RequestMethod.GET)
    public String entrypoint(ModelMap model, HttpServletRequest request) {
		Profile profile = profileService.findCurrent();
		ProfileSession.update(profile, request);
		ProfileSession.update(parameterService.find(ParameterService.APP_HELP_FILE), request);
		return "redirect:/profile";
	}
	
}
