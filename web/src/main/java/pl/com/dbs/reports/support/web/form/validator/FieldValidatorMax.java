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
 * Is value = max or less?
 * If not throw exception!
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlDiscriminatorValue("max")
public class FieldValidatorMax<T> extends AFieldValidator<T> {
	
	public FieldValidatorMax() {
		super();
	}
	
	@Override
	public void validate(AField<T> field, LinkedList<AField<?>> fields) throws FieldValidatorException {
		if (field instanceof FieldText) {
			int max = Integer.valueOf(this.parameter);
			if (field.getValue()==null||max<field.getValue().length()) 
				throw new FieldValidatorException(field, "errors.max.text",  new String[]{this.parameter});
		} else if (field instanceof FieldNumber) {
			FieldNumber f = (FieldNumber)field;
			try {
				Number max = NumberFormat.getNumberInstance().parse(this.parameter);
				if (f.getValueTypized()==null||f.getValueTypized().doubleValue()>max.doubleValue())
					throw new FieldValidatorException(field, "errors.max.number", new String[]{this.parameter});
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
			//String max = "";
			//try { max = Integer.valueOf(this.parameter).toString(); } catch (NumberFormatException e) {};
			//this.description = new String[] {"errors.max.text", !StringUtils.isBlank(max)?max:"validator.bad.parameter"};
			this.description = new FieldValidatorDescription("errors.max.text", this.parameter);
		} else if (field instanceof FieldNumber) {
			this.description = new FieldValidatorDescription("errors.max.number", this.parameter);
		}
	}		
	
}
