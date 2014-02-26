/**
 * 
 */
package pl.com.dbs.reports.access.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import pl.com.dbs.reports.support.db.domain.AEntity;

/**
 * Access entity.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
@Entity
@Table(name = "tac_access")
public class Access extends AEntity {
	private static final long serialVersionUID = 60098567026973914L;
	public static final java.util.regex.Pattern NAME_PATTERN = java.util.regex.Pattern.compile("^[a-zA-z0-9\\-_]+$",  java.util.regex.Pattern.CASE_INSENSITIVE);

	@Id
	@Column(name = "id")
	@SequenceGenerator(name = "sg_access", sequenceName = "sac_access", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sg_access")
	private Long id;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "description")
	private String description;
	
	public Access() {}
	
	public Access(AccessCreation form) {
		this.name = form.getName();
		this.description = form.getDescription();
	}
	
	public void modify(AccessModification form) {
		this.name = form.getName();
		this.description = form.getDescription();
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}
	
	public boolean isAlike(String access) {
		return this.name.equals(access);
	}
	
	@Override
	public boolean equals(Object obj) {
		 if (obj == null) return false;
		 if (obj == this) return true;
		 if (!(obj instanceof Access)) return false;
		 
		 return new EqualsBuilder().
		            // if deriving: appendSuper(super.equals(obj)).
		            append(id, ((Access)obj).id).
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
