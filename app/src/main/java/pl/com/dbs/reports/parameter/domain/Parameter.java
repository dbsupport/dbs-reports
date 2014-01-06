/**
 * 
 */
package pl.com.dbs.reports.parameter.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import pl.com.dbs.reports.support.db.domain.IEntity;

/**
 * Parameters.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
@Entity
@Table(name = "tpa_parameter")
public class Parameter implements IEntity {
	private static final long serialVersionUID = -2257699676299341231L;
	
	@Id
	@Column(name = "id")
	private Long id;
	
	@Column(name = "key")
	private String key;
	
	@Column(name = "value")
	private String value;
	
	@Column(name = "type")
	@Enumerated(EnumType.STRING)
	private ParameterType type;
	
	public Parameter() {}

	public String getKey() {
		return key;
	}

	public ParameterType getType() {
		return type;
	}

	public void changeValue(String value) {
		this.value = value; 
	}	
	
	public Integer getValueAsInteger() {
		try {
			return Integer.valueOf(value);
		} catch (NumberFormatException e) {}
		return null;
	}
	
	@Override
	public String toString() {
		return StringUtils.isNotBlank(value)?value:null;
	}
	
	public boolean isSame(final String value) {
		return 
		((StringUtils.isBlank(this.value)&&StringUtils.isBlank(value))
		||(StringUtils.isNotBlank(this.value)&&this.value.equals(value)));
	}

}
