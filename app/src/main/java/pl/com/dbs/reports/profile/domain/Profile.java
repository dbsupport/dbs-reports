/**
 * 
 */
package pl.com.dbs.reports.profile.domain;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import pl.com.dbs.reports.access.domain.Access;
import pl.com.dbs.reports.api.profile.ClientProfileAuthority;
import pl.com.dbs.reports.authority.domain.Authority;
import pl.com.dbs.reports.support.db.domain.AEntity;
import pl.com.dbs.reports.support.utils.separator.Separator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Local profile.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2013
 */
@Entity
@Table(name = "tpr_profile")
public class Profile extends AEntity implements Serializable {
	private static final long serialVersionUID = 301060274149701349L;
	public static final String PARAMETER_PROFILE = "IN_PROFILE";
	public static final String PARAMETER_USER = "IN_USER";
	
	@Id
	@Column(name = "id")
	@SequenceGenerator(name = "sg_profile", sequenceName = "spr_profile", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sg_profile")
	private Long id;
	
	@Column(name = "uuid")
	private String uuid;
	
	@Column(name = "active")
	private boolean active;
	
	@Column(name = "accepted")
	private boolean accepted;
	
	@Column(name = "firstname")
	private String firstname;
	
	@Column(name = "lastname")
	private String lastname;
	
	@Column(name = "description")
	private String description;
	
	@Column(name = "login")
	private String login;
	
	@Column(name = "passwd")
	private String passwd;
	
	@Column(name = "email")
	private String email;
	
	@Column(name = "phone")
	private String phone;
	
	@OneToOne(fetch=FetchType.EAGER, orphanRemoval=true)
	@JoinColumn(name="address_id")	
	private ProfileAddress address;
	
	@OneToOne(fetch=FetchType.LAZY, orphanRemoval=true)
	@JoinColumn(name="photo_id")	
	private ProfilePhoto photo;
	
	@OneToOne(fetch=FetchType.LAZY, orphanRemoval=true)
	@JoinColumn(name="note_id")	
	private ProfileNote note;	
	

	/**
	 * Acces to some functionality (i.e. reports).
	 */
	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(name="tpr_access",
		      joinColumns={@JoinColumn(name="profile_id", referencedColumnName="id")},
		      inverseJoinColumns={@JoinColumn(name="access_id", referencedColumnName="id")})	
	private Set<Access> accesses = Sets.newHashSet();

    /**
	 * Access to part of application (i.e. to import patterns).
	 */
    @ManyToMany
    @JoinTable(name = "tpr_authority", 
  		joinColumns={@JoinColumn(name="profile_id", referencedColumnName="id")},
  		inverseJoinColumns={@JoinColumn(name="authority_id", referencedColumnName="id")})
	private List<Authority> authorities = new ArrayList<Authority>();	
	
    @Transient
    private List<ClientProfileAuthority> hrauthorities = new ArrayList<ClientProfileAuthority>();
    
	public Profile() {/*JPA*/}
	
	public Profile(ProfileCreation form) {
		this.firstname = form.getFirstName();
		this.lastname = form.getLastName();
		this.login = form.getLogin();
		this.passwd = form.getPasswd();
		this.email = form.getEmail();
		this.phone = form.getPhone();
		this.accepted = form.isAccepted();
		this.active = true;
		if (form instanceof ProfileGlobalCreation) {
			this.uuid = ((ProfileGlobalCreation)form).getUuid();
			this.description = ((ProfileGlobalCreation)form).getDescription();
		}
	}
	
	public Profile modify(ProfileModification form) {
		this.email = form.getEmail();
		this.firstname = form.getFirstName();
		this.lastname = form.getLastName();
		this.passwd = !StringUtils.isBlank(form.getPasswd())?form.getPasswd():this.passwd;
		this.phone = form.getPhone();
		this.accepted = form.isAccepted();
		return this;
	}
	
	public Profile modify(String desc) {
		this.description = desc;
		return this;
	}
	
	public Profile deactivate() {
		this.active = false;
		return this;
	}
	
	public Profile accept() {
		this.accepted = true;
		return this;
	}
	
	public Profile unaccept() {
		this.accepted = false;
		return this;
	}
	
	public Profile addNote(ProfileNote note) {
		this.note = note;
		return this;
	}
	
	public Profile addAccess(Access access) {
		Validate.notNull(access, "Access cant be null!");
        this.accesses.add(access);
		return this;
	}
	
	public Profile removeAccess(Access access) {
		Validate.notNull(access, "Access cant be null!");
		if (accesses.contains(access))
			this.accesses.remove(access);
		return this;
	}
	
	public Profile removeAccesses() {
		this.accesses = Sets.newHashSet();
		return this;
	}
	
	public Profile addAuthority(Authority authority) {
		Validate.notNull(authority, "Authority cant be null!");
		if (!authorities.contains(authority))
			this.authorities.add(authority);
		return this;
	}
	
	public Profile removeAuthority(Authority authority) {
		Validate.notNull(authority, "Authority cant be null!");
		if (authorities.contains(authority))
			this.authorities.remove(authority);
		return this;
	}
	
	public Profile removeAuthorities() {
		this.authorities = new ArrayList<Authority>();
		return this;
	}

	public Profile addAddress(ProfileAddress address) {
		this.address = address;
		return this;
	}
	
	public Profile removeAddress() {
		this.address = null;
		return this;
	}
	
	public Profile addPhoto(ProfilePhoto photo) {
		this.photo = photo;
		return this;
	}
	
	public Profile removePhoto() {
		this.photo = null;
		return this;
	}	
	
	public Profile initHRAuthorities(List<ClientProfileAuthority> hrauthorities) {
		this.hrauthorities = !Iterables.isEmpty(hrauthorities)?hrauthorities:new ArrayList<ClientProfileAuthority>();
		return this;
	}
	
	public boolean isGlobal() {
		return !StringUtils.isBlank(uuid);
	}
	
	public boolean hasAddress() {
		return getAddress()!=null;
	}
	
	public boolean hasPhoto() {
		return getPhoto()!=null;
	}	
	

	public Long getId() {
		return id;
	}

	public String getUuid() {
		return uuid;
	}
	
	public boolean isAccepted() {
		return accepted;
	}

	public String getFirstName() {
		return firstname;
	}
	
	public String getName() {
		return firstname+" "+lastname;
	}

	public String getLastName() {
		return lastname;
	}

	public String getDescription() {
		return description;
	}

	public String getLogin() {
		return login;
	}

	public String getPasswd() {
		return passwd;
	}

	public List<Authority> getAuthorities() {
		return authorities;
	}

	public List<ClientProfileAuthority> getClientAuthorities() {
		return hrauthorities;
	}
	
	/**
	 * First HR authority from list (sic!)
	 */
	private ClientProfileAuthority getClientAuthority() {
		return hasClientAuthorities()?hrauthorities.get(0):null;
	}
	
	private boolean hasClientAuthorities() {
		return hrauthorities!=null&&!hrauthorities.isEmpty();
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
	
	public String getAuthoritiesAsString() {
		StringBuffer sb = new StringBuffer();
		Separator s = new Separator(", ");		
		for (Authority authority : authorities) sb.append(s).append(authority.getName()); 
		return sb.toString();
	}	
	
	public String getClientAuthoritiesAsString() {
		StringBuffer sb = new StringBuffer();
		Separator s = new Separator(", ");		
		for (ClientProfileAuthority authority : hrauthorities) sb.append(s).append(authority.getName()); 
		return sb.toString();
	}
	
	/**
	 * If profile is global and has ClientAuthorities..
	 * Otherwise empty string;
	 */
	public String getClientAuthorityMetaData() {
		return isGlobal()&&hasClientAuthorities()?
				getClientAuthority().getMetaData():"";
	}

	public String getEmail() {
		return email;
	}


	public String getPhone() {
		return phone;
	}


	public ProfileAddress getAddress() {
		return address;
	}

	public ProfilePhoto getPhoto() {
		return photo;
	}

	public ProfileNote getNote() {
		return note;
	}
	
	public boolean isSomeNote() {
		return this.note!=null&&!this.note.isBlank();
	}
	
	public boolean isActive() {
		return active;
	}
	
	public boolean isSame(final Profile profile) {
		return profile!=null&&this.id!=null&&profile.getId().equals(this.id);
	}
	
	public boolean isSame(final Long id) {
		return id!=null&&this.id!=null&&id.equals(this.id);
	}	
	
}
