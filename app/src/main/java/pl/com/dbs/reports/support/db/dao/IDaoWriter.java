/**
 * 
 */
package pl.com.dbs.reports.support.db.dao;

import pl.com.dbs.reports.support.db.domain.IEntity;

/**
 * DAO interface of writing.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
public interface IDaoWriter<T extends IEntity, K> {
	/**
	 * Zapisuje nowy obiekt, metoda przeznaczona jest tylko dla nowych encji. Wszelkie zmiany
	 * dokonane na obiekcie przed/po wykonaniu tej metody zostaną utrwalone. Pod spodem uruchamiane
	 * jest <b>EntityManager.persist</b>.
	 * 
	 * @see <a href="http://docs.oracle.com/javaee/6/tutorial/doc/bnbqw.html#bnbrc">Zarządzenie cyklem życia encji</a>
	 * @see <a href="http://en.wikibooks.org/wiki/Java_Persistence/Print_version#Persisting">Zapisywanie encji</a>
	 * @see <a href="http://stackoverflow.com/questions/8469871/jpa-merge-vs-persist">Persist vs merge</a>
	 * @see <a href="http://docs.oracle.com/javaee/6/api/javax/persistence/EntityManager.html#persist(java.lang.Object)">EntityManager.persis</a>
	 */
	void create(T obiect) throws DaoException;
}
