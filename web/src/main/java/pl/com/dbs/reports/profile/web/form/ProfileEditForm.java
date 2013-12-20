/**
 * 
 */
package pl.com.dbs.reports.profile.web.form;

import java.io.IOException;

import pl.com.dbs.reports.profile.domain.Profile;
import pl.com.dbs.reports.profile.domain.ProfileModification;
import pl.com.dbs.reports.support.web.file.FileMeta;

/**
 * TODO
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
public class ProfileEditForm extends ProfileNewForm implements ProfileModification {
	public static final String KEY = "profileEditForm";
	private Long id;
	
	public ProfileEditForm() {
		super();
	}

	public void reset(Profile profile) {
		if (profile==null) return;
		
		id = profile.getId();
		setAccesses(profile.getAccesses());
		setAuthorities(profile.getAuthorities());
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
		setFirstName(profile.getName());
		setLastName(profile.getName());
		setLogin(profile.getLogin());
		setPhone(profile.getPhone());
	}
	
	@Override
	public Long getId() {
		return id;
	}
	
	public boolean hasProfile() {
		return id!=null;
	}

}
