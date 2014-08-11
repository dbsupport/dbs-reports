/**
 * 
 */
package pl.com.dbs.reports.support.web.form.validator;

import java.io.Serializable;
import java.util.LinkedList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlValue;

import org.apache.commons.lang.StringUtils;
import org.eclipse.persistence.oxm.annotations.XmlDiscriminatorNode;

import pl.com.dbs.reports.support.utils.separator.Separator;
import pl.com.dbs.reports.support.web.form.field.AField;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

/**
 * Abstract field validator definition.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlDiscriminatorNode("@type")
public abstract class AFieldValidator implements Serializable {
	private static final long serialVersionUID = -2500079204810428025L;
	@XmlAttribute(name="type", required = true)
	protected String type;
	@XmlAttribute(name="stop")
	private boolean stop;
	@XmlValue
	protected String parameter;
	@XmlTransient
	protected FieldValidatorDescription description = new FieldValidatorDescription("errors.validator.not.implemented", type);
	
	public AFieldValidator() {}
	
	public boolean isStop() {
		return stop;
	}
	
	public FieldValidatorDescription getDescription() {
		return description;
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(type);
		sb.append(stop?"[stops]":"");
		if (!StringUtils.isBlank(this.parameter)) sb.append("("+this.parameter+")");
		return sb.toString(); 
	}	


	/**
	 * Find field by name.
	 */
	protected static AField<?> findField(LinkedList<AField<?>> fields, final String name) {
		return Iterables.find(fields, new Predicate<AField<?>>() {
			@Override
			public boolean apply(AField<?> input) {
				return input.getName().equals(name);
			}}, null);
	}	
	
	/**
	 * Process validation
	 */
	public abstract void validate(AField<?> field, LinkedList<AField<?>> fields) throws FieldValidatorException;
	
	/**
	 * Initialize description.
	 */
	public abstract void init(AField<?> field, LinkedList<AField<?>> fields);
	
	public class FieldValidatorDescription {
		private String code;
		private String args="";
		FieldValidatorDescription(String code) {
			this.code = code;
		}
		
		FieldValidatorDescription(String code, String... args) {
			this(code);
			Separator s = new Separator(",");
			for (String arg : args) this.args += s.toString()+arg;
		}

		public String getCode() {
			return code;
		}

		public String getArgs() {
			return args;
		}		
	}
	
}
