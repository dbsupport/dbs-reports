/**
 * 
 */
package pl.com.dbs.reports.profile.dao;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import pl.com.dbs.reports.access.domain.Access;
import pl.com.dbs.reports.profile.domain.Profile;
import pl.com.dbs.reports.profile.domain.ProfileGroup;
import pl.com.dbs.reports.support.db.dao.AFilter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Profile groups filter.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2015
 */
public class ProfileGroupsFilter extends AFilter<ProfileGroup> {
	private static final long serialVersionUID = -8697585651148607522L;

	private String name;
    private Set<Long> accesses = Sets.newHashSet();
    private Long pid;
    private Long gidn;
    private Set<Long> groups = Sets.newHashSet();

    public ProfileGroupsFilter() {}


	public ProfileGroupsFilter name(String name) {
		this.name = name;
        return this;
	}

    public ProfileGroupsFilter accesses(Set<Access> accesses) {
        this.accesses = Sets.newHashSet();
        for (Access access : accesses)
            this.accesses.add(access.getId());
        return this;
    }

    public ProfileGroupsFilter access(Access access) {
        if (this.accesses==null) this.accesses = Sets.newHashSet();
        this.accesses.add(access.getId());
        return this;
    }


    public ProfileGroupsFilter profile(Profile profile) {
        this.pid = profile!=null?profile.getId():null;
        return this;
    }

    public ProfileGroupsFilter pid(long pid) {
        this.pid = pid;
        return this;
    }

    public ProfileGroupsFilter gidn(ProfileGroup group) {
        this.gidn = group!=null?group.getId():null;
        return this;
    }

    public ProfileGroupsFilter groups(Set<Long> groups) {
        this.groups = groups;
        return this;
    }
	
	public String getName() {
		return name;
	}

    public Set<Long> getAccesses() {
        return accesses;
    }

    public boolean hasAccesses() {
        return accesses!=null&&!accesses.isEmpty();
    }

    public boolean hasGroups() {
        return groups!=null&&!groups.isEmpty();
    }

    public Long getPid() {
        return pid;
    }

    public Long getGidn() {
        return gidn;
    }

    public Set<Long> getGroups() {
        return groups;
    }
}
