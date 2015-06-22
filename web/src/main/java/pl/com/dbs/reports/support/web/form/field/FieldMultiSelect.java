/**
 * 
 */
package pl.com.dbs.reports.support.web.form.field;

import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import org.apache.commons.lang.StringUtils;
import org.eclipse.persistence.oxm.annotations.XmlDiscriminatorValue;

import pl.com.dbs.reports.support.utils.separator.Separator;
import pl.com.dbs.reports.support.web.form.option.FieldOption;
import pl.com.dbs.reports.support.web.form.validator.AFieldValidator;
import pl.com.dbs.reports.support.web.form.validator.FieldValidatorDivisibles;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;


/**
 * Multi select field.
 * Multiselect splits reports on many files.
 *
 *
 *http://www.jqueryrain.com/?pYw7weSs
 *<select class="selectpicker" multiple data-selected-text-format="count>3">
     <option>Mustard</option>
      <option>Ketchup</option>
      <option>Relish</option>
      <option>Onions</option>
  </select>
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2014
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlDiscriminatorValue("multiselect")
public class FieldMultiSelect  extends AField<List<String>> implements IFieldInflatable, IFieldSelectable, IFieldDivisible {
	private static final long serialVersionUID = -5990002806441623716L;

	//FIXME: private T value; nie dziala...
	@XmlAttribute(name="value")	
	private List<String> value;
	
	@XmlElement(name="option", namespace = "http://www.dbs.com.pl/reports/1.0/form")
	private List<FieldOption> options;
	
	@XmlAttribute(name="source")
	private String source;

	
	public FieldMultiSelect() {
		super();
	}
	
	@Override
	public void init(LinkedList<AField<?>> fields) {
		if (this.validators==null) this.validators = new LinkedList<AFieldValidator>();
		this.validators.add(new FieldValidatorDivisibles());
		super.init(fields);
		if (hasOptions()) for (FieldOption option : options) option.init();
	}	

	@Override
	public List<FieldOption> getOptions() {
		return options;
	}
	
	@Override
	public String getSource() {
		return source;
	}

	@Override
	public List<String> getValue() {
		return this.value;
	}
	
	@Override
	public String getValueAsString() {
		Separator s = new Separator(", ");
		StringBuffer sb = new StringBuffer();
		if (hasValue()) {
			for (String value : this.value) {
				sb.append(s).append(value);
			}
		} else {
			sb.append(s).append("");
		}
		return sb.toString();
	}
	
	public String getValueAsLabels() {
		Separator s = new Separator(", ");
		StringBuffer sb = new StringBuffer();
		if (hasValue()) {
			for (String value : this.value) {
				FieldOption option = find(value);
				if (option != null) {
					sb.append(s).append(option.getLabel());
				}
			}
		}
		return sb.toString();
	}	
	
	public boolean isAllSelected() {
		return (this.value!=null
			   &&this.options!=null
			   &&this.value.size() == this.options.size())
			   ||(this.value==null&&this.options==null);
	}
	
	@Override
	public void setValue(List<String> value) {
		this.value = value;
		if (this.options == null) return;
		
		for (FieldOption option : this.options) {
			option.setChecked(find(option)!=null);
		}		
	}	
	
	public void setCounter(Integer counter) {
		if (counter != null && counter<=0)
			setValue(null);
	}

	@Override
	public boolean hasValue() {
		return this.value!=null&&!this.value.isEmpty();
	}	

	@Override
	public boolean divides() {
		return this.value!=null&&this.value.size()>1;
	}
	
	@Override
	public String getTooltip() {
		return StringUtils.isBlank(tooltip)?"- wybierz -":tooltip;
	}	
	
	@Override
	public FieldMultiSelect inflate(FieldOption option) {
		if (this.options == null)
			this.options = new LinkedList<FieldOption>();
		this.options.add(option);
		return this;
	}	

	
	@Override
	public String getTile() {
		return "tiles-field-multiselect";
	}
	
	private boolean hasOptions() {
		return this.options!=null&&!this.options.isEmpty();
	}
	
	private FieldOption find(final String value) {
		return hasOptions()?Iterables.find(this.options, new Predicate<FieldOption>() {
			@Override
			public boolean apply(FieldOption input) {
				return input.isValue(value);
			}
			
		}, null):null;
	}
	
	private String find(final FieldOption option) {
		if (this.value == null) return null;
		return Iterables.find(this.value, new Predicate<String>() {
			@Override
			public boolean apply(String input) {
				return option.isValue(input);
			}
		}, null);
	}	
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer(super.toString());
		if (hasOptions()) {
			sb.append(";options:");
			Separator s = new Separator(",");
			for (FieldOption option: options) sb.append(s).append("[").append(option).append("]");
		}		

		sb.append(";values:");
		if (hasValue()) {
			Separator s = new Separator(",");
			for (String value: this.value) 
				sb.append(s).append("[").append(value).append("]");
		}
		return sb.toString(); 
	}	
}
