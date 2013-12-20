/**
 * 
 */
package pl.com.dbs.reports.support.web.form.field;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

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
	
	public FieldText() {
		super();
	}

	@Override
	public String getValueTypized() {
		return this.value;
	}
}
