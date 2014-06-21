/**
 * 
 */
package pl.com.dbs.reports.activedirectory.web.form;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.springframework.format.annotation.DateTimeFormat;

import pl.com.dbs.reports.activedirectory.dao.ActiveDirectoryFilter;

/**
 * AD listing/updating form.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2014
 */
public class ActiveDirectoryListForm {
	public static final String KEY = "activeDirectoryListForm";
	private static final String DATE_FORMAT = "yyyy-MM-dd";
	
	private List<String> id;
	private String value;
	@DateTimeFormat(pattern = DATE_FORMAT)
	private Date date;
	private Action action;
	
	private ActiveDirectoryFilter filter = new ActiveDirectoryFilter();
	
	public ActiveDirectoryListForm() { }
	
	public void reset() {
		this.id = new ArrayList<String>();
		this.date = null;
		this.action = null;
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
	
	public String getDateFormated() {
		DateTime dateTime = new DateTime(date);
		return dateTime.toString(DATE_FORMAT);
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Action getAction() {
		return action;
	}

	public void setAction(Action action) {
		this.action = action;
	}
	
	public enum Action {
		INSERT();
	}
}

