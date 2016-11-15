/**
 * 
 */
package pl.com.dbs.reports.profile.web.form;

import com.google.common.collect.Sets;
import pl.com.dbs.reports.access.domain.Access;
import pl.com.dbs.reports.profile.domain.ProfileGroup;
import pl.com.dbs.reports.profile.domain.ProfileGroupEdit;
import pl.com.dbs.reports.support.web.form.AForm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Edit profile group form.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2015
 */
public class ProfileGroupEditForm extends AForm implements ProfileGroupEdit, Serializable {
	private static final long serialVersionUID = -3562062478745725678L;
	public static final String KEY = "profilegroupeditform";

    private Long id;
    private String description;
    protected Set<Access> accesses = Sets.newHashSet();

	public ProfileGroupEditForm() { super(); }
	
	public void reset(ProfileGroup group) {
		super.reset();
        this.id = group.getId();
        this.description = group.getDescription();
		this.accesses = group.getAccesses();
	}

    public Long getId() {
        return id;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    @Override
    public Set<Access> getAccesses() {
        return accesses;
    }

    public void setAccesses(Set<Access> accesses) {
        this.accesses = accesses;
    }
}
