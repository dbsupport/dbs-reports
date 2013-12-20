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

import pl.com.dbs.reports.api.security.SecurityAuthority;
import pl.com.dbs.reports.support.db.domain.AEntity;

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
	
	@OneToOne(fetch=FetchType.EAGER, orphanRemoval=true)
	@JoinColumn(name="photo_id")	
	private ProfilePhoto photo;
	

	/**
	 * Acces to some functionality (i.e. reports).
	 */
	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(name="tpr_profile_access",
		      joinColumns={@JoinColumn(name="profile_id", referencedColumnName="id")},
		      inverseJoinColumns={@JoinColumn(name="access_id", referencedColumnName="id")})	
	private List<ProfileAccess> accesses = new ArrayList<ProfileAccess>();
	
	/**
	 * Access to part of application (i.e. to import patterns).
	 */
    @ManyToMany
    @JoinTable(name = "tpr_profile_authority", 
  		joinColumns={@JoinColumn(name="profile_id", referencedColumnName="id")},
  		inverseJoinColumns={@JoinColumn(name="authority_id", referencedColumnName="id")})
	private List<ProfileAuthority> authorities = new ArrayList<ProfileAuthority>();	
	
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
	
	public void addAccess(ProfileAccess access) {
		Validate.notNull(access, "Access cant be null!");
		if (!accesses.contains(access))
			this.accesses.add(access);
	}
	
	public void removeAccess(ProfileAccess access) {
		Validate.notNull(access, "Access cant be null!");
		if (accesses.contains(access))
			this.accesses.remove(access);
	}
	
	
	public void addAuthority(ProfileAuthority authority) {
		Validate.notNull(authority, "Authority cant be null!");
		if (!authorities.contains(authority))
			this.authorities.add(authority);
	}
	
	public void removeAuthority(ProfileAuthority authority) {
		Validate.notNull(authority, "Authority cant be null!");
		if (authorities.contains(authority))
			this.authorities.remove(authority);
	}

	public void addAddress(ProfileAddress address) {
		this.address = address;
	}
	
	public void addPhoto(ProfilePhoto photo) {
		this.photo = photo;
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

	public List<ProfileAuthority> getAuthorities() {
		return authorities;
	}
	
	public List<String> getAuthoritiesAsString() {
		List<String> result = new ArrayList<String>();
		for (ProfileAuthority authority : authorities) result.add(authority.getName()); 
		return result;
	}

	public List<SecurityAuthority> getHrauthorities() {
		return hrauthorities;
	}
	
	public List<ProfileAccess> getAccesses() {
		return accesses;
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
	
}
