/**
 * 
 */
package pl.com.dbs.reports.profile.web.controller;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import pl.com.dbs.reports.access.service.AccessService;
import pl.com.dbs.reports.profile.dao.ProfileGroupsFilter;
import pl.com.dbs.reports.profile.domain.Profile;
import pl.com.dbs.reports.profile.domain.ProfileException;
import pl.com.dbs.reports.profile.service.ProfileAuthorityService;
import pl.com.dbs.reports.profile.service.ProfileGroupService;
import pl.com.dbs.reports.profile.service.ProfileService;
import pl.com.dbs.reports.profile.web.ProfileSession;
import pl.com.dbs.reports.profile.web.form.ProfileEditForm;
import pl.com.dbs.reports.profile.web.validator.ProfileEditValidator;
import pl.com.dbs.reports.security.domain.SessionContext;
import pl.com.dbs.reports.support.web.alerts.Alerts;
import pl.com.dbs.reports.support.web.file.FileMeta;


/**
 * Profil - edycja.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2013
 */
@Controller
@SessionAttributes({ProfileEditForm.KEY})
public class ProfileEditController {
	private static final Logger logger = LoggerFactory.getLogger(ProfileEditController.class);
	@Autowired private Alerts alerts;
	@Autowired private MessageSource messageSource;
	@Autowired private ProfileService profileService;
    @Autowired private ProfileGroupService profileGroupService;
	@Autowired private AccessService accessService;
	@Autowired private ProfileAuthorityService profileAuthorityService;
	
	@ModelAttribute(ProfileEditForm.KEY)
    public ProfileEditForm createForm() {
		ProfileEditForm form = new ProfileEditForm();
		return form;
    }
	
	@RequestMapping(value="/profile/edit", method = RequestMethod.GET)
    public String init(Model model, @ModelAttribute(ProfileEditForm.KEY) final ProfileEditForm form) {
		return "redirect:/profile/edit/"+form.getId();
    }
	
	
	@RequestMapping(value="/profile/edit/{id}", method = RequestMethod.GET)
    public String init(Model model, @PathVariable("id") Long id, @ModelAttribute(ProfileEditForm.KEY) final ProfileEditForm form, HttpSession session) {
        ProfileGroupsFilter filter = new ProfileGroupsFilter().pid(id);

		form.reset(profileService.findById(id), profileGroupService.find(filter));
		if (!form.hasProfile()) {
			alerts.addError(session, "profile.edit.wrong.id");
			return "redirect:/profile/list";
		}
		//return "redirect:/profile/edit/personal";
		return "profile/profile-edit-personal";
    }
	
	@RequestMapping(value="/profile/edit/personal", method = RequestMethod.GET)
    public String personal(Model model, @ModelAttribute(ProfileEditForm.KEY) final ProfileEditForm form) {
		return "profile/profile-edit-personal";
    }

	@RequestMapping(value="/profile/edit/access", method = RequestMethod.GET)
    public String access(Model model, @ModelAttribute(ProfileEditForm.KEY) final ProfileEditForm form) {
		if (!SessionContext.hasAnyRole(SessionContext.ROLE_ADMIN)) {
			return "redirect:/profile/edit/summary";	
		}
        model.addAttribute("groups", profileGroupService.find());
		model.addAttribute("accesses", accessService.find());
		model.addAttribute("authorities", profileAuthorityService.find());
		return "profile/profile-edit-access";
    }
	
	@RequestMapping(value="/profile/edit/summary", method = RequestMethod.GET)
    public String summary(Model model, @ModelAttribute(ProfileEditForm.KEY) final ProfileEditForm form) {
        if (form.hasGroups()) model.addAttribute("groups", profileGroupService.find(new ProfileGroupsFilter().groupsInclude(form.getGroups())));
        return "profile/profile-edit-summary";
    }
	
	/**
	 * http://hmkcode.com/spring-mvc-jquery-file-upload-multiple-dragdrop-progress/
	 */
	@RequestMapping(value="/profile/edit/photo", method = RequestMethod.GET)
	@ResponseBody
    public FileSystemResource photo(@ModelAttribute(ProfileEditForm.KEY) final ProfileEditForm form) {
		return form.isPhoto()?new FileSystemResource(form.getPhoto()):null; 
    }
	
	@RequestMapping(value="/profile/edit/photo/delete", method = RequestMethod.GET)
    public String deletePhoto(@ModelAttribute(ProfileEditForm.KEY) final ProfileEditForm form) {
		//FIXME: AJAXEM
		form.addPhoto(null); 
		return "profile/profile-edit-personal";
    }	

	@RequestMapping(value="/profile/edit/photo/upload", method = RequestMethod.POST)
	@ResponseBody
    public LinkedList<FileMeta> photo(@ModelAttribute(ProfileEditForm.KEY) final ProfileEditForm form, MultipartHttpServletRequest request, HttpServletResponse response) {
		 Iterator<String> itr =  request.getFileNames();
	     MultipartFile file = request.getFile(itr.next());
	     LinkedList<FileMeta> result = new LinkedList<FileMeta>();
	     try {
	    	 result.add(new FileMeta(file));
	    	 form.addPhoto(result.get(0));
	     } catch (IOException e) {}
	     return result;
    }
	
	
	
	@RequestMapping(value= "/profile/edit/personal", method = RequestMethod.POST)
    public String personal(@Valid @ModelAttribute(ProfileEditForm.KEY) final ProfileEditForm form, BindingResult results, HttpServletRequest request, HttpSession session) {
		if (!results.hasErrors()) {
			if (form.saveNow()) return save(form, request, session);
			return "redirect:/profile/edit/access";
		}
		return "profile/profile-edit-personal";
	}
	
	@RequestMapping(value= "/profile/edit/access", method = RequestMethod.POST)
    public String access(@Valid @ModelAttribute(ProfileEditForm.KEY) final ProfileEditForm form, BindingResult results, HttpServletRequest request, HttpSession session) {
		if (!SessionContext.hasAnyRole(SessionContext.ROLE_ADMIN)) {
			return "redirect:/profile/edit/summary";
		}
		if (!results.hasErrors()) {
			if (form.saveNow()) return save(form, request, session);
			return "redirect:/profile/edit/summary";
		}
		return "profile/profile-edit-access";
	}
	
	@RequestMapping(value= "/profile/edit/summary", method = RequestMethod.POST)
    public String summary(@Valid @ModelAttribute(ProfileEditForm.KEY) final ProfileEditForm form, BindingResult results, HttpServletRequest request, HttpSession session) {
		if (!results.hasErrors()) {
			return save(form, request, session);
		}
		return "profile/profile-edit-summary";
	}
	
	private String save(final ProfileEditForm form, HttpServletRequest request, HttpSession session) {
		try {
			profileService.edit(form);
		} catch (Exception e) {
			exception(e, session);
			return "redirect:/profile/edit/summary";
		}		
		
		Profile profile = profileService.findById(form.getId());
		alerts.addSuccess(session, "profile.edit.edited", form.getLogin());
		ProfileSession.update(profile, request);		
		return "redirect:/profile/"+form.getId();
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
		if (binder.getTarget() instanceof ProfileEditForm) binder.setValidator(new ProfileEditValidator(profileService));
		
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
		
		binder.registerCustomEditor(Set.class, "accesses", new CustomCollectionEditor(Set.class) {
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
