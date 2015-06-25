/**
 * 
 */
package pl.com.dbs.reports.profile.service;

import com.google.common.collect.Sets;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.com.dbs.reports.access.domain.Access;
import pl.com.dbs.reports.profile.dao.ProfileGroupDao;
import pl.com.dbs.reports.profile.dao.ProfileGroupsFilter;
import pl.com.dbs.reports.profile.domain.Profile;
import pl.com.dbs.reports.profile.domain.ProfileGroup;
import pl.com.dbs.reports.profile.domain.ProfileGroupCreation;
import pl.com.dbs.reports.profile.domain.ProfileGroupEdit;

import java.util.List;
import java.util.Set;

/**
 * Profiles groups services.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2015
 */
@Service("profile.group.service")
public class ProfileGroupService {
	private static final Logger logger = LoggerFactory.getLogger(ProfileGroupService.class);
	@Autowired private ProfileGroupDao profileGroupDao;

    public ProfileGroup findByName(String name) {
        return profileGroupDao.findByName(name);
    }

	public List<ProfileGroup> find(ProfileGroupsFilter filter) {
        return profileGroupDao.find(filter);
	}

    public List<ProfileGroup> find() {
        return profileGroupDao.find(new ProfileGroupsFilter());
    }

    public ProfileGroup findById(long id) {
        return profileGroupDao.find(id);
    }

	/**
	 * Brand new group profile.
	 */
	@Transactional
	public void add(ProfileGroupCreation form) {
		logger.info("Adding new group profile..");
		
		ProfileGroup group = new ProfileGroup(form);
        profileGroupDao.create(group);
	}
	
	/**
	 * Modify profile.
	 */
	@Transactional
	public void edit(ProfileGroupEdit form) {
		logger.info("Modifying group {} profile..", form.getId());

        ProfileGroup group = profileGroupDao.find(form.getId());
        Validate.notNull(group, "Group is no more!");

        Set<Access> accesses2Add = Sets.newHashSet();
        Set<Access> accesses2Remove = Sets.newHashSet();

        for (Access access : group.getAccesses()) {
            if (!form.getAccesses().contains(access))
                accesses2Remove.add(access);
        }

        for (Access access : form.getAccesses()) {
            if (!group.getAccesses().contains(access))
                accesses2Add.add(access);
        }

        boolean accessesChange = !accesses2Add.isEmpty()||!accesses2Remove.isEmpty();

        //..remove profile accesses that came from group..
        if (accessesChange) removeProfileAccessesByGroup(group);

        //..modify group..
        group.modify(form);

        //..update profile accesses according group..
        if (accessesChange) {
            for (Profile profile : group.getProfiles()) {
                //..profile accesses can be from many groups..
                for (Access access : group.getAccesses()) {
                    profile.addAccess(access);
                }
            }
        }

	}

	@Transactional
	public void delete(Long gid) {
		logger.info("Deleting group profile..");
		Validate.notNull(gid, "Group ID is no more!");

		ProfileGroup group = profileGroupDao.find(gid);
		Validate.notNull(group, "Group is no more!");

        removeProfileAccessesByGroup(group);

        //..delete..
        profileGroupDao.erase(group);
	}

    private void removeProfileAccessesByGroup(ProfileGroup group) {
        //..unset profiles accesses from this group..
        for (Profile profile : group.getProfiles()) {
            //..profile accesses can be from many groups..
            for (Access access : group.getAccesses()) {
                //..if profile has another group with such an access don't remove it..
                if (profile.getAccesses().contains(access)) {
                    ProfileGroupsFilter filter = new ProfileGroupsFilter()
                            .access(access)
                            .groupExclude(group)
                            .profile(profile);
                    if (profileGroupDao.find(filter).isEmpty()) {
                        //..this group is the only one that profile has its access..
                        profile.removeAccess(access);
                    }
                }
            }
        }
    }

}
