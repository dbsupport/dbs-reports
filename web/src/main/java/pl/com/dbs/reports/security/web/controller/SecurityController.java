/**
 * 
 */
package pl.com.dbs.reports.security.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
	@Autowired private Alerts alerts;
	@Autowired private ProfileService profileService;
	
	@RequestMapping(value="/security/login", method = RequestMethod.GET)
    public String login(RedirectAttributes ra) {
		return "security/login";
    }
	
	@RequestMapping(value="/security/loginfailed", method = RequestMethod.GET)
    public String loginfailed(RedirectAttributes ra, @RequestParam(value="code", required=false) String code) {
		if (!StringUtils.isBlank(code)) alerts.addError(ra, code);	
		return "redirect:/security/login";
    }
	
	@RequestMapping(value="/security/noaccess", method = RequestMethod.GET)
    public String noaccess(ModelMap model, @RequestParam(value="code", required=false) String code, RedirectAttributes ra) {
		if (!StringUtils.isBlank(code)) alerts.addError(ra, code);
		model.addAttribute("profile", SessionContext.getProfile());
		return "security/noaccess";
	}	
	
	@RequestMapping(value="/security/entrypoint", method = RequestMethod.GET)
    public String entrypoint(ModelMap model, HttpServletRequest request) {
		Profile profile = profileService.findCurrent();
		ProfileSession.update(profile, request);
		return "redirect:/profile";
	}
	
}
