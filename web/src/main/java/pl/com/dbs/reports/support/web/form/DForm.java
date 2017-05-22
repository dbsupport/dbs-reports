/**
 * 
 */
package pl.com.dbs.reports.support.web.form;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.inject.internal.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.Errors;
import org.springframework.web.multipart.MultipartFile;
import pl.com.dbs.reports.support.utils.separator.Separator;
import pl.com.dbs.reports.support.web.form.field.AField;
import pl.com.dbs.reports.support.web.form.field.FieldFile;
import pl.com.dbs.reports.support.web.form.field.IFieldDivisible;
import pl.com.dbs.reports.support.web.form.field.IFieldInflatable;
import pl.com.dbs.reports.support.web.form.inflater.FieldInflater;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;


/**
 * Abstract dynamic form.
 * @see form-schema-x.x.x.xsd
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2013
 */
@XmlAccessorType(XmlAccessType.FIELD)
//@XmlRootElement(name = "form", namespace = "http://www.dbs.com.pl/reports/1.0/form")
public abstract class DForm extends AForm {
    private Logger log = LoggerFactory.getLogger(DForm.class);
	private static final long serialVersionUID = -8413242846104561845L;

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
	
	protected List<IFormParameters> fetchParameters() {
		List<IFormParameters> result = Lists.newArrayList();
		
		List<IFieldDivisible> divisibles = getDivisibles();
		
		if (!divisibles.isEmpty()) {
			/**
			 * only one divisible expected now...
			 */
			IFieldDivisible divisible = divisibles.get(0);
			final String name = divisible.getName();
			for (final String value : divisible.getValue()) {
				final List<IFormParameter> params = getNonDivisiblesAsList();
				/**
				 * FIXME: divisible ALWAYS at the END of the map!
				 */
				result.add(convert2Parameters(params, name, value));
			}
		} else {
			final List<IFormParameter> params = getNonDivisiblesAsList();
			result.add(convert2Parameters(params, null, null));
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
	    	if (inflater!=null) {
	    		try {
	    			inflater.inflate(field);
	    		} catch (Exception e) {
                    log.error("Error inflating filed:"+field.toString()+" with inflater:"+inflater.toString()+" Details:"+e.getMessage(), e);
	    			//field.getOptions().add(new FieldOption(null, e.getMessage().substring(0, 100)));
	    		}
	    	}
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
	
	private List<IFormParameter> getNonDivisiblesAsList() {
		List<IFormParameter> parameters = Lists.newArrayList();

		for (final AField<?> field : getNonDivisibles()) {
			parameters.add(convert2Parameter(field));
		}

		return parameters;
	}

	private IFormParameter convert2Parameter(final AField field) {
		final boolean isFile = FieldFile.class.equals(field.getClass());
		return new IFormParameter() {

			@Override
			public String getName() {
				return field.getName();
			}

			@Override
			public String getValue() {
				return !isFile ? field.getValueAsString() : null;
			}

			@Override
			public MultipartFile getFile() {
				return isFile ? ((FieldFile)field).getFile() : null;
			}

			@Override
			public String getDescription() {
				return isFile ? ((FieldFile)field).getFile().getName() : null;
			}
		};
	}

	private IFormParameters convert2Parameters(final List<IFormParameter> params, final String name, final String value) {
		return new IFormParameters() {
			public boolean isDivisibled() {
				return name != null;
			}
			@Override
			public List<IFormParameter> getParameters() {
				return params;
			}

			@Override
			public String getName() {
				return name;
			}

			@Override
			public String getValue() {
				return name != null ? value : null;
			}
		};
	}

	protected String readFile(MultipartFile file) {
		if (file == null) return null;
		try {
			return new String(file.getBytes(), "UTF-8");
		} catch (IOException e) {}
		return null;
	}

}
