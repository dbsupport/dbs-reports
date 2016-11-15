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
import pl.com.dbs.reports.profile.service.ProfileGroupService;
import pl.com.dbs.reports.profile.web.form.ProfileGroupNewForm;
import pl.com.dbs.reports.profile.web.validator.ProfileGroupNewValidator;
import pl.com.dbs.reports.support.web.alerts.Alerts;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;
import java.util.Set;

/**
 * Add new group profile.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2015
 */
@Controller
@SessionAttributes({ProfileGroupNewForm.KEY})
public class ProfileGroupNewController {
	private static final Logger logger = LoggerFactory.getLogger(ProfileGroupNewController.class);
	@Autowired private Alerts alerts;
	@Autowired private AccessService accessService;
    @Autowired private ProfileGroupService profileGroupService;
	
	@ModelAttribute(ProfileGroupNewForm.KEY)
    public ProfileGroupNewForm createForm() {
        ProfileGroupNewForm form = new ProfileGroupNewForm();
		return form;
    }

    @RequestMapping(value="/profile/group/new/init", method = RequestMethod.GET)
    public String init(Model model, @ModelAttribute(ProfileGroupNewForm.KEY) final ProfileGroupNewForm form) {
        form.reset();
        return "redirect:/profile/group/new";
    }

	@RequestMapping(value="/profile/group/new", method = RequestMethod.GET)
    public String add(Model model, @ModelAttribute(ProfileGroupNewForm.KEY) final ProfileGroupNewForm form) {
		return "profile/profile-group-new";
    }

    @RequestMapping(value= "/profile/group/new", method = RequestMethod.POST)
    public String add(@Valid @ModelAttribute(ProfileGroupNewForm.KEY) final ProfileGroupNewForm form, BindingResult results, HttpSession session) {
        if (results.hasErrors()) return "redirect:/profile/group/new";

        try {
            profileGroupService.add(form);
            alerts.addSuccess(session, "profile.group.new.added", form.getName());
        } catch (Exception e) {
            alerts.addError(session, "profile.group.new.error", form.getName(), e.getMessage());
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
		if (binder.getTarget() instanceof ProfileGroupNewForm) binder.setValidator(new ProfileGroupNewValidator(profileGroupService));

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
