/**
 * 
 */
package pl.com.dbs.reports.support.web.form.validator;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.LinkedList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import org.eclipse.persistence.oxm.annotations.XmlDiscriminatorValue;

import pl.com.dbs.reports.support.web.form.field.AField;
import pl.com.dbs.reports.support.web.form.field.FieldNumber;
import pl.com.dbs.reports.support.web.form.field.FieldText;


/**
 * Is value = min or more?
 * If not throw exception!

 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2013
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlDiscriminatorValue("min")
public class FieldValidatorMin extends AFieldValidator {
	private static final long serialVersionUID = 5447642679865505766L;

	public FieldValidatorMin() {
		super();
	}
	
	@Override
	public void validate(AField<?> field, LinkedList<AField<?>> fields) throws FieldValidatorException {
		if (field instanceof FieldText) {
			FieldText f = (FieldText)field;
			int min = Integer.valueOf(this.parameter);
			if (field.getValue()==null||min>f.getValue().length()) 
				throw new FieldValidatorException(field, "errors.min.text",  new String[]{this.parameter});
		} else if (field instanceof FieldNumber) {
			FieldNumber f = (FieldNumber)field;
			try {
				Number min = NumberFormat.getNumberInstance().parse(this.parameter);
				if (f.getValue()==null||f.getValue().doubleValue()<min.doubleValue())
					throw new FieldValidatorException(field, "errors.min.number", new String[]{this.parameter});
			} catch (ParseException e) {
				throw new FieldValidatorImproperException(field, this.type, e);
			}
		} else {
			throw new FieldValidatorNotImplementedException(field, this.type);
		}		
	}
	
	@Override
	public void init(AField<?> field, LinkedList<AField<?>> fields) {
		if (field instanceof FieldText) {
			this.description = new FieldValidatorDescription("errors.min.text", this.parameter);
		} else if (field instanceof FieldNumber) {
			this.description = new FieldValidatorDescription("errors.min.number", this.parameter);
		}
	}		
	
}
