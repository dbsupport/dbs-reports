/**
 * 
 */
package pl.com.dbs.reports.profile.web.form;

import com.google.common.collect.Sets;
import pl.com.dbs.reports.access.domain.Access;
import pl.com.dbs.reports.profile.domain.ProfileGroupCreation;
import pl.com.dbs.reports.support.web.form.AForm;

import java.io.Serializable;
import java.util.Set;

/**
 * New profile group form.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2015
 */
public class ProfileGroupNewForm extends AForm implements ProfileGroupCreation, Serializable {
	private static final long serialVersionUID = -3562062478745725527L;
	public static final String KEY = "profilegroupnewform";

    private String name;
    private String description;
    protected Set<Access> accesses = Sets.newHashSet();

	public ProfileGroupNewForm() { super(); }
	
	public void reset() {
		super.reset();
        this.name=null;
        this.description=null;
		this.accesses = Sets.newHashSet();
	}


    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
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
