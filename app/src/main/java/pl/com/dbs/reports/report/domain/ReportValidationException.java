/**
 * 
 */
package pl.com.dbs.reports.report.domain;

import java.util.List;

import org.springframework.core.NestedRuntimeException;


/**
 * Report validation error.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
public class ReportValidationException extends NestedRuntimeException {
	private static final long serialVersionUID = -5553968663222700803L;
	//private List<String> params = new ArrayList<String>();
	
	public ReportValidationException(String msg) {
		super(msg);
	}
	
	public ReportValidationException(String code, String param) {
		this(code);
		//this.addParam(param);
	}
	
	public ReportValidationException(String code, List<String> params) {
		this(code);
		//this.params.addAll(params);
	}
	
	public ReportValidationException(String msg, Throwable e) {
		super(msg, e);
	}
	
//	public ReportValidationException(Exception e, String code, String param) {
//		this(e, code);
//		this.addParam(param);
//	}
//	
//	public ReportValidationException(Exception e, String code, List<String> params) {
//		this(e, code);
//		this.params.addAll(params);
//	}
//	
//	
//	
//	public void addParam(String param) {
//		this.params.add(param);
//	}
//
//	public String getCode() {
//		return code;
//	}
//
//	public List<String> getParams() {
//		return params;
//	}
	
}
