/**
 * 
 */
package pl.com.dbs.reports.profile.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import pl.com.dbs.reports.support.db.domain.AEntity;

/**
 * TODO
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
@Entity
@Table(name = "tpr_access")
public class ProfileAccess extends AEntity {
	private static final long serialVersionUID = 60098567026973914L;

	@Id
	@Column(name = "id")	
	private Long id;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "description")
	private String description;
	
	public ProfileAccess() {}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}
	
	
	
}
