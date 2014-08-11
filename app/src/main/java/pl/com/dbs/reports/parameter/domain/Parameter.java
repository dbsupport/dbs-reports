/**
 * 
 */
package pl.com.dbs.reports.parameter.domain;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Lob;
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
	
	@Lob
	@Column(name = "value")
	@Basic(fetch = FetchType.LAZY)
	private byte[] value;
	
	@Column(name = "description")
	private String desc;
	
	@Column(name = "type")
	@Enumerated(EnumType.STRING)
	private ParameterType type;
	
	@Column(name = "scope")
	@Enumerated(EnumType.STRING)
	private ParameterScope scope;
	
	public Parameter() {}

	public String getKey() {
		return key;
	}

	public ParameterType getType() {
		return type;
	}

	public ParameterScope getScope() {
		return scope;
	}

	public String getDesc() {
		return desc;
	}
	
	public void changeDesc(String desc) {
		this.desc = desc; 
	}	

	public void changeValue(byte[] value) {
		this.value = value!=null&&value.length>0?value:null; 
	}	
	
	public byte[] getValue() {
		return value;
	}

	public Integer getValueAsInteger() {
		try {
			return Integer.valueOf(getValueAsString());
		} catch (NumberFormatException e) {}
		return null;
	}
	
	public String getValueAsString() {
		return value!=null?new String(value):"";
	}
	
	public boolean isSame(final byte[] value) {
		String str = getValueAsString();
		String val = value!=null?new String(value):"";
		return ((StringUtils.isBlank(str)&&StringUtils.isBlank(val))
				||(StringUtils.isNotBlank(str)&&str.equals(val)));
	}

}
