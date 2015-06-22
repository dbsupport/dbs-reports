/**
 * 
 */
package pl.com.dbs.reports.profile.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomCollectionEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import pl.com.dbs.reports.access.domain.Access;
import pl.com.dbs.reports.access.service.AccessService;
import pl.com.dbs.reports.profile.domain.ProfileGroup;
import pl.com.dbs.reports.profile.service.ProfileGroupService;
import pl.com.dbs.reports.profile.web.form.ProfileGroupEditForm;
import pl.com.dbs.reports.profile.web.validator.ProfileGroupEditValidator;
import pl.com.dbs.reports.support.web.alerts.Alerts;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;
import java.util.Set;

/**
 * Edit groups;
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2015
 */
@Controller
@SessionAttributes({ProfileGroupEditForm.KEY})
public class ProfileGroupEditController {
	private static final Logger logger = LoggerFactory.getLogger(ProfileGroupEditController.class);
	@Autowired private Alerts alerts;
	@Autowired private AccessService accessService;
    @Autowired private ProfileGroupService profileGroupService;
	
    @ModelAttribute(ProfileGroupEditForm.KEY)
    public ProfileGroupEditForm editForm() {
        ProfileGroupEditForm form = new ProfileGroupEditForm();
        return form;
    }

    @RequestMapping(value="/profile/group/{id}/edit/init", method = RequestMethod.GET)
    public String init(Model model, @ModelAttribute(ProfileGroupEditForm.KEY) final ProfileGroupEditForm form, @PathVariable Long id) {
        ProfileGroup group = profileGroupService.findById(id);
        form.reset(group);
        return "redirect:/profile/group/edit";
    }

    @RequestMapping(value="/profile/group/edit", method = RequestMethod.GET)
    public String edit(Model model, @ModelAttribute(ProfileGroupEditForm.KEY) final ProfileGroupEditForm form) {
        model.addAttribute("group", profileGroupService.findById(form.getId()));
        return "profile/profile-group-edit";
    }

    @RequestMapping(value= "/profile/group/edit", method = RequestMethod.POST)
    public String edit(@Valid @ModelAttribute(ProfileGroupEditForm.KEY) final ProfileGroupEditForm form, BindingResult results, HttpSession session) {
        if (results.hasErrors()) return "redirect:/profile/group/edit";

        try {
            profileGroupService.edit(form);
            ProfileGroup group = profileGroupService.findById(form.getId());
            alerts.addSuccess(session, "profile.group.edit.edited", group.getName());
            int profiles = group.getProfiles().size();
            if (profiles>0) alerts.addSuccess(session, "profile.group.edit.profiles", String.valueOf(profiles));
        } catch (Exception e) {
            alerts.addError(session, "profile.group.edit.error", e.getMessage());
            logger.error("access.new.error:"+e.getMessage());
        }
        return "redirect:/profile/group/list";
    }


    @ModelAttribute("accesses")
    public List<Access> accesses() {
        return accessService.find();
    }

	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		if (binder.getTarget() instanceof ProfileGroupEditForm) binder.setValidator(new ProfileGroupEditValidator());
		
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

        binder.registerCustomEditor(Set.class, "groups", new CustomCollectionEditor(Set.class) {
            @Override
            protected Object convertElement(Object element) {
                if (element != null && element instanceof String) {
                    try {
                        long id = Long.valueOf((String)element);
                        return profileGroupService.findById(id);
                    } catch (NumberFormatException e) {}
                }
                return null;
            }
        });
    }
}
