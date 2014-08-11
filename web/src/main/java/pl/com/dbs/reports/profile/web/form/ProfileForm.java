/**
 * 
 */
package pl.com.dbs.reports.profile.web.form;

import java.io.Serializable;

import pl.com.dbs.reports.profile.domain.Profile;

/**
 * Profile form.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
public class ProfileForm implements Serializable {
	private static final long serialVersionUID = 4747270807190829517L;
	public static final String KEY = "profileForm";
	private Long id;
	private String note;
	
	public ProfileForm() {}
	
	public void reset(Profile profile) {
		this.id = profile.getId();
		this.note = profile.getNote()!=null?profile.getNote().getContent():"";
	}

	public Long getId() {
		return id;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
	
}
