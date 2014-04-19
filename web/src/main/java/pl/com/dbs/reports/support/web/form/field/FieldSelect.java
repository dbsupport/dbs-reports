/**
 * 
 */
package pl.com.dbs.reports.support.web.form.field;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import org.eclipse.persistence.oxm.annotations.XmlDiscriminatorValue;


/**
 * Single select field.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlDiscriminatorValue("select")
public class FieldSelect  extends AField<String> {

	public FieldSelect() {
		super();
	}
	
	@Override
	public String getValueTypized() {
		return this.value;
	}
	
	@Override
	public String getTile() {
		return "tiles-field-select";
	}
	
}
