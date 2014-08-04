/**
 * 
 */
package pl.com.dbs.reports.profile.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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

import pl.com.dbs.reports.api.profile.ClientProfile;
import pl.com.dbs.reports.profile.domain.ClientProfilesFilter;
import pl.com.dbs.reports.profile.service.ProfileScheduler;
import pl.com.dbs.reports.profile.web.form.ProfileEncodingForm;
import pl.com.dbs.reports.profile.web.view.ClientProfileEncoding;

/**
 * Tests..
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
@Controller
@SessionAttributes({ProfileEncodingForm.KEY})
public class ProfileEncodingController {
	@Autowired private ProfileScheduler profileScheduler;

	
	@ModelAttribute(ProfileEncodingForm.KEY)
    public ProfileEncodingForm createProfileEncodingForm() {
		ProfileEncodingForm form = new ProfileEncodingForm();
		return form;
    }
	
	@RequestMapping(value="/profile/encoding", method = RequestMethod.GET)
    public String encoding(Model model, @ModelAttribute(ProfileEncodingForm.KEY) final ProfileEncodingForm form) {
		return "profile/profile-encoding";
    }	
	
	@RequestMapping(value= "/profile/encoding", method = RequestMethod.POST)
    public String encoding(Model model, @Valid @ModelAttribute(ProfileEncodingForm.KEY) final ProfileEncodingForm form, BindingResult results, HttpServletRequest request, RedirectAttributes ra) {
		List<ClientProfileEncoding> result = new ArrayList<ClientProfileEncoding>();
		
		for (final String inencoding : form.getInEncodings()) {
			for (final String outencoding : form.getOutEncodings()) {
				ClientProfilesFilter filter = new ClientProfilesFilter() {
					@Override
					public String getLogin() {
						return null;
					}
		
					@Override
					public String getPasswd() {
						return null;
					}
		
					@Override
					public Integer getMax() {
						return 1;
					}
		
					@Override
					public String getInEncoding() {
						return inencoding;
					}
		
					@Override
					public String getOutEncoding() {
						return outencoding;
					}
		
					@Override
					public String getUuid() {
						return form.getUuid();
					}
				};
				
				List<ClientProfile> profiles = profileScheduler.findClientProfiles(filter);
				result.add(new ClientProfileEncoding(inencoding, outencoding, !profiles.isEmpty()?profiles.get(0).getDescription():""));
			}
		}
		model.addAttribute("profiles", result);
		
		return "profile/profile-encoding";
	}
	
	@RequestMapping(value="/profile/encoding/reset/in", method = RequestMethod.GET)
    public String resetin(Model model, @ModelAttribute(ProfileEncodingForm.KEY) final ProfileEncodingForm form) {
    	form.resetInEncodings();
	    return "redirect:/profile/encoding";
	}
	
	@RequestMapping(value="/profile/encoding/reset/out", method = RequestMethod.GET)
    public String resetout(Model model, @ModelAttribute(ProfileEncodingForm.KEY) final ProfileEncodingForm form) {
    	form.resetOutEncodings();
	    return "redirect:/profile/encoding";
	}
	
	
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
	}	
	
}
