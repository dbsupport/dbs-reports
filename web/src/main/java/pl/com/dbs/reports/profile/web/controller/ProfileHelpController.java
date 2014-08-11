/**
 * 
 */
package pl.com.dbs.reports.profile.web.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import pl.com.dbs.reports.parameter.domain.Parameter;
import pl.com.dbs.reports.parameter.service.ParameterService;
import pl.com.dbs.reports.support.web.controller.DownloadController;

/**
 * 
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
@Controller
public class ProfileHelpController {
	
	@Autowired private ParameterService parameterService;
	
	@RequestMapping(value="/profile/help", method = RequestMethod.GET)
    public String help(Model model, HttpSession session,  HttpServletRequest request, HttpServletResponse response) {
		Parameter parameter = parameterService.find(ParameterService.APP_HELP_FILE);
		if (parameter.getValue()!=null) {
			try {
				DownloadController.download(parameter.getValue(), parameter.getDesc(), request, response);
			} catch (IOException e) {}
		}
		
		return null;
    }	
	
}
