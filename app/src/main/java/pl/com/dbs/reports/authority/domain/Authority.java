/**
 * 
 */
package pl.com.dbs.reports.profile.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;

import pl.com.dbs.reports.support.db.domain.IEntity;

/**
 * TODO
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
@Entity
@Table(name = "tpr_authority")
public class ProfileAuthority implements IEntity, GrantedAuthority {
	private static final long serialVersionUID = 4370570750104490857L;
	
	@Id
	@Column(name = "id")
	private Long id;		
	
	@Column(name = "name")	
	private String name;
	
	public ProfileAuthority() {/* JPA */}
	
	
	public Long getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}	

	@Override
	public String getAuthority() {
		return name;
	}
}