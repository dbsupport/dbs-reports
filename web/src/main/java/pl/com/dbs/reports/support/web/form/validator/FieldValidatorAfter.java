/**
 * 
 */
package pl.com.dbs.reports.support.web.form.validator;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import org.apache.commons.lang.StringUtils;
import org.eclipse.persistence.oxm.annotations.XmlDiscriminatorValue;

import pl.com.dbs.reports.support.web.form.field.AField;
import pl.com.dbs.reports.support.web.form.field.FieldDate;
import pl.com.dbs.reports.support.web.form.field.FieldNumber;


/**
 * Is value AFTER "when"?
 * If not throw exception.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2013
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlDiscriminatorValue("after")
public class FieldValidatorAfter<T> extends AFieldValidator {
	private static final long serialVersionUID = -680834683013882813L;
	
	private static final DateFormat DATEFORMAT_DEFAULT = new SimpleDateFormat("yyyy-MM-dd");
	private static final String NOW = "now";
	
	public FieldValidatorAfter() {
		super();
	}
	
	@Override
	public void validate(AField<?> field, LinkedList<AField<?>> fields) throws FieldValidatorException {
		if (field instanceof FieldDate) {
			FieldDate f1 = (FieldDate)field;
			
			Date when = null;
			if (NOW.equalsIgnoreCase(this.parameter)) {
				when = new Date();
				if (f1.getValue()==null||!when.before(f1.getValue())) {
					throw new FieldValidatorException(f1, "errors.after.date.when", new String[]{ DATEFORMAT_DEFAULT.format(when) });
				}
			} else {
				//..get field of param name..
				AField<?> f2 = findField(fields, this.parameter);
				if (f2!=null&&f2 instanceof FieldDate) when = ((FieldDate)f2).getValue();
				if (when==null||f1.getValue()==null||!when.before(f1.getValue())) {
					String f2n = StringUtils.isBlank(f2.getLabel())?f2.getName():f2.getLabel();
					throw new FieldValidatorException(f1, "errors.after.date", new String[]{f2n});
				}
			}
		} else if (field instanceof FieldNumber) {
			FieldNumber f1 = (FieldNumber)field;
			Number when = null;
			//..get field of param name..
			AField<?> f2 = findField(fields, this.parameter);
			if (f2!=null&&f2 instanceof FieldNumber) when = ((FieldNumber)f2).getValue();
			if (when==null||f1.getValue()==null||when.doubleValue()>=f1.getValue().doubleValue()) {
				String f2n = StringUtils.isBlank(f2.getLabel())?f2.getName():f2.getLabel();
				throw new FieldValidatorException(f1, "errors.after.number", new String[]{f2n});
			}
		} else {
			throw new FieldValidatorNotImplementedException(field, this.type);
		}		
	}

	@Override
	public void init(AField<?> field, LinkedList<AField<?>> fields) {
		if (field instanceof FieldDate) {
			if (NOW.equalsIgnoreCase(this.parameter)) this.description = new FieldValidatorDescription("errors.after.date.when", DATEFORMAT_DEFAULT.format(new Date()));
			else {
				AField<?> f2 = findField(fields, this.parameter);
				this.description = new FieldValidatorDescription("errors.after.date", f2!=null?f2.getLabel():"validator.unknown.field", this.parameter);
			}
		} else if (field instanceof FieldNumber) {
			AField<?> f2 = findField(fields, this.parameter);
			this.description = new FieldValidatorDescription("errors.after.number", f2!=null?f2.getLabel():"validator.unknown.field", this.parameter);
		}
	}
}
