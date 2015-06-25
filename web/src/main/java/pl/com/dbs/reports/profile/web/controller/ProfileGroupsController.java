/**
 * 
 */
package pl.com.dbs.reports.profile.web.controller;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomCollectionEditor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import pl.com.dbs.reports.access.domain.Access;
import pl.com.dbs.reports.access.service.AccessService;
import pl.com.dbs.reports.profile.domain.ProfileGroup;
import pl.com.dbs.reports.profile.service.ProfileGroupService;
import pl.com.dbs.reports.profile.web.form.ProfileGroupListForm;
import pl.com.dbs.reports.profile.web.validator.ProfileGroupListValidator;
import pl.com.dbs.reports.support.utils.exception.Exceptions;
import pl.com.dbs.reports.support.web.alerts.Alerts;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Add new group profile.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2015
 */
@Controller
@SessionAttributes({ProfileGroupListForm.KEY})
public class ProfileGroupsController {
	private static final Logger logger = LoggerFactory.getLogger(ProfileGroupsController.class);
    @Autowired private MessageSource messageSource;
    @Autowired private Alerts alerts;
	@Autowired private AccessService accessService;
    @Autowired private ProfileGroupService profileGroupService;
	
	@ModelAttribute(ProfileGroupListForm.KEY)
    public ProfileGroupListForm createForm() {
        ProfileGroupListForm form = new ProfileGroupListForm();
		return form;
    }

    @RequestMapping(value="/profile/group/list", method = RequestMethod.GET)
    public String list(Model model, @ModelAttribute(ProfileGroupListForm.KEY) final ProfileGroupListForm form) {
        model.addAttribute("groups", profileGroupService.find(form.getFilter()));
        return "profile/profile-group-list";
    }

    @RequestMapping(value= "/profile/group/list", method = RequestMethod.POST)
    public String list(Model model, @Valid @ModelAttribute(ProfileGroupListForm.KEY) final ProfileGroupListForm form, BindingResult results, HttpSession session) {
        if (results.hasErrors()) return "redirect:/profile/group/list";

        if (ProfileGroupListForm.Action.REMOVE.equals(form.getAction())) {
            delete(model, null, form.getIdAsArray(), session);
        }
        form.reset();

        return "redirect:/profile/group/list";
    }

    @RequestMapping(value="/profile/group/{ids}/delete", method = RequestMethod.GET)
    public String delete(Model model, @RequestParam(required=false) String site, @PathVariable Long[] ids, HttpSession session) {
        List<ProfileGroup> groups = new ArrayList<ProfileGroup>();
        int profiles = 0;
        for (Long id : ids) {
            try {
                ProfileGroup group = profileGroupService.findById(id);
                profiles += group.getProfiles().size();
                groups.add(group);
                profileGroupService.delete(id);
            } catch (Exception e) {
                logger.error("profile.group.delete.error:"+ Exceptions.stack(e));
            }
        }

        StringBuilder msg = new StringBuilder();

        if (groups.size()>1&&groups.size()<5) msg.append(messageSource.getMessage("profile.group.multi234.delete.success", new Object[]{String.valueOf(groups.size())}, null));
        else if (groups.size()>4) msg.append(messageSource.getMessage("profile.group.multi5.delete.success", new Object[]{String.valueOf(groups.size())}, null));
        else if (groups.size()==1) msg.append(messageSource.getMessage("profile.group.delete.success", new Object[]{groups.get(0).getName()}, null));
        else msg.append(messageSource.getMessage("profile.group.delete.error", null, null));

        if (profiles>0) msg.append("<br/>").append(messageSource.getMessage("profile.group.edit.profiles", new Object[]{String.valueOf(profiles)}, null));

        if (groups.size()>0) alerts.addSuccess(session, msg.toString());
        else alerts.addError(session, msg.toString());

        return StringUtils.isBlank(site)?"redirect:/profile/group/list":"redirect:/"+site;
    }

    @ModelAttribute("accesses")
    public List<Access> accesses() {
        return accessService.find();
    }

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        if (binder.getTarget() instanceof ProfileGroupListForm) binder.setValidator(new ProfileGroupListValidator());

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
