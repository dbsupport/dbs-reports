/**
 * 
 */
package pl.com.dbs.reports.parameter.web.form;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import pl.com.dbs.reports.parameter.dao.ParameterFilter;
import pl.com.dbs.reports.parameter.domain.Parameter;
import pl.com.dbs.reports.parameter.domain.ParameterScope;
import pl.com.dbs.reports.parameter.domain.ParameterType;
import pl.com.dbs.reports.support.web.form.AForm;



/**
 * Parameters edition form.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
public class ParameterEditForm extends AForm {
	private static final long serialVersionUID = 3152234608866799682L;
	
	private List<Param> params = new ArrayList<Param>();
	private ParameterFilter filter;
	
	public ParameterEditForm() {}
	
	public ParameterEditForm(ParameterScope scope) {
		super();
		this.filter = new ParameterFilter().scope(scope);
	}
	
	public void reset(List<Parameter> params) {
		super.reset();
		this.params = new ArrayList<Param>();
		for (Parameter param : params)
			this.params.add(new Param(param));
	}
	
	public List<Param> getParams() {
		return params;
	}

	public void setParams(List<Param> params) {
		this.params = params;
	}
	
//	public void setFile(FileMeta file, String key) {
//		for (Param param : params) {
//			if (param.getKey().equals(key)) {
//				param.file = file;
//			}
//		}
//	}	

	public class Param implements Serializable { 
		private static final long serialVersionUID = -2450459252544068342L;
		
		private String key;
		private String value;
		private String desc;
		private MultipartFile file;
		private ParameterType type;
		
		Param(Parameter param) {
			this.key = param.getKey();
			if (!ParameterType.FILE.equals(param.getType())) {
				this.value = param.getValueAsString();
			} else if (param.getValue()!=null) {
				this.desc = param.getDesc();
//				try {
//
//					this.file = new CommonsMultipartFile(); 
//							FileMeta.bytesToFile(param.getDesc(), param.getValue());
//				} catch (IOException e) {}
			}
			this.type = param.getType();
		}

		public String getKey() {
			return key;
		}

		public String getValue() {
			return value;
		}
		
		public byte[] getValueAsBytes() {
			if (!ParameterType.FILE.equals(type)) {
				return this.value!=null?this.value.getBytes():null;
			} else if (file!=null) {
				try {
					return file.getBytes();
				} catch (IOException e) {}
			}
			return null;
		}
		

		public void setValue(String value) {
			this.value = value;
		}

		public ParameterType getType() {
			return type;
		}

		public MultipartFile getFile() {
			return file;
		}
		
		public void setFile(MultipartFile file) {
			this.file = file;
			this.desc = file!=null?file.getOriginalFilename():null;
		}

		public boolean isValued() {
			if (!ParameterType.FILE.equals(type)) 
				return !StringUtils.isBlank(this.value);
			return !StringUtils.isBlank(this.desc);
		}
		
		public String getDesc() {
			return this.desc;
		}
	}

	public ParameterFilter getFilter() {
		return filter;
	}
	
}
