package pl.com.dbs.reports.support.filter;

import java.io.Serializable;

/**
 * Sorted field.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2013
 */
public class SorterField implements Serializable {
	private static final long serialVersionUID = 3230903885321779745L;
	
	private String name;
	private boolean asc = true;
	
	
	public SorterField(String name) {
		this.name = name;
	}
	
	public SorterField(String name, boolean asc) {
		this.name = name;
		this.asc = asc;
	}
	
	public void reorder() {
		asc = !asc;
	}
	
	public boolean equals(Object other) {
		if (other == null
			|| !(other instanceof SorterField)) {
			return false;
		}
		
		if (name == null) {
			return false;
		}
		
		return name.equals(((SorterField)other).getName());
	}

	public String getName() {
		return name;
	}
	
	public boolean isAsc() {
		return asc;
	}
}
