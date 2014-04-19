/**
 * 
 */
package pl.com.dbs.reports.support.web.form.option;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

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
	@XmlAttribute(name="value", required = true)
	private String value;	
	@XmlAttribute(name="label", required = false)
	private String label;
	
	public FieldOption() {}
	
	public String getValue() {
		return value;
	}

	public String getLabel() {
		return label;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		sb.append("value="+value);
		sb.append("label:"+label);
		return sb.toString();
	}
}
