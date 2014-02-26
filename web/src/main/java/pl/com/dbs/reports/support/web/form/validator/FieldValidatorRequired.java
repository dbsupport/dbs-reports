/**
 * 
 */
package pl.com.dbs.reports.support.web.form.validator;

import java.util.LinkedList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import org.apache.commons.lang.StringUtils;
import org.eclipse.persistence.oxm.annotations.XmlDiscriminatorValue;

import pl.com.dbs.reports.support.web.form.field.AField;


/**
 * Requirement validator.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlDiscriminatorValue("required")
public class FieldValidatorRequired<T> extends AFieldValidator<T> {
	public FieldValidatorRequired() {
		super();
	}
	
	@Override
	public void validate(AField<T> field, LinkedList<AField<?>> fields) throws FieldValidatorException {
		if (StringUtils.isBlank(field.getValue())) throw new FieldValidatorException(field, "errors.required");
	}

	@Override
	public void init(AField<?> field, LinkedList<AField<?>> fields) {
		this.description = new FieldValidatorDescription("errors.required");
	}	
	
}
