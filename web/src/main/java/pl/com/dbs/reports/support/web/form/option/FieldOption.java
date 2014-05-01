/**
 * 
 */
package pl.com.dbs.reports.support.web.form.option;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;

import org.apache.commons.lang.StringUtils;
import org.eclipse.persistence.oxm.annotations.XmlDiscriminatorValue;

/**
 * Available field value.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2014
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlDiscriminatorValue("option")
public class FieldOption {
	@XmlAttribute(name="value", required = false)
	private String value;	
	@XmlValue
	protected String label;
	
	public FieldOption() {}
	
	public String getValue() {
		return StringUtils.isBlank(value)?label:value;
	}

	public String getLabel() {
		return StringUtils.isBlank(label)?value:label;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		sb.append("value="+getValue());
		sb.append("label:"+getLabel());
		return sb.toString();
	}
}
