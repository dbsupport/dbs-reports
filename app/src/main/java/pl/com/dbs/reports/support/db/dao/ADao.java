/**
 * 
 */
package pl.com.dbs.reports.support.db.dao;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;

import pl.com.dbs.reports.support.db.domain.IEntity;

/**
 * TODO
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
		return null;//wykonajZapytanieFiltr(filtr);
	}
	
	
	
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
