/**
 * 
 */
package pl.com.dbs.reports.activedirectory.dao;

import java.util.Date;

import pl.com.dbs.reports.api.activedirectory.ClientActiveDirectoryProfile;

/**
 * AD profile.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2014
 */
public class ActiveDirectoryProfile {
	/**
	 * Numer ewidencyjny
	 */
	private String number;
	
	/**
	 * Kod lokalizacji
	 */
	private String locationCode;
	
	/**
	 * Nazwa lokalizacji
	 */
	private String locationName;
	
	/**
	 * Imie
	 */
	private String firstName;

	/**
	 * Nazwisko
	 */
	private String lastName;
	
	/**
	 * Kod jednostki organizacyjnej
	 */
	private String unitCode;

	/**
	 * Nazwa jednostki organizacyjnej
	 */
	private String unitName;

	/**
	 * Data zatrudnienia
	 */
	private Date employmentDate;
	
	/**
	 * Data zwolnienia
	 */
	private Date dismissalDate;
	
	public ActiveDirectoryProfile(ClientActiveDirectoryProfile profile) {
		this.dismissalDate = profile.getDismissalDate();
		this.employmentDate = profile.getEmploymentDate();
		this.firstName = profile.getFirstName();
		this.lastName = profile.getLastName();
		this.locationCode = profile.getLocationCode();
		this.locationName = profile.getLocationName();
		this.number = profile.getNumber();
		this.unitCode = profile.getUnitCode();
		this.unitName = profile.getUnitName();
	}

	public String getNumber() {
		return number;
	}



	public String getLocationCode() {
		return locationCode;
	}



	public String getLocationName() {
		return locationName;
	}



	public String getFirstName() {
		return firstName;
	}



	public String getLastName() {
		return lastName;
	}



	public String getUnitCode() {
		return unitCode;
	}



	public String getUnitName() {
		return unitName;
	}



	public Date getEmploymentDate() {
		return employmentDate;
	}



	public Date getDismissalDate() {
		return dismissalDate;
	}


}
