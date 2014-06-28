/**
 * 
 */
package pl.com.dbs.reports.support.web.form;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.springframework.dao.DataAccessException;

import pl.com.dbs.reports.support.web.form.field.FieldDate;
import pl.com.dbs.reports.support.web.form.field.FieldMultiSelect;
import pl.com.dbs.reports.support.web.form.field.FieldNumber;
import pl.com.dbs.reports.support.web.form.field.FieldSelect;
import pl.com.dbs.reports.support.web.form.field.FieldText;
import pl.com.dbs.reports.support.web.form.inflater.FieldInflater;
import pl.com.dbs.reports.support.web.form.option.FieldOption;
import pl.com.dbs.reports.support.web.form.validator.FieldValidatorAfter;
import pl.com.dbs.reports.support.web.form.validator.FieldValidatorBefore;
import pl.com.dbs.reports.support.web.form.validator.FieldValidatorMax;
import pl.com.dbs.reports.support.web.form.validator.FieldValidatorMin;
import pl.com.dbs.reports.support.web.form.validator.FieldValidatorRequired;

import com.google.common.collect.Sets;

/**
 * Dynamic form builder.
 * Reads *.xml (based on form-schema.xsd) to construct form.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
public class DFormBuilder<T extends DForm> {
//	private static final Log logger = LogFactory.getLog(DFormBuilder.class);
//	private final TypeToken typeToken = new TypeToken(getClass()) { };
//	private final Type type = typeToken.getType();
	private Set<Class<?>> classes = new HashSet<Class<?>>();
	private T form;
	private byte[] content;
	private Set<FieldInflater> inflaters;

//	public DFormBuilder(byte[] content) {
//		this.content = content;
//		addKnownClasses();
//		this.classes.add(getClazz());
//	}
	
	public DFormBuilder(byte[] content, Class<?> clazz) {
		this.content = content;
		addKnownClasses();
		this.classes.add(clazz);
	}
	
	
	public DFormBuilder(File file, Class<?> clazz) throws IOException {
		InputStream input = new FileInputStream(file);
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int bytesRead;
		while ((bytesRead = input.read(buffer)) != -1) 
	        output.write(buffer, 0, bytesRead);
	    output.close();
	    input.close();
	    
	    this.content = output.toByteArray();
		addKnownClasses();
		this.classes.add(clazz);	    
	}
	
	public DFormBuilder<T> add(Set<FieldInflater> inflaters) {
		this.inflaters = inflaters;
		return this;
	}
	
//	public DFormBuilder(byte[] content, Class<?>[] classes) throws IOException {
//		this(content);
//		this.classes = classes;
//	}
//	
//	public DFormBuilder(File file, Class<?>[] classes) throws IOException {
//		InputStream input = new FileInputStream(file);
//		ByteArrayOutputStream output = new ByteArrayOutputStream();
//		byte[] buffer = new byte[1024];
//		int bytesRead;
//		while ((bytesRead = input.read(buffer)) != -1) 
//	        output.write(buffer, 0, bytesRead);
//	    output.close();
//	    input.close();
//	    this.content = output.toByteArray();
//		this.classes = classes;
//	}	
	
	private void addKnownClasses() {
		this.classes = Sets.<Class<?>>newHashSet(
				FieldText.class, 
				FieldDate.class,
				FieldNumber.class,
				FieldSelect.class,
				FieldMultiSelect.class,
				
				FieldOption.class,
				
				FieldValidatorRequired.class,
				FieldValidatorMin.class,
				FieldValidatorMax.class,
				FieldValidatorBefore.class,
				FieldValidatorAfter.class
		);		
	}

	/**
	 * Build form from xml..
	 */
	public DFormBuilder<T> build() throws JAXBException, DataAccessException {
		//Class<T> clazz = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];			
		//http://stackoverflow.com/questions/11785543/jaxbcontext-jaxb-properties-and-moxy
		JAXBContext context = org.eclipse.persistence.jaxb.JAXBContextFactory.createContext(classes.toArray(new Class[this.classes.size()]), null);
	    //JAXBContext context = JAXBContext.newInstance(DynamicForm.class, FieldText.class, FieldDate.class);
		return build(context);
	}
	
	@SuppressWarnings("unchecked")
	private DFormBuilder<T> build(JAXBContext context) throws JAXBException, DataAccessException {
	    Unmarshaller unmarshaller = context.createUnmarshaller();
//		    unmarshaller.setEventHandler(new ValidationEventHandler() {
//		        @Override
//		        public boolean handleEvent(ValidationEvent event) {
//		            return true;
//		        }
//
//		    });		    
	    ByteArrayInputStream in = new ByteArrayInputStream(content);
	    this.form = (T) unmarshaller.unmarshal(in);
	    
	    /**
	     * check if form has special fields.. 
	     */
	    form.inflate(inflaters);
	    
		return this;
	}	


	/**
	 * Return form after building..
	 */
	public T getForm() {
		return form;
	}

	
//	@SuppressWarnings("unchecked")
//	private Class<T> getClazz() {
//		Class<?> generic = this.getClass();
//		//if (DFormBuilder.class.isAssignableFrom(this.getClass().getSuperclass())) {
//			Type typ;
//
//			do {
//				typ = generic.getGenericSuperclass();
//				if(typ instanceof ParameterizedType) {
//					return (Class<T>)((ParameterizedType)typ).getActualTypeArguments()[0];
//				}
//				generic = generic.getSuperclass();
//			} while(generic != DFormBuilder.class);
//
//			throw new IllegalStateException("Brak mozliwosci pobrania parametrow generycznego budowniczego formularzy dynamicznych dla "
//					+ generic.getSuperclass() + ", zweryfikuj czy klasa "
//					+ this.getClass() + " poprawnie implementuje budowniczego formularzy dynamicznych bazujacego na DFormBuilder");
//		//}
////		throw new IllegalStateException("Klasa " + this.getClass().getName()
////				+ " nie dziedziczy po " + DFormBuilder.class.getName()
////				+ " odczytanie paramertu klasy (typu bazodanowego) nie jest mozliwe");
//	}		
	
}
