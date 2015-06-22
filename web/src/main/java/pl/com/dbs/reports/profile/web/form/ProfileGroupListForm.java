/**
 * 
 */
package pl.com.dbs.reports.profile.web.form;

import com.google.common.collect.Sets;
import pl.com.dbs.reports.access.domain.Access;
import pl.com.dbs.reports.profile.dao.ProfileGroupsFilter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Profiles groups form.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2015
 */
public class ProfileGroupListForm implements Serializable {
	private static final long serialVersionUID = 622395683103389275L;
	public static final String KEY = "profilegrouplistform";

    private List<Long> id;
	private String name;
    protected Set<Access> accesses = Sets.newHashSet();
    private Action action;

	private ProfileGroupsFilter filter = new ProfileGroupsFilter();

	public ProfileGroupListForm() {}

    public void reset() {
        this.action = null;
        this.id = new ArrayList<Long>();
    }

    public boolean anyIDselected() {
        return id!=null&&!id.isEmpty();
    }

    public List<Long> getId() {
        return id;
    }

    public Long[] getIdAsArray() {
        return id.toArray(new Long[]{});
    }

    public void setId(List<Long> id) {
        this.id = id;
    }

	public ProfileGroupsFilter getFilter() {
		return filter;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

    public Set<Access> getAccesses() {
        return accesses;
    }

    public void setAccesses(Set<Access> accesses) {
        this.accesses = accesses;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public enum Action {
        REMOVE;
    }

}
