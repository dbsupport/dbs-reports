/**
 * 
 */
package pl.com.dbs.reports.profile.web.form;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import pl.com.dbs.reports.profile.domain.ProfileCreation;
import pl.com.dbs.reports.support.web.file.FileService;
import pl.com.dbs.reports.support.web.form.AForm;

/**
 * TODO
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
public class ProfileNewForm extends AForm implements ProfileCreation {
	public static final String KEY = "profileNewForm";
	
	private String login;
	private String passwd;
	private String firstName;
	private String lastName;
	private String email;
	private String phone;
	
	private String street;
	private String city;
	private String state;
	private String zipCode;
	
	private List<String> accesses = new ArrayList<String>();
	
	private MultipartFile  file;
	
	public ProfileNewForm() {}
	
	public void reset() {
		super.reset();
		this.login = null;
		this.passwd = null;
		this.firstName = null;
		this.lastName = null;
		this.email = null;
		this.phone = null;
		this.street = null;
		this.city = null;
		this.state = null;
		this.zipCode = null;
		this.file = null;
		this.accesses = new ArrayList<String>();
	}
	

	@Override	
	public String getLogin() {
		return login;
	}

	@Override
	public String getPasswd() {
		return passwd;
	}

	@Override
	public String getFirstName() {
		return firstName;
	}

	@Override
	public String getLastName() {
		return lastName;
	}

	@Override
	public String getEmail() {
		return email;
	}

	@Override
	public String getPhone() {
		return phone;
	}
	
	@Override
	public Address getAddress() {
		return new Address() {
			@Override
			public String getStreet() {
				return street;
			}
			@Override
			public String getCity() {
				return city;
			}
			@Override
			public String getState() {
				return state;
			}
			@Override
			public String getZipCode() {
				return zipCode;
			}
		};
	}

	@Override
	public File getPhoto()  {
		try {
			return FileService.multipartToFile(getFile());
		} catch (IOException e) {}
		return null;
	}
	
	

	public String getStreet() {
		return street;
	}

	public String getCity() {
		return city;
	}

	public String getState() {
		return state;
	}

	public String getZipCode() {
		return zipCode;
	}

	public MultipartFile getFile() {
		return file;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setState(String state) {
		this.state = state;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}

	public List<String> getAccesses() {
		return accesses;
	}

	public void setAccesses(List<String> accesses) {
		this.accesses = accesses;
	}


	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}


}
