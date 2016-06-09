/**
 * 
 */
package pl.com.dbs.reports.profile.dao;

import com.google.common.collect.Sets;
import pl.com.dbs.reports.access.domain.Access;
import pl.com.dbs.reports.profile.domain.Profile;
import pl.com.dbs.reports.profile.domain.ProfileGroup;
import pl.com.dbs.reports.profile.domain.ProfileGroup_;
import pl.com.dbs.reports.support.db.dao.AFilter;

import java.util.Set;

/**
 * Profile groupsInclude filter.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2015
 */
public class ProfileGroupsFilter extends AFilter<ProfileGroup> {
	private static final long serialVersionUID = -8697585651148607522L;

	private String name;
    private Long pid;
    private Set<Long> accesses = Sets.newHashSet();
    private Set<Long> groupsExclude = Sets.newHashSet();
    private Set<Long> groupsInclude = Sets.newHashSet();

    public ProfileGroupsFilter() {
        getSorter().add(ProfileGroup_.name.getName(), true);
    }


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

    public ProfileGroupsFilter groupsExclude(Set<Long> gids) {
        this.groupsExclude = gids;
        return this;
    }

    public ProfileGroupsFilter groupExclude(ProfileGroup group) {
        this.groupsExclude.add(group.getId());
        return this;
    }

    public ProfileGroupsFilter groupsInclude(Set<Long> groups) {
        this.groupsInclude = groups;
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

    public boolean hasGroupsInclude() {
        return groupsInclude !=null&&!groupsInclude.isEmpty();
    }

    public Long getPid() {
        return pid;
    }

    public Set<Long> getGroupsExclude() {
        return groupsExclude;
    }

    public boolean hasGroupsExclude() {
        return groupsExclude!=null&&!groupsExclude.isEmpty();
    }

    public Set<Long> getGroupsInclude() {
        return groupsInclude;
    }
}
