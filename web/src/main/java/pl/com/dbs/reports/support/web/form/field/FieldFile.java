/**
 * 
 */
package pl.com.dbs.reports.support.web.form.field;

import com.google.common.base.Predicate;
import com.google.common.base.Strings;
import com.google.common.collect.Iterables;
import org.apache.commons.lang.StringUtils;
import org.eclipse.persistence.oxm.annotations.XmlDiscriminatorValue;
import org.springframework.web.multipart.MultipartFile;
import pl.com.dbs.reports.support.utils.separator.Separator;
import pl.com.dbs.reports.support.web.form.option.FieldOption;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import java.util.LinkedList;
import java.util.List;


/**
 * File field.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2017
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlDiscriminatorValue("file")
public class FieldFile extends AField<String> {
	private static final long serialVersionUID = -606676031675990235L;
	@XmlAttribute(name="value")
	protected String value;

	private MultipartFile file;

	public FieldFile() {
		super();
	}
	
	@Override
	public String getValue() {
		return this.value;
	}
	
	@Override
	public String getValueAsString() {
		return hasValue()?file.getName():"";
	}
	
	public String getValueAsLabel() {
		if (!hasValue()) return null;
		return file.getName();
	}	
	
	@Override
	public void setValue(String value) {
	}

	@Override
	public boolean hasValue() {
		return file!=null&&!file.isEmpty();
	}		
	
	@Override
	public String getTile() {
		return "tiles-field-file";
	}
	
	@Override
	public void init(LinkedList<AField<?>> fields) {
		super.init(fields);
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer(super.toString());

		sb.append(";value:"+(hasValue()?value:""));
		return sb.toString(); 
	}

	public void setFile(MultipartFile file) {
		this.file = file;
		if (null != file) {
			this.value = file.getOriginalFilename();
		}
	}

	public MultipartFile getFile() {
		return file;
	}
}
