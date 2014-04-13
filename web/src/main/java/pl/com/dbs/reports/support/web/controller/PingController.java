/**
 * 
 */
package pl.com.dbs.reports.support.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import pl.com.dbs.reports.profile.web.controller.ProfileController;
import pl.com.dbs.reports.security.domain.SessionContext;

/**
 * To keep session alive.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2014
 */
@Controller
public class PingController {
	private static final Logger logger = Logger.getLogger(ProfileController.class);
	
	@RequestMapping(value="/ping", method = RequestMethod.GET)
    public ResponseEntity<String> ping(Model model, HttpServletRequest request, HttpServletResponse response) {
		logger.debug("ping!");
		return new ResponseEntity<String>(SessionContext.getProfile()==null?HttpStatus.BAD_REQUEST:HttpStatus.OK);
    }
	
}
