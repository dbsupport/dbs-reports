/**
 * 
 */
package pl.com.dbs.reports.support.web.form.field;

import java.util.Date;
import java.util.LinkedList;
import java.util.regex.Pattern;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.eclipse.persistence.oxm.annotations.XmlDiscriminatorValue;
import org.joda.time.format.DateTimeFormat;


/**
 * Default date field.
 * WATCH IT! bootstrap format:dd/mm/yyyy = DateFormat:dd/MM/yyyy
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlDiscriminatorValue("date")
public class FieldDate  extends AField<Date> {
	private static final Pattern TIME_PATTERN = Pattern.compile("hh|HH|HH24|hh24", Pattern.CASE_INSENSITIVE);
	private static final Pattern NOW_PATTERN = Pattern.compile("now|teraz", Pattern.CASE_INSENSITIVE);
	
	private Date value;
	/**
	 * value as string coz it can be initialized by pattern, i.e. NOW
	 * temporary value.
	 */
	//FIXME: private T value; nie dziala...
	@XmlAttribute(name="value")
	private String svalue;
	
	public FieldDate() {
		super();
	}
	
	@Override
	public Date getValue() {
		return this.value;
	}
	
	@Override
	public String getValueAsString() {
		return hasValue()?svalue:"";
	}
	
	public void setValueAsString(String value) {
		this.svalue = value;
		try {
			this.value = DateTimeFormat.forPattern(resolveFormat()).parseDateTime(value).toDate();
		} catch (Exception e) {}
	}
	
	@Override
	public void setValue(Date value) {
		this.value = value;
	}
	
	@Override
	public boolean hasValue() {
		return getValue()!=null;
	}

	@Override
	public void init(LinkedList<AField<?>> fields) {
		if (!StringUtils.isBlank(svalue)&&NOW_PATTERN.matcher(svalue).find()) {
			this.value = new Date();
			this.svalue = DateFormatUtils.format(this.value, resolveFormat());
		}
		super.init(fields);
	}
	
	@Override
	public String getTile() {
		return "tiles-field-date";
	}
	
	@Override
	public String getFormat() {
		return StringUtils.isBlank(format)?"yyyy-mm-dd":format;
	}
	
	public boolean isWithTime() {
		return TIME_PATTERN.matcher(getFormat()).find();
	}
	
	private String resolveFormat() {
		/**
		 * http://www.malot.fr/bootstrap-datetimepicker/
		 * 
		 * bootstrap mm = format MM
		 * bootstrap ii = format mm
		 */
		String format = getFormat(); 
		format = format.replaceFirst("mm", "MM");
		format = format.replaceFirst("ii", "mm");
		
		return format;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer(super.toString());
		sb.append(";value:"+(hasValue()?svalue:""));
		return sb.toString(); 
	}	
	
}
