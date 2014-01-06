package pl.com.dbs.reports.support.filter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A Sorter.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
public class Sorter implements Serializable{
	private static final long serialVersionUID = 6439216307578778627L;
	
	private List<SorterField> fields = new ArrayList<SorterField>();
	
	public boolean isOn() {
		return !fields.isEmpty();
	}
	
	public SorterField find(String name) {
		int index = fields.indexOf(new SorterField(name));
		return index != -1?fields.get(index):null;
	}

	public List<SorterField> getFields() {
		return fields;
	}

	public boolean contains(String name) {
		return fields.contains(new SorterField(name));
	}

	public void add(String name, boolean asc) {
		SorterField field = new SorterField(name, asc);
		if (fields.contains(field)) fields.remove(field);
		fields.add(field);
	}

	public void remove(String name) {
		fields.remove(new SorterField(name));
	}

	/**
	 * Called from jsp
	 */
	public void setReorder(String name) {
		SorterField field = find(name);
		if (field == null) {
			field = new SorterField(name);
		} else {
			field.reorder();
		}
		
		fields.clear();
		fields.add(field);
	}

}
