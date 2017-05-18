/**
 * 
 */
package pl.com.dbs.reports.support.web.form.validator;

import com.google.common.base.Strings;
import org.apache.commons.lang.StringUtils;
import org.eclipse.persistence.oxm.annotations.XmlDiscriminatorValue;
import pl.com.dbs.reports.support.web.form.field.AField;
import pl.com.dbs.reports.support.web.form.field.FieldDate;
import pl.com.dbs.reports.support.web.form.field.FieldNumber;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;


/**
 * Is the only one field of given type?
 * If not throw exception!
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2017
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlDiscriminatorValue("single")
public class FieldValidatorSingle extends AFieldValidator {
	//private static final long serialVersionUID = ;

	public FieldValidatorSingle() {
		super();
	}

	@Override
	public void validate(AField<?> field, LinkedList<AField<?>> fields) throws FieldValidatorException {
		if (Strings.isNullOrEmpty(parameter)) return;

		int counter = 0;
		for (AField f : fields) {
			if (f.getType().equalsIgnoreCase(parameter)) {
				counter++;
			}
		}

		if (counter != 1) {
			throw new FieldValidatorException(field, "errors.single.field.type", new String[]{parameter});
		}
	}

	@Override
	public void init(AField<?> field, LinkedList<AField<?>> fields) {
		if (!Strings.isNullOrEmpty(parameter)) {
			this.description = new FieldValidatorDescription("errors.single.field.type", this.parameter);
		}
	}
}
