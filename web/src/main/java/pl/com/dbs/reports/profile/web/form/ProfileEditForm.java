/**
 * 
 */
package pl.com.dbs.reports.profile.web.form;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

import pl.com.dbs.reports.access.domain.Access;
import pl.com.dbs.reports.authority.domain.Authority;
import pl.com.dbs.reports.profile.domain.Profile;
import pl.com.dbs.reports.profile.domain.ProfileGroup;
import pl.com.dbs.reports.profile.domain.ProfileModification;
import pl.com.dbs.reports.security.domain.SessionContext;
import pl.com.dbs.reports.support.web.file.FileMeta;

/**
 * Editing profile form.
 * Contains profile id.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2013
 */
public class ProfileEditForm extends ProfileNewForm implements ProfileModification, Serializable {
	private static final long serialVersionUID = -7023050729373559447L;
	
	public static final String KEY = "profileEditForm";
	private Long id;
	private boolean save;
	
	public ProfileEditForm() {
		super();
	}

	public void reset(Profile profile, List<ProfileGroup> groups) {
		if (profile==null) return;
		super.reset();

		id = profile.getId();
		global = profile.isGlobal();
		super.setAccesses(profile.getAccesses());
		super.setAuthorities(profile.getAuthorities());
        for (ProfileGroup group : groups) getGroups().add(group.getId());
		if (profile.hasAddress()) {
			setCity(profile.getAddress().getCity());
			setState(profile.getAddress().getState());
			setStreet(profile.getAddress().getStreet());
			setZipCode(profile.getAddress().getZipcode());
		}
		setEmail(profile.getEmail());
		if (profile.hasPhoto()) {
			try { 
				addPhoto(new FileMeta(profile.getPhoto().getPath(), 
									  profile.getPhoto().getContent())); 
			} catch (IOException e) {}
		}
		setFirstName(profile.getFirstName());
		setLastName(profile.getLastName());
		setLogin(profile.getLogin());
		//setPasswd(profile.getPasswd());
		setPhone(profile.getPhone());
		setAccepted(profile.isAccepted());
		
	}
	
	@Override
	public Long getId() {
		return id;
	}
	
	public boolean hasProfile() {
		return id!=null;
	}
	
	@Override
	public void setAccesses(Set<Access> accesses) {
		if (SessionContext.hasAnyRole(SessionContext.ROLE_ADMIN)) 
			this.accesses = accesses;
	}

	@Override
	public void setAuthorities(List<Authority> authorities) {
		if (SessionContext.hasAnyRole(SessionContext.ROLE_ADMIN))
			this.authorities = authorities;
	}

	public boolean saveNow() {
		return save;
	}

	public void setSave(boolean save) {
		this.save = save;
	}

}
