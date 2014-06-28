/**
 * 
 */
package pl.com.dbs.reports.support.web.form.field;

import java.text.NumberFormat;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

import org.eclipse.persistence.oxm.annotations.XmlDiscriminatorValue;


/**
 * Default number field.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlDiscriminatorValue("number")
public class FieldNumber extends AField<Number> {
	//FIXME: private T value; nie dziala...
	@XmlAttribute(name="value")
	protected Number value;	
	
	public FieldNumber() {
		super();
	}

	@Override
	public Number getValue() {
		return this.value;
	}
	
	@Override
	public String getValueAsString() {
		return hasValue()?NumberFormat.getNumberInstance().format(value):null;
	}

	@Override
	public void setValue(Number value) {
		this.value = value;
	}

	@Override
	public boolean hasValue() {
		return this.value!=null;
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer(super.toString());
		sb.append(";value:"+(hasValue()?value:""));
		return sb.toString(); 
	}
	
}
