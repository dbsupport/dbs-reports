/**
 * 
 */
package pl.com.dbs.reports.support.web.form.option;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlValue;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.eclipse.persistence.oxm.annotations.XmlDiscriminatorValue;

/**
 * Available field value.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2014
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlDiscriminatorValue("option")
public class FieldOption implements Serializable {
	private static final long serialVersionUID = -4566295727688032403L;
	
	@XmlAttribute(name="value", required = false)
	private String value;	
	@XmlValue
	private String label;
	
	@XmlTransient
	private boolean checked = false;
	
	public FieldOption() {}

	public FieldOption(String value) {
		Validate.notNull(value, "Value is no more!");
		this.value = value;
	}
	
	public FieldOption(String value, String label) {
		this(value);
		this.label = label;
	}
	
	public String getValue() {
		return value;
	}

	public String getLabel() {
		return StringUtils.isBlank(label)?value:label;
	}
	
	public boolean isValue(String value) {
		return !StringUtils.isBlank(this.value)
			&&!StringUtils.isBlank(value)
			&&this.value.equals(value);
	}
	
	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	
	public void init() {
		if (StringUtils.isBlank(value)) {
			value = label;
		}
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		sb.append("value="+getValue());
		sb.append("label:"+getLabel());
		return sb.toString();
	}
}
