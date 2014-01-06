/**
 * 
 */
package pl.com.dbs.reports.profile.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import pl.com.dbs.reports.profile.domain.ProfileCreation.Address;
import pl.com.dbs.reports.support.db.domain.IEntity;

/**
 * Profile address data.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
@Entity
@Table(name = "tpr_address")
public class ProfileAddress implements IEntity {
	private static final long serialVersionUID = -3554578281684983709L;

	@Id
	@Column(name = "id")
	@SequenceGenerator(name = "sg_address", sequenceName = "spr_address", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sg_address")
	private Long id;	
	
	@Column(name = "street")
	private String street;
	
	@Column(name = "city")
	private String city;
	
	@Column(name = "state")
	private String state;
	
	@Column(name = "zipcode")
	private String zipcode;
	
    
	public ProfileAddress() {/*JPA*/}
	
	public ProfileAddress(Address address) {
		this.street = address.getStreet();
		this.city = address.getCity();
		this.state = address.getState();
		this.zipcode = address.getZipCode();
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

	public String getZipcode() {
		return zipcode;
	}
	
	
	
}
