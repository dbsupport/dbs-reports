/**
 * 
 */
package pl.com.dbs.reports.support.web.form.field;

import java.text.NumberFormat;
import java.text.ParseException;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

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
	
	public FieldNumber() {
		super();
	}

	@Override
	public Number getValueTypized() {
		try {
			return NumberFormat.getNumberInstance().parse(this.value);
		} catch (ParseException e) {}
		return null;
	}
	
}
