/**
 * 
 */
package pl.com.dbs.reports.support.web.form.field;

import java.util.LinkedList;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import org.apache.commons.lang.StringUtils;
import org.eclipse.persistence.oxm.annotations.XmlDiscriminatorNode;
import org.springframework.validation.Errors;

import pl.com.dbs.reports.support.utils.separator.Separator;
import pl.com.dbs.reports.support.web.form.validator.AFieldValidator;
import pl.com.dbs.reports.support.web.form.validator.FieldValidatorException;

/**
 * Abstract dynamic form field.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlDiscriminatorNode("@type")
public abstract class AField<T> {
	//@XmlTransient
	@XmlAttribute(name="type", required = true)
	private String type;	
	@XmlAttribute(name="name", required = true)
	private String name;
	
	@XmlAttribute(name="label")
	private String label;
	//FIXME: private T value; nie dziala...
	@XmlAttribute(name="value")
	protected String value;
	@XmlAttribute(name="tooltip")
	private String tooltip;
	@XmlAttribute(name="format")
	protected String format;
	@XmlElement(name="validator", namespace = "http://www.dbs.com.pl/reports/1.0/form")
	private LinkedList<AFieldValidator<T>> validators;
	
	public AField() {}

	/**
	 * Field label.
	 */
	public String getLabel() {
		return StringUtils.isBlank(label)?"":label;
	}
	/**
	 * Field acctual name.
	 */
	public String getName() {
		return name;
	}
	/**
	 * Placeholder
	 */
	public String getTooltip() {
		return StringUtils.isBlank(tooltip)?"":tooltip;
	}
	/**
	 * Get view (bootstrap) format. 
	 */
	public String getFormat() {
		return StringUtils.isBlank(format)?"":format;
	}
	/**
	 * Default.
	 * Add field to parameters map in raw format.
	 */
	public void addAsParameter(Map<String, String> map) {
		map.put(name, value);
	}
	
	/**
	 * Get RAW value.
	 */
	public String getValue() {
		return this.value;
	}
	/**
	 * Set RAW value.
	 */
	public void setValue(String value) {
		this.value = value;
	}	

	/**
	 * Get DEFAULT renderer tile name.
	 */
	public String getTile() {
		return "tiles-field-text";
	}	
	
	public LinkedList<AFieldValidator<T>> getValidators() {
		return validators;
	}

	/**
	 * Run all validators assigned to this field.
	 */
	public void validate(LinkedList<AField<?>> fields, Errors errors, String name) {
		if (!isValidated()) return;
		
		for (AFieldValidator<T> validator : validators) {
			try {
				validator.validate(this, fields);
			} catch (FieldValidatorException e) {
				errors.rejectValue(name, e.getCode(), e.getArgs(), e.getCode());
				if (validator.isStop()) break;
			}
		}
	}

	/**
	 * Initialize field.
	 */
	public void init(LinkedList<AField<?>> fields) {
		if (!isValidated()) return;
		for (AFieldValidator<T> validator : validators) validator.init(this, fields);
	}
	
	/**
	 * Get typized value.
	 */
	public abstract T getValueTypized();
	
	

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(getName()+"=");
		sb.append("type:"+type);
		if (!StringUtils.isBlank(label)) sb.append(";label:"+getLabel());
		if (!StringUtils.isBlank(value)) sb.append(";value:"+getValue());
		if (!StringUtils.isBlank(tooltip)) sb.append(";tooltip:"+getTooltip());
		if (!StringUtils.isBlank(format)) sb.append(";format:"+format);
		if (isValidated()) {
			sb.append(";validators:");
			Separator s = new Separator(",");
			for (AFieldValidator<T> validator: validators) sb.append(s).append("[").append(validator).append("]");
		}
		return sb.toString(); 
	}
	
	/**
	 * Any validator assigned to this field?
	 */
	private boolean isValidated() {
		return this.validators!=null&&!this.validators.isEmpty();
	}	
	
}
