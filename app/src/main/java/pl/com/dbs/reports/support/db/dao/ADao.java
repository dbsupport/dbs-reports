/**
 * 
 */
package pl.com.dbs.reports.support.db.dao;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;

import org.apache.commons.lang.Validate;

import pl.com.dbs.reports.support.db.domain.IEntity;
import pl.com.dbs.reports.support.filter.Filter;
import pl.com.dbs.reports.support.filter.Pager;
import pl.com.dbs.reports.support.filter.SorterField;

/**
 * Base CRUD.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
public abstract class ADao<T extends IEntity, K> implements IDaoReader<T, K> ,IDaoWriter<T, K> ,IDaoEraser<T, K> {

	/**
	 * Zwraca Entity Manager którego ma używać to DAO.
	 */
	public abstract EntityManager getEntityManager();	
	
	@Override
	public void create(T obiect) {
		getEntityManager().persist(obiect);
	}

	@Override
	public void erase(T obiect) {
		getEntityManager().remove(obiect);
	}

	@Override
	public void erase(Collection<T> obiects) {
		for(T obiect : obiects) erase(obiect);
	}
	
	@Override
    public T find(K id) {
        return getEntityManager().find(clazz, id);
    }
	
	@Override
	public List<T> find(AFilter<T> filter) {
		Validate.notNull(filter, "Filtr is no more!");
		FilterDao<T> dao = new FilterDao<T>(filter, getEntityManager());
		filter = dao.getUpdatedFilter();
		return dao.getResults();
	}
	
	
	
	
	
	
	
	@SuppressWarnings("unchecked")
	protected T executeSingleQuery(String sql) {
		Query query = getEntityManager().createQuery(sql);
		try {
			return (T) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	protected List<T> executeQuery(String sql) {
		return executeQuery(getEntityManager().createQuery(sql));
	}
	
	@SuppressWarnings("unchecked")
	protected List<T> executeQuery(Query query) {
		return query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	protected T executeSingleQuery(Query query) {
		try {
			return (T)query.getSingleResult();
		} catch (NoResultException e) {}
		return null;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected T executeSingleQuery(CriteriaQuery criteria) {
		try {
			Query query = getEntityManager().createQuery(criteria);
			return (T)query.getSingleResult();
		} catch (NoResultException e) {}
		return null;
	}
	

	protected int count(String sql) {
		return executeQuery(sql).size();
	}
	
	protected int count(CriteriaQuery<T> criteria) {
		Query query = getEntityManager().createQuery(criteria);
		return executeQuery(query).size();
	}

	@SuppressWarnings("unchecked")
	protected List<T> executeQuery(String sql, Filter filter) {
		Query query = getEntityManager().createQuery(sql);

		List<T> result = new ArrayList<T>();

    	if (filter != null && filter.getPager() != null && filter.getPager().getPageSize() != Pager.NO_LIMIT) {
    		int all = count(sql);
			filter.getPager().setDataSize(all);
			if(all > 0) {
				int max = filter.getPager().getDataEnd() - filter.getPager().getDataStart() + 1;
				query.setFirstResult(filter.getPager().getDataStart()-1);
				query.setMaxResults(max);
				result = query.getResultList();
			}
    	} else {
            result = executeQuery(sql);
           	//filter.getPager().setDataSize(result.size());
        }
    	return result;
	}
	
//	@SuppressWarnings({"unchecked", "rawtypes"})
//	protected List<T> executeQuery(CriteriaQuery criteria, Filter filter) {
//		Query query = getEntityManager().createQuery(criteria);
//
//		List<T> result = new ArrayList<T>();
//		
//		
//    	if (filter != null && filter.getSorter() != null && filter.getSorter().isOn()) {
//    		Class clazz = getClazz();
//    		Root<?> c = criteria.from(clazz);
//    		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
//    		for (SorterField field : filter.getSorter().getFields()) {
//    			criteria.orderBy(field.isAsc()?cb.asc(c.get(field.getName())):cb.desc(c.get(field.getName())));
//    		}
//    	}		
//
//    	if (filter!=null && filter.isPaging()) {
//    		int all = count(criteria);
//			filter.getPager().setDataSize(all);
//			if(all > 0) {
//				int max = filter.getPager().getDataEnd() - filter.getPager().getDataStart() + 1;
//				query.setFirstResult(filter.getPager().getDataStart()-1);
//				query.setMaxResults(max);
//				result = query.getResultList();
//			}
//    	} else {
//            result = executeQuery(query);
//           	//filter.getPager().setDataSize(result.size());
//        }
//    	return result;
//	}
	
	@SuppressWarnings({"unchecked", "rawtypes"})
	protected List<T> executeQuery(IContextDao context) {
		List<T> result = new ArrayList<T>();
		Validate.notNull(context, "A contex is null!");
		CriteriaQuery<T> criteria = context.getCriteria();
		Validate.notNull(criteria, "A criteria is null!");
		Filter filter = context.getFilter();
		
		
    	if (filter != null && filter.getSorter() != null && filter.getSorter().isOn() && context.getBuilder()!=null) {
    		List<Order> orders = new ArrayList<Order>();
    		for (SorterField field : filter.getSorter().getFields()) {
    			orders.add(field.isAsc()?context.getBuilder().asc(context.getRoot().get(field.getName())):context.getBuilder().desc(context.getRoot().get(field.getName())));
    		}
    		criteria.orderBy(orders);
    	}
    	
    	Query query = getEntityManager().createQuery(criteria);

    	if (filter!=null && filter.isPaging()) {
    		int all = count(criteria);
			filter.getPager().setDataSize(all);
			if(all > 0) {
				int max = filter.getPager().getDataEnd() - filter.getPager().getDataStart() + 1;
				query.setFirstResult(filter.getPager().getDataStart()-1);
				query.setMaxResults(max);
				result = query.getResultList();
			}
    	} else {
            result = executeQuery(query);
           	//filter.getPager().setDataSize(result.size());
        }
    	return result;
	}	

	/**
	 * Return FIRST or none.
	 */
	protected T executeQuerySingle(IContextDao context) {
		List<T> result = executeQuery(context);
    	return result.isEmpty()?null:result.get(0);
	}		
	
//	@SuppressWarnings({"unchecked"})
//	protected List<T> executeQuery(Query query, Filter filter) {
//		List<T> result = new ArrayList<T>();
//
//    	if (filter != null&& filter.getPager() != null&& filter.getPager().getPageSize() != Pager.NO_LIMIT) {
//    		int all = count(query);
//			filter.getPager().setDataSize(all);
//			if(all > 0) {
//				int max = filter.getPager().getDataEnd() - filter.getPager().getDataStart() + 1;
//				query.setFirstResult(filter.getPager().getDataStart()-1);
//				query.setMaxResults(max);
//				result = query.getResultList();
//			}
//    	} else {
//            result = executeQuery(query);
//           	filter.getPager().setDataSize(result.size());
//        }
//    	return result;
//	}	
	
	
	
	protected Class<T> clazz = getClazz();
	
	@SuppressWarnings("unchecked")
	private Class<T> getClazz() {
		Class<?> generic = this.getClass();
		if(ADao.class.isAssignableFrom(this.getClass().getSuperclass())) {
			Type typ;

			do {
				typ = generic.getGenericSuperclass();
				if(typ instanceof ParameterizedType) {
					return (Class<T>)((ParameterizedType)typ).getActualTypeArguments()[0];
				}
				generic = generic.getSuperclass();
			} while(generic != ADao.class);

			throw new IllegalStateException("Brak mozliwosci pobrania parametrow generycznego DAO dla "
					+ generic.getSuperclass() + ", zweryfikuj czy klasa "
					+ this.getClass() + " poprawnie implementuje DAO generyczne bazujace na ADao");
		}
		throw new IllegalStateException("Klasa " + this.getClass().getName()
				+ " nie dziedziczy po " + ADao.class.getName()
				+ " odczytanie paramertu klasy (typu bazodanowego) nie jest mozliwe");
	}	
}
