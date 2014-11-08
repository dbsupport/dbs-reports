/**
 * 
 */
package pl.com.dbs.reports.support.db.dao;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import pl.com.dbs.reports.support.db.domain.IEntity;
import pl.com.dbs.reports.support.filter.Filter;

/**
 * Context for DAO.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2013
 */
public class ContextDao<T extends IEntity> implements IContextDao<T> {
	private CriteriaBuilder cb;
	private Root<T> q;
	private CriteriaQuery<T> cq;
	private Filter filter;
	
	public ContextDao(EntityManager em, Class<T> clazz) {
		//Class<T> clazz = getClazz();
		this.cb = em.getCriteriaBuilder();
		this.cq = cb.createQuery(clazz);
	    this.q = cq.from(clazz);
	}
	
	public ContextDao(EntityManager em, Class<T> clazz, Filter filter) {
		this(em, clazz);
		this.filter = filter;
	}
	
	
	@Override
	public CriteriaBuilder getBuilder() {
		return cb;
	}

	@Override
	public Root<T> getRoot() {
		return q;
	}

	@Override
	public CriteriaQuery<T> getCriteria() {
		return cq;
	}

	@Override
	public Filter getFilter() {
		return filter;
	}
	
//	@SuppressWarnings("unchecked")
//	private Class<T> getClazz() {
//		return (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
//		
////		return (Class<T>)
////                ((ParameterizedType)getClass()
////                .getGenericSuperclass())
////                .getActualTypeArguments()[0];		
////		Class<?> generic = this.getClass();
////		//if(ContextDao.class.isAssignableFrom(this.getClass().getSuperclass())) {
////			Type typ;
////			do {
////				typ = generic.getGenericSuperclass();
////				if(typ instanceof ParameterizedType) {
////					return (Class<T>)((ParameterizedType)typ).getActualTypeArguments()[0];
////				}
////				generic = generic.getSuperclass();
////			} while(generic != ContextDao.class);
////
////			throw new IllegalStateException("Brak mozliwosci pobrania parametrow generycznego DAO dla "
////					+ generic.getSuperclass() + ", zweryfikuj czy klasa "
////					+ this.getClass() + " poprawnie implementuje DAO generyczne bazujace na ADao");
////		//}
////		throw new IllegalStateException("Klasa " + this.getClass().getName()
////				+ " nie dziedziczy po " + ContextDao.class.getName()
////				+ " odczytanie paramertu klasy (typu bazodanowego) nie jest mozliwe");
//	}		
	
}
