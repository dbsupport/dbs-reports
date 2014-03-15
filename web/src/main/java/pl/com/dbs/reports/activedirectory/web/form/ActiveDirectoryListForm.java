/**
 * 
 */
package pl.com.dbs.reports.activedirectory.web.form;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.google.common.collect.Lists;

import pl.com.dbs.reports.activedirectory.dao.ActiveDirectoryFilter;

/**
 * AD listing/updating form.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2014
 */
public class ActiveDirectoryListForm {
	public static final String KEY = "activeDirectoryListForm";

	private List<String> id;
	private String value;
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date date;
	private ActionDirectoryListAction action;
	
	private ActiveDirectoryFilter filter = new ActiveDirectoryFilter();
	
	public ActiveDirectoryListForm() { 
	}
	
	public void reset() {
		
	}
	
	public boolean anyIDselected() {
		return id!=null&&!id.isEmpty();
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}


	public List<String> getId() {
		return id;
	}

	public void setId(List<String> id) {
		this.id = id;
	}

	public ActiveDirectoryFilter getFilter() {
		return filter;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public ActionDirectoryListAction getAction() {
		return action;
	}

	public void setAction(ActionDirectoryListAction action) {
		this.action = action;
	}
}
