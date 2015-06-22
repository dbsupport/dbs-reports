/**
 * 
 */
package pl.com.dbs.reports.profile.domain;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import org.apache.commons.lang.Validate;
import pl.com.dbs.reports.access.domain.Access;
import pl.com.dbs.reports.support.db.domain.AEntity;
import pl.com.dbs.reports.support.utils.separator.Separator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Local profile group.
 * Group has some attributes that can be applied to its profiles while they change.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2015
 */
@Entity
@Table(name = "tpr_group")
public class ProfileGroup extends AEntity implements Serializable {
	private static final long serialVersionUID = 301060274111701349L;
    public static final java.util.regex.Pattern NAME_PATTERN = java.util.regex.Pattern.compile("^[a-zA-z0-9\\-_ ]+$",  java.util.regex.Pattern.CASE_INSENSITIVE);

	@Id
	@Column(name = "id")
	@SequenceGenerator(name = "sg_profile_group", sequenceName = "spr_group", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sg_profile_group")
	private Long id;

    @Column(name = "name")
    private String name;

	@Column(name = "description")
	private String description;

    /**
     * Profiles that belongs to group.
     */
    @ManyToMany(fetch=FetchType.LAZY)
    @JoinTable(name="tpr_group_profile",
            joinColumns={@JoinColumn(name="group_id", referencedColumnName="id")},
            inverseJoinColumns={@JoinColumn(name="profile_id", referencedColumnName="id")})
    private List<Profile> profiles = new ArrayList<Profile>();

	/**
	 * Accesses to some functionality (i.e. reports).
	 */
	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(name="tpr_group_access",
		      joinColumns={@JoinColumn(name="group_id", referencedColumnName="id")},
		      inverseJoinColumns={@JoinColumn(name="access_id", referencedColumnName="id")})
	private Set<Access> accesses = Sets.newHashSet();

	public ProfileGroup() {/*JPA*/}

	public ProfileGroup(ProfileGroupCreation form) {
        this.name = form.getName();
		this.description = form.getDescription();
        this.accesses = form.getAccesses();
	}

	public ProfileGroup modify(ProfileGroupEdit form) {
        this.description = form.getDescription();
        this.accesses = form.getAccesses();
		return this;
	}
	
	public ProfileGroup addAccess(Access access) {
		Validate.notNull(access, "Access cant be null!");
		if (!accesses.contains(access))
			this.accesses.add(access);
		return this;
	}

	public ProfileGroup removeAccess(Access access) {
		Validate.notNull(access, "Access cant be null!");
		if (accesses.contains(access))
			this.accesses.remove(access);
		return this;
	}
	
	public ProfileGroup removeAccesses() {
		this.accesses = Sets.newHashSet();
		return this;
	}

    public ProfileGroup addProfile(Profile profile) {
        Validate.notNull(profile, "Profile cant be null!");
        if (!profiles.contains(profile))
            this.profiles.add(profile);
        return this;
    }

    public ProfileGroup removeProfile(Profile profile) {
        Validate.notNull(profile, "Profile cant be null!");
        if (profiles.contains(profile))
            this.profiles.remove(profile);
        return this;
    }

    public ProfileGroup removeProfiles() {
        this.profiles = new ArrayList<Profile>();
        return this;
    }
	
	public Long getId() {
		return id;
	}

	public String getDescription() {
		return description;
	}

    public String getName() {
        return name;
    }


    public List<Profile> getProfiles() {
		return profiles;
	}

    public Set<Access> getAccesses() {
        return accesses;
    }

	public boolean hasAccess(final String access) {
		return Iterables.find(accesses, new Predicate<Access>() {
			@Override
			public boolean apply(Access input) {
				return input.isAlike(access);
			}
		}, null)!=null;
	}	
	
	public String getAccessesAsString() {
		StringBuffer sb = new StringBuffer();
		Separator s = new Separator(", ");
		for (Access access : accesses) sb.append(s).append(access.getName()); 
		return sb.toString();
	}
	
}
