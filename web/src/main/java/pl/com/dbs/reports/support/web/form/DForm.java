/**
 * 
 */
package pl.com.dbs.reports.support.web.form;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import org.springframework.validation.Errors;

import pl.com.dbs.reports.support.utils.separator.Separator;
import pl.com.dbs.reports.support.web.form.field.AField;
import pl.com.dbs.reports.support.web.form.field.IFieldDivisible;
import pl.com.dbs.reports.support.web.form.field.IFieldInflatable;
import pl.com.dbs.reports.support.web.form.inflater.FieldInflater;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.inject.internal.Lists;
import com.google.inject.internal.Maps;



/**
 * Abstract dynamic form.
 * @see form-schema-x.x.x.xsd
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
@XmlAccessorType(XmlAccessType.FIELD)
//@XmlRootElement(name = "form", namespace = "http://www.dbs.com.pl/reports/1.0/form")
public abstract class DForm extends AForm {
	@XmlElement(name="field", namespace = "http://www.dbs.com.pl/reports/1.0/form")
	@XmlElementWrapper(name="fields", namespace = "http://www.dbs.com.pl/reports/1.0/form")
	protected LinkedList<AField<?>> fields;
	
	@XmlElement(name="@stop", namespace = "http://www.dbs.com.pl/reports/1.0/form")
	private boolean stop;	
	
	public DForm() {
		super();
	}

	/**
	 * Initialize objects..
	 */
	public void afterUnmarshal(Unmarshaller unmarshaller, Object parent) {
		if (isFieldfull()) for (AField<?> field : fields) 
			field.init(fields);
	}
	
	public void reset() {
		super.reset();
	}

	public boolean isFieldfull() {
		return this.fields!=null&&!this.fields.isEmpty();
	}

	public LinkedList<AField<?>> getFields() {
		return fields;
	}
	
	public void validate(Errors errors) {
		for (int idx=0; idx<this.fields.size(); idx++) {
			AField<?> field = this.fields.get(idx);
			//..validate..
			field.validate(this.fields, errors, "fields["+idx+"]");
			if (errors.hasErrors()&&stop) break;
		}
	}
	
	public List<Map<String, String>> getParameters() {
		List<Map<String, String>> result = Lists.newArrayList();
		
		List<IFieldDivisible> divisibles = getDivisibles();
		
		if (!divisibles.isEmpty()) {
			/**
			 * only one divisible expected now...
			 */
			IFieldDivisible divisible = divisibles.get(0);
			String name = divisible.getName();
			for (String value : divisible.getValue()) {
				Map<String, String> params = getNonDivisiblesAsMap();
				/**
				 * FIXME: divisible ALWAYS at the END of the map!
				 */
				params.put(name, value);
				result.add(params);
			}
		} else {
			result.add(getNonDivisiblesAsMap());
		}
		
		return result;
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		Separator s = new Separator("\n");
		for (AField<?> field : fields) sb.append(s).append(field);
		return sb.toString();
	}
	
	/**
	 * inflate inflatable field with inflaters;)
	 */
	DForm inflate(Set<FieldInflater> inflaters) {
	    /**
	     * check if form has special fields.. 
	     */
	    for (IFieldInflatable field : getInflatables()) {
	    	FieldInflater inflater = resolveInflater(inflaters, field);
	    	if (inflater!=null) inflater.inflate(field);
	    }
	    return this;
	}	
	
	private FieldInflater resolveInflater(final Set<FieldInflater> inflaters, final IFieldInflatable field) {
		return Iterables.find(inflaters, new Predicate<FieldInflater>() {
			@Override
			public boolean apply(FieldInflater input) {
				return input.supports(field);
			}
		}, null);
	}

	private List<IFieldInflatable> getInflatables() {
		if (this.fields==null) return Lists.newArrayList();
		return Lists.newArrayList(Iterables.filter(this.fields, IFieldInflatable.class));
	}
	
	private List<IFieldDivisible> getDivisibles() {
		if (this.fields==null) return Lists.newArrayList();
		Iterable<IFieldDivisible> divisionables = Iterables.filter(this.fields, IFieldDivisible.class);
		divisionables = Iterables.filter(divisionables, new Predicate<IFieldDivisible>() {
			@Override
			public boolean apply(IFieldDivisible input) {
				return input.divides();
			}
		});
		return Lists.newArrayList(divisionables);
	}	
	
	private List<AField<?>> getNonDivisibles() {
		if (this.fields==null) return Lists.newArrayList();
		
		return Lists.newArrayList(Iterables.filter(this.fields, new Predicate<AField<?>>() {
				@Override
				public boolean apply(AField<?> input) {
					return (!(input instanceof IFieldDivisible)||
							(input instanceof IFieldDivisible &&
							!((IFieldDivisible)input).divides()));
				}
			}));
	}	
	
	private Map<String, String> getNonDivisiblesAsMap() {
		Map<String, String> parameters = Maps.newLinkedHashMap();
		for (AField<?> field : getNonDivisibles()) {
			//if (field.hasValue())
				parameters.put(field.getName(), field.getValueAsString());
		}
		return parameters;
	}
	
}
