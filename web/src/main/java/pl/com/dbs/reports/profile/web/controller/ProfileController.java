/**
 * 
 */
package pl.com.dbs.reports.profile.web.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import pl.com.dbs.reports.profile.domain.Profile;
import pl.com.dbs.reports.profile.domain.ProfileException;
import pl.com.dbs.reports.profile.domain.ProfilePhoto;
import pl.com.dbs.reports.profile.service.ProfilePhotoService;
import pl.com.dbs.reports.profile.service.ProfileScheduler;
import pl.com.dbs.reports.profile.service.ProfileService;
import pl.com.dbs.reports.profile.web.ProfileSession;
import pl.com.dbs.reports.profile.web.form.ProfileForm;
import pl.com.dbs.reports.profile.web.form.ProfileListForm;
import pl.com.dbs.reports.profile.web.validator.ProfileListValidator;
import pl.com.dbs.reports.profile.web.validator.ProfileValidator;
import pl.com.dbs.reports.report.service.ReportService;
import pl.com.dbs.reports.security.domain.SessionContext;
import pl.com.dbs.reports.support.web.alerts.Alerts;
import pl.com.dbs.reports.support.web.file.FileMeta;

/**
 * Profiles actions..
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
@Controller
@SessionAttributes({ProfileListForm.KEY, ProfileForm.KEY})
@Scope("request")
public class ProfileController {
	private static final Logger logger = Logger.getLogger(ProfileController.class);
	@Autowired private Alerts alerts;
	@Autowired private MessageSource messageSource;
	@Autowired private ProfileService profileService;
	@Autowired private ProfilePhotoService profilePhotoService;
	@Autowired private ReportService reportService;
	@Autowired private ProfileScheduler profileScheduler;

	
	@ModelAttribute(ProfileListForm.KEY)
    public ProfileListForm createProfileListForm() {
		ProfileListForm form = new ProfileListForm();
		return form;
    }
	
	@ModelAttribute(ProfileForm.KEY)
    public ProfileForm createProfileForm() {
		ProfileForm form = new ProfileForm();
		return form;
    }	
	
	@RequestMapping(value="/profile/list", method = RequestMethod.GET)
    public String profiles(Model model, @ModelAttribute(ProfileListForm.KEY) final ProfileListForm form) {
		model.addAttribute("profiles", profileService.find(form.getFilter()));
		return "profile/profile-list";
    }	
	
	@RequestMapping(value="/profile", method = RequestMethod.GET)
    public String profile(Model model, @ModelAttribute(ProfileForm.KEY) final ProfileForm form) {
		Profile profile = profileService.findById(SessionContext.getProfile().getId());
		model.addAttribute("profile", profile);
		model.addAttribute("current", true);
		
		form.reset(profile);
		return "profile/profile";
    }
	
	@RequestMapping(value= "/profile/note", method = RequestMethod.POST)
    public String note(@Valid @ModelAttribute(ProfileForm.KEY) final ProfileForm form, BindingResult results, HttpServletRequest request, RedirectAttributes ra) {
		if (!results.hasErrors()) {
			try {
				Profile profile = profileService.note(form.getId(), form.getNote());
				ProfileSession.update(profile, request);
				alerts.addSuccess(ra, "profile.note.edited");
			} catch (Exception e) {
				alerts.addError(ra, "profile.note.not.edited", e.getMessage());
			}
		}
		
		return SessionContext.getProfile().isSame(form.getId())?"redirect:/profile":("redirect:/profile/"+form.getId());	
	}	
	
	@RequestMapping(value="/profile/{id}", method = RequestMethod.GET)
    public String profile(Model model, @PathVariable("id") Long id, @ModelAttribute(ProfileForm.KEY) final ProfileForm form) {
		Profile profile = profileService.findById(id);
		model.addAttribute("profile", profile);
		model.addAttribute("current", SessionContext.getProfile().isSame(profile));
		model.addAttribute("reports", null);
		
		form.reset(profile);
		return "profile/profile";
    }	
	
	@RequestMapping(value="/profile/photo/{id}", method = RequestMethod.GET)
	@ResponseBody
    public FileSystemResource photo(@PathVariable("id") Long id) {
		ProfilePhoto photo = profilePhotoService.find(id);
		if (photo!=null) {
			try {
				return new FileSystemResource(FileMeta.bytesToFile(photo.getPath(), photo.getContent()));
			} catch (IOException e) {
				logger.error("error reading photo from:"+photo.getPath());
			}
		}
		return null; 
    }
	
	@RequestMapping(value="/profile/accept/{id}", method = RequestMethod.GET)
    public String accept(Model model, @PathVariable("id") Long id, RedirectAttributes ra) {
		if (id.equals(SessionContext.getProfile().getId())) {
			alerts.addError(ra, "profile.accept.session.profile");
			return "redirect:/profile/"+id;	
		}
		
		try {
			Profile profile = profileService.accept(id);
			alerts.addSuccess(ra, "profile.accepted", profile.getName());
		} catch (Exception e) {
			alerts.addError(ra, "profile.delete.wrong.id");
			return "redirect:/profile/list";	
		}
		
		return "redirect:/profile/"+id;
    }		
	
	@RequestMapping(value="/profile/unaccept/{id}", method = RequestMethod.GET)
    public String unaccept(Model model, @PathVariable("id") Long id, RedirectAttributes ra) {
		if (id.equals(SessionContext.getProfile().getId())) {
			alerts.addError(ra, "profile.unaccept.session.profile");
			return "redirect:/profile/"+id;	
		}
		
		try {
			Profile profile = profileService.unaccept(id);
			alerts.addSuccess(ra, "profile.unaccepted", profile.getName());
		} catch (Exception e) {
			alerts.addError(ra, "profile.delete.wrong.id");
			return "redirect:/profile/list";	
		}
		
		return "redirect:/profile/"+id;
    }	
	
	@RequestMapping(value= "/profile/list", method = RequestMethod.POST)
    public String profiles(@Valid @ModelAttribute(ProfileListForm.KEY) final ProfileListForm form, BindingResult results, HttpServletRequest request, RedirectAttributes ra) {
		return "redirect:/profile/list";
	}
	
	@RequestMapping(value="/profile/delete/{id}", method = RequestMethod.GET)
    public String delete(Model model, @PathVariable("id") Long id, HttpServletRequest request, RedirectAttributes ra) {
		Profile profile = profileService.findById(id);
		if (profile==null) {
			alerts.addError(ra, "profile.delete.wrong.id");
			return "redirect:/profile/list";
		}
		
		if (SessionContext.getProfile().getId().equals(id)) { 
			alerts.addWarning(ra, "profile.delete.current");
			return "redirect:/profile/list";
		}
		
		try {
			profileService.delete(id);
			alerts.addSuccess(ra, "profile.delete.deleted", profile.getName());
		} catch (Exception e) {
			exception(e, request, ra);
		}
		return "redirect:/profile/list";
    }

	@RequestMapping(value="/profiles/synchronize", method = RequestMethod.GET)
    public String synchronize(Model model, RedirectAttributes ra) {
		try {
			profileScheduler.synchronize();
			alerts.addSuccess(ra, "profile.synchronization");
		} catch (Exception e) {
			alerts.addError(ra, "profile.synchronization.error", e.getMessage());
		}
		return "redirect:/profile/list";
    }
	
	private void exception(Exception e, HttpServletRequest request, RedirectAttributes ra) {
		if (e instanceof ProfileException) {
			String msg = e.getMessage();
			if (((ProfileException) e).getCode()!=null) {
				if (!((ProfileException) e).getParams().isEmpty()) 
					msg = messageSource.getMessage(((ProfileException) e).getCode(), ((ProfileException) e).getParams().toArray(), null);
				else msg = messageSource.getMessage(((ProfileException) e).getCode(), null, null);
				alerts.addError(ra, msg);
			}
		} else {
			alerts.addError(ra, "profile.add.unknown.error", e.getMessage());
			logger.error("profile.add.unknown.error:"+e.getMessage());
		}
	}		
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		if (binder.getTarget() instanceof ProfileListForm) binder.setValidator(new ProfileListValidator());
		if (binder.getTarget() instanceof ProfileForm) binder.setValidator(new ProfileValidator());
	}	
	
}
