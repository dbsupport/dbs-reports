/**
 * 
 */
package pl.com.dbs.reports.profile.web.controller;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomCollectionEditor;
import org.springframework.context.MessageSource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import pl.com.dbs.reports.access.service.AccessService;
import pl.com.dbs.reports.profile.domain.ProfileException;
import pl.com.dbs.reports.profile.service.ProfileAuthorityService;
import pl.com.dbs.reports.profile.service.ProfileService;
import pl.com.dbs.reports.profile.web.form.ProfileNewForm;
import pl.com.dbs.reports.profile.web.validator.ProfileNewValidator;
import pl.com.dbs.reports.support.web.alerts.Alerts;
import pl.com.dbs.reports.support.web.file.FileMeta;

/**
 * Add new profile.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2013
 */
@Controller
@SessionAttributes({ProfileNewForm.KEY})
public class ProfileNewController {
	private static final Logger logger = LoggerFactory.getLogger(ProfileNewController.class);
	@Autowired private Alerts alerts;
	@Autowired private MessageSource messageSource;
	@Autowired private ProfileService profileService;
	@Autowired private AccessService accessService;
	@Autowired private ProfileAuthorityService profileAuthorityService;
	
	@ModelAttribute(ProfileNewForm.KEY)
    public ProfileNewForm createForm() {
		ProfileNewForm form = new ProfileNewForm();
		return form;
    }		
	
	@RequestMapping(value="/profile/new", method = RequestMethod.GET)
    public String init(Model model, @ModelAttribute(ProfileNewForm.KEY) final ProfileNewForm form, SessionStatus sessionStatus) {
		sessionStatus.setComplete();
		return "redirect:/profile/new/personal";
    }
	
	@RequestMapping(value="/profile/new/personal", method = RequestMethod.GET)
    public String personal(Model model, @ModelAttribute(ProfileNewForm.KEY) final ProfileNewForm form) {
		return "profile/profile-new-personal";
    }

	@RequestMapping(value="/profile/new/access", method = RequestMethod.GET)
    public String access(Model model, @ModelAttribute(ProfileNewForm.KEY) final ProfileNewForm form) {
		model.addAttribute("accesses", accessService.find());
		model.addAttribute("authorities", profileAuthorityService.find());
		return "profile/profile-new-access";
    }
	
	@RequestMapping(value="/profile/new/summary", method = RequestMethod.GET)
    public String summary(Model model, @ModelAttribute(ProfileNewForm.KEY) final ProfileNewForm form) {
		return "profile/profile-new-summary";
    }
	
	/**
	 * http://hmkcode.com/spring-mvc-jquery-file-upload-multiple-dragdrop-progress/
	 */
	@RequestMapping(value="/profile/new/photo", method = RequestMethod.GET)
	@ResponseBody
    public FileSystemResource photo(@ModelAttribute(ProfileNewForm.KEY) final ProfileNewForm form) {
		return form.isPhoto()?new FileSystemResource(form.getPhoto()):null; 
    }
	
	@RequestMapping(value="/profile/new/photo/delete", method = RequestMethod.GET)
    public String photo(@ModelAttribute(ProfileNewForm.KEY) final ProfileNewForm form, RedirectAttributes ra) {
		//FIXME: AJAXEM
		form.addPhoto(null); 
		return "profile/profile-new-personal";
    }	

	@RequestMapping(value="/profile/new/photo/upload", method = RequestMethod.POST)
	@ResponseBody
    public LinkedList<FileMeta> photo(@ModelAttribute(ProfileNewForm.KEY) final ProfileNewForm form, MultipartHttpServletRequest request, HttpServletResponse response) {
		 Iterator<String> itr =  request.getFileNames();
	     MultipartFile file = request.getFile(itr.next());
	     LinkedList<FileMeta> result = new LinkedList<FileMeta>();
	     try {
	    	 result.add(new FileMeta(file));
	    	 form.addPhoto(result.get(0));
	     } catch (IOException e) {}
	     return result;
    }
	
	
	@RequestMapping(value= "/profile/new/personal", method = RequestMethod.POST)
    public String personal(@Valid @ModelAttribute(ProfileNewForm.KEY) final ProfileNewForm form, BindingResult results) {
		if (!results.hasErrors()) {
			return "redirect:/profile/new/access";
		}
		return "redirect:/profile/new/personal";
	}
	
	@RequestMapping(value= "/profile/new/access", method = RequestMethod.POST)
    public String access(@Valid @ModelAttribute(ProfileNewForm.KEY) final ProfileNewForm form, BindingResult results, HttpServletRequest request) {
		if (!results.hasErrors()) {
			return "redirect:/profile/new/summary";
		}
		return "redirect:/profile/new/access";
	}
	
	@RequestMapping(value= "/profile/new/summary", method = RequestMethod.POST)
    public String summary(@Valid @ModelAttribute(ProfileNewForm.KEY) final ProfileNewForm form, BindingResult results, HttpSession session) {
		if (!results.hasErrors()) {
			try {
				profileService.add(form);
			} catch (Exception e) {
				exception(e, session);
				return "redirect:/profile/new/summary";
			}
			
			alerts.addSuccess(session, "profile.new.added", form.getLogin());
			return "redirect:/profile/list";
		}
		return "redirect:/profile/new/summary";
	}
	
	private void exception(Exception e, HttpSession session) {
		if (e instanceof ProfileException) {
			String msg = e.getMessage();
			if (((ProfileException) e).getCode()!=null) {
				if (!((ProfileException) e).getParams().isEmpty()) 
					msg = messageSource.getMessage(((ProfileException) e).getCode(), ((ProfileException) e).getParams().toArray(), null);
				else msg = messageSource.getMessage(((ProfileException) e).getCode(), null, null);
				alerts.addError(session, msg);
			}
		} else {
			alerts.addError(session, "profile.add.unknown.error", e.getMessage());
			logger.error("profile.add.unknown.error:"+e.getMessage());
		}
	}	
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		if (binder.getTarget() instanceof ProfileNewForm) binder.setValidator(new ProfileNewValidator(profileService));
		
		binder.registerCustomEditor(List.class, "authorities", new CustomCollectionEditor(List.class) {
			@Override
			protected Object convertElement(Object element) {
				if (element != null && element instanceof String) {
					try {
						long id = Long.valueOf((String)element);
						return profileAuthorityService.find(id);
					} catch (NumberFormatException e) {}
				}
				return null;     
			}
		});
		
		binder.registerCustomEditor(List.class, "accesses", new CustomCollectionEditor(List.class) {
			@Override
			protected Object convertElement(Object element) {
				if (element != null && element instanceof String) {
					try {
						long id = Long.valueOf((String)element);
						return accessService.find(id);
					} catch (NumberFormatException e) {}
				}
				return null;     
			}
		});		
	}
}
