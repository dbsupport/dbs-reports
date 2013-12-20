/**
 * 
 */
package pl.com.dbs.reports.profile.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;

import pl.com.dbs.reports.access.domain.Access;
import pl.com.dbs.reports.api.security.SecurityAuthority;
import pl.com.dbs.reports.authority.domain.Authority;
import pl.com.dbs.reports.support.db.domain.AEntity;
import pl.com.dbs.reports.support.utils.separator.Separator;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

/**
 * TODO
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
@Entity
@Table(name = "tpr_profile")
public class Profile extends AEntity {
	private static final long serialVersionUID = 301060274149701349L;
	public static final String PARAMETER_UUID = "IN_NUDOSS";
	
	@Id
	@Column(name = "id")
	@SequenceGenerator(name = "sg_profile", sequenceName = "spr_profile", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sg_profile")
	private Long id;
	
	@Column(name = "uuid")
	private String uuid;
	
	@Column(name = "name")
	private String name;
	
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
	

	/**
	 * Acces to some functionality (i.e. reports).
	 */
	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(name="tpr_access",
		      joinColumns={@JoinColumn(name="profile_id", referencedColumnName="id")},
		      inverseJoinColumns={@JoinColumn(name="access_id", referencedColumnName="id")})	
	private List<Access> accesses = new ArrayList<Access>();
	
	/**
	 * Access to part of application (i.e. to import patterns).
	 */
    @ManyToMany
    @JoinTable(name = "tpr_authority", 
  		joinColumns={@JoinColumn(name="profile_id", referencedColumnName="id")},
  		inverseJoinColumns={@JoinColumn(name="authority_id", referencedColumnName="id")})
	private List<Authority> authorities = new ArrayList<Authority>();	
	
    @Transient
    private List<SecurityAuthority> hrauthorities = new ArrayList<SecurityAuthority>();
    
	public Profile() {/*JPA*/}
	
	public Profile(ProfileCreation form) {
		this.name = form.getFirstName()+" "+form.getLastName();
		this.login = form.getLogin();
		this.passwd = form.getPasswd();
		this.email = form.getEmail();
		this.phone = form.getPhone();
	}
	
	public void modify(ProfileModification form) {
		this.email = form.getEmail();
		this.name = form.getFirstName() +" "+form.getLastName();
		this.passwd = !StringUtils.isBlank(form.getPasswd())?form.getPasswd():this.passwd;
		this.phone = form.getPhone();
	}
	
	public void addAccess(Access access) {
		Validate.notNull(access, "Access cant be null!");
		if (!accesses.contains(access))
			this.accesses.add(access);
	}
	
	public void removeAccess(Access access) {
		Validate.notNull(access, "Access cant be null!");
		if (accesses.contains(access))
			this.accesses.remove(access);
	}
	
	public void removeAccesses() {
		this.accesses = new ArrayList<Access>();
	}
	
	public void addAuthority(Authority authority) {
		Validate.notNull(authority, "Authority cant be null!");
		if (!authorities.contains(authority))
			this.authorities.add(authority);
	}
	
	public void removeAuthority(Authority authority) {
		Validate.notNull(authority, "Authority cant be null!");
		if (authorities.contains(authority))
			this.authorities.remove(authority);
	}
	
	public void removeAuthorities() {
		this.authorities = new ArrayList<Authority>();
	}

	public void addAddress(ProfileAddress address) {
		this.address = address;
	}
	
	public void removeAddress() {
		this.address = null;
	}
	
	public void addPhoto(ProfilePhoto photo) {
		this.photo = photo;
	}
	
	public void removePhoto() {
		this.photo = null;
	}	
	
	public void initHRAuthorities(List<SecurityAuthority> hrauthorities) {
		this.hrauthorities = !Iterables.isEmpty(hrauthorities)?hrauthorities:new ArrayList<SecurityAuthority>();
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

	public String getName() {
		return name;
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
	
	public List<SecurityAuthority> getHrauthorities() {
		return hrauthorities;
	}
	
	public List<Access> getAccesses() {
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
	
	public String getHrauthoritiesAsString() {
		StringBuffer sb = new StringBuffer();
		Separator s = new Separator(", ");		
		for (SecurityAuthority authority : hrauthorities) sb.append(s).append(authority.getName()); 
		return sb.toString();
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
	
}
