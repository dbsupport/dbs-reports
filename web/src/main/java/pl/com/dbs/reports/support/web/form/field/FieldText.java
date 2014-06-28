/**
 * 
 */
package pl.com.dbs.reports.support.web.form.field;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

import org.apache.commons.lang.StringUtils;
import org.eclipse.persistence.oxm.annotations.XmlDiscriminatorValue;


/**
 * Default text field.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlDiscriminatorValue("text")
public class FieldText extends AField<String> {
	//FIXME: private T value; nie dziala...
	@XmlAttribute(name="value")
	protected String value;	
	
	public FieldText() {
		super();
	}

	@Override
	public String getValue() {
		return this.value;
	}
	
	@Override
	public String getValueAsString() {
		return value;
	}	

	@Override
	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public boolean hasValue() {
		return !StringUtils.isBlank(this.value);
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer(super.toString());
		sb.append(";value:"+(hasValue()?value:""));
		return sb.toString(); 
	}		
}
