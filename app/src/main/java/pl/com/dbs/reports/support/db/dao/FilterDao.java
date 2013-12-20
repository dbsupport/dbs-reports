package pl.com.dbs.reports.support.db.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import pl.com.dbs.reports.support.db.domain.IEntity;

/**
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
public class FilterDao<T extends IEntity> {
	protected AFilter<T> filter;
	protected Long quantity;
	protected List<T> results;
	protected EntityManager em;
	
	protected EntityManager getEntityManager() {
		return em;
	}	
	
	public FilterDao(AFilter<T> filter, EntityManager em) {
		this.filter = filter;
		this.em = em;
	}
	
	public Long getQuantity() {
		if (quantity == null) quantity = count(filter);
		return quantity;
	}
	
	public List<T> getResults() {
		if (results == null) results = getResults(filter);
		return results;
	}
	
	public AFilter<T> getUpdatedFilter() {
		updateFilter(filter, getQuantity());		
		return filter;
	}
	

	
	/**
	 * Pobiera wyniki wg filtra - brane jest pod uwagę:
	 * - warunki z filtra
	 * - sortowanie
	 * - stronicowanie
	 * 
	 * Zapytanie jest implementowane w osobnej metodzie z powodu błędu:
	 * https://issues.jboss.org/browse/SEAMSECURITY-91
	 * 
	 */
	protected List<T> getResults(AFilter<T> filter) {
		Class<T> clazz = filter.getClazz();
		CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<T> criteria = builder.createQuery(clazz);
		Root<T> from = criteria.from(clazz);
		criteria.select(from);
		
		// warunki
		criteria.where(makeCriterias(filter, builder, criteria, from));
		
		// sortowanie
		criteria.orderBy(makeOrders(filter, builder, criteria, from));
		
		// wykonaj zapytanie
		TypedQuery<T> query = getEntityManager().createQuery(criteria);
		return getResultsAccording2Pager(query, filter);
	}

	/**
	 * Pobiera ilość wszystkich wyników. Metoda składa osobne zapytanie zwracające liczbę aby nie
	 * wciągać do pamięci listy encji. Pod uwagę brane są tylko warunki z filtra wg których należy
	 * wyszukiwać a ignorowane jest sortowanie.
	 * 
	 * Zapytanie jest implementowane w osobnej metodzie z powodu błędu:
	 * https://issues.jboss.org/browse/SEAMSECURITY-91
	 * 
	 */
	protected Long count(AFilter<T> filter) {
		Class<T> clazz = filter.getClazz();
		CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
		Root<T> from = criteria.from(clazz);
		CriteriaQuery<Long> select = criteria.select(builder.count(from));
		
		Predicate[] criterias = makeCriterias(filter, builder, criteria, from);
		select.where(criterias);
		
		// wykonaj zapytanie
		TypedQuery<Long> query = getEntityManager().createQuery(select);
		return query.getSingleResult();
	}
	
	/**
	 * Uaktualnia wartosci w filtrze w oparciu o wyniki.
	 */
	protected void updateFilter(AFilter<T> filter, Long quantity) {
		if (isPaging(filter)) filter.getPager().setDataSize(quantity.intValue());
	}

	/**
	 * Metoda pobiera dane dbając o prawidłowe przesunięcie początkowe 
	 * oraz ilość danych jaką należy wyciągnąć.
	 */
	protected List<T> getResultsAccording2Pager(TypedQuery<T> query, AFilter<T> filter) {
		query.setFirstResult(resolverStartIndex(filter));
		query.setMaxResults(resolverMaxResults(filter));
		return query.getResultList();
	}
	
	/**
	 * Zwraca początkową pozycję w liście wszystkich wyników (offset).
	 */
	protected int resolverStartIndex(AFilter<T> filter) {
		int result = 0;
		if (isPaging(filter)) {
			final int index = filter.getPager().getDataStart();
			if (index > 1) result = index - 1;
		}
		
		return result;
	}
	
	/**
	 * Zwraca ilość danych jaką należy pobrać.
	 */
	protected int resolverMaxResults(AFilter<T> filter) {
		return isPaging(filter)?filter.getPager().getPageSize():0;
	}
	
	/**
	 * Sprawdza czy w filtrze zostały ustawione dane stronicowania.
	 * 
	 */
	protected boolean isPaging(AFilter<T> filter) {
		return filter != null && filter.isPaging();
	}

	/**
	 * Metoda tłumaczy każde ustawione pole w filtrze na predykat - warunek zapytania. 
	 * Pola są walidowane.
	 */
	protected Predicate[] makeCriterias(AFilter<T> filter, CriteriaBuilder builder, CriteriaQuery<?> query, Root<T> from) {
		List<Predicate> criteria = new ArrayList<Predicate>();
		if (filter != null) {
//NOT IMPLEMENTED YET			
//			for (IPoleWyszukiwanie field : filter.getPolaWyszukiwania()) {
//				if (field.czyWartoscPolaSpelniaWymaganiaWalidatorow()) {
//					criteria.add(field.wywolaj(builder, from));
//				}
//			}
		}
		return criteria.toArray(new Predicate[criteria.size()]);
	}

	/**
	 * Metoda tłumaczy każde ustawione w filtrze pole wg którego należy sortować na odpowiedni
	 * obiekt Order który zostanie uwzględniony w zapytaniu.
	 */
	protected List<Order> makeOrders(AFilter<T> filter, CriteriaBuilder builder, CriteriaQuery<T> query, Root<T> from) {
		List<Order> orders = new ArrayList<Order>();
    	if (filter != null) {
//NOT IMPLEMENTED YET	    		
//    		for (IPoleSortowanie pole : filter.getPolaSortowania()) {
//    			orders.add(pole.wywolaj(builder, from));
//    		}
    	}
    	return orders;
	}
}