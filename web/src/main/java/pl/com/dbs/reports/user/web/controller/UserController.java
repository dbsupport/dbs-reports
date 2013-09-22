/**
 * 
 */
package pl.com.dbs.reports.user.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import pl.com.dbs.reports.support.web.message.WebMessages;

/**
 * Obsluga uzytkownikow
 * 
 * @author krzysztof.kaziura@gmail.com
 */
@Controller
public class UserController {
	@Autowired private WebMessages webmessages;
	
	@RequestMapping(value="/user/list", method = RequestMethod.GET)
    public String list(RedirectAttributes ra) {
		return "user/list";
    }
	
	@RequestMapping(value="/user/profile", method = RequestMethod.GET)
    public String profile(RedirectAttributes ra, @RequestParam(value="code", required=false) String code) {
		return "user/profile";
    }	
	
//	@InitBinder
//	protected void initBinder(WebDataBinder binder) {
//		if (binder.getTarget() instanceof PasswordChangeForm) binder.setValidator(validator);
//	}
}
