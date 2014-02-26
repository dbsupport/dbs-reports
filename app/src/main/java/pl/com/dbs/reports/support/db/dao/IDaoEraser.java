/**
 * 
 */
package pl.com.dbs.reports.support.db.dao;

import java.util.Collection;

import pl.com.dbs.reports.support.db.domain.IEntity;

/**
 * DAP interface of ereasing.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
public interface IDaoEraser<T extends IEntity, K>  {
	/**
	 * Usuwa pojedynczy obiekt. Pod spodem wywoływana jest metoda <b>EntityManager.remove</b>
	 * 
	 * @see <a href="http://docs.oracle.com/javaee/6/api/javax/persistence/EntityManager.html#remove(java.lang.Object)">EntityManager.remove</a>
	 */
	void erase(T obiect) throws DaoException;

	/**
	 * Usuwa kolekcję obiektów. Pod spodem dla każdego elementu kolekcji wywoływana jest metoda
	 * <b>EntityManager.remove</b>
	 * 
	 * @see <a href="http://docs.oracle.com/javaee/6/api/javax/persistence/EntityManager.html#remove(java.lang.Object)">EntityManager.remove</a>
	 */
	void erase(Collection<T> obiects) throws DaoException;
}
