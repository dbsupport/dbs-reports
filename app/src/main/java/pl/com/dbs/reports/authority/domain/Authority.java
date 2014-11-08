/**
 * 
 */
package pl.com.dbs.reports.authority.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.springframework.security.core.GrantedAuthority;

import pl.com.dbs.reports.support.db.domain.IEntity;

/**
 * Application access roles.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2013
 */
@Entity
@Table(name = "tau_authority")
public class Authority implements IEntity, GrantedAuthority {
	private static final long serialVersionUID = 4370570750104490857L;
	public static final String USER = "User";
	public static final String MANAGEMENT = "Management";
	public static final String ADMIN = "Admin";
	
	@Id
	@Column(name = "id")
	private Long id;		
	
	@Column(name = "name")	
	private String name;
	
	public Authority() {/* JPA */}
	
	
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
	
	@Override
	public boolean equals(Object obj) {
		 if (obj == null) return false;
		 if (obj == this) return true;
		 if (!(obj instanceof Authority)) return false;
		 
		 return new EqualsBuilder().
		            // if deriving: appendSuper(super.equals(obj)).
		            append(id, ((Authority)obj).id).
		            isEquals();		 
	}
	
	@Override
	public int hashCode() {
		 return new HashCodeBuilder(17, 31).
				 // if deriving: appendSuper(super.hashCode()).
		            append(name).
		            append(id).
		            toHashCode();
	}	
	
}