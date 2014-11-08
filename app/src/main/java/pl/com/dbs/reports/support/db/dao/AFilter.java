/**
 * 
 */
package pl.com.dbs.reports.support.db.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import pl.com.dbs.reports.support.db.domain.IEntity;
import pl.com.dbs.reports.support.filter.Filter;

/**
 * Base abstract db filter.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2013
 */
public abstract class AFilter<T extends IEntity> extends Filter implements Serializable {
	private static final long serialVersionUID = 3744924326805811415L;

	public AFilter() {
		super();
	}

//	public abstract List<IPoleWyszukiwanie> getPolaWyszukiwania();
//
//	/**
//	 * @return lista pól sortowania używanych później w order by
//	 */
//	public List<IPoleSortowanie> getPolaSortowania() {
//		List<IPoleSortowanie> polaSortowania = new ArrayList<IPoleSortowanie>();
//		if (ustawionoDaneSortowania()) {
//			for (CSystemSorterPole pole : getSorter().getPola()) {
//				polaSortowania.add(new CFiltrPoleSortowanie(pole));
//			}
//		}
//		return polaSortowania;
//	}
//	
//	private boolean ustawionoDaneSortowania() {
//		return getSorter() != null && getSorter().isWlaczony();
//	}
//	
	/**
	 * klasa ktorej dotyczylo bedzie zapytanie (new ReadAllQuery(klasa))
	 */
	@SuppressWarnings("unchecked")
	public Class<T> getClazz(){
		Type klasa = ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		return (Class<T>) klasa;
	}
}
