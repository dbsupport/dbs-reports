/**
 * 
 */
package pl.com.dbs.reports.support.db.domain;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Version;

/**
 * Czesc wspolna kazdej encji.
 * Obsluguje pre- i post- by aktualizowac pola techniczne (Attributes)
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2013
 */
@MappedSuperclass
public abstract class AEntity implements IEntity {
	private static final long serialVersionUID = 1L;

    @Version
    @Column(name=IEntity.STAMP)
	private Long stamp;
    
    @Embedded
    private Attributes attributes;
    
    @PrePersist
	private void onCreate() {
    	if (attributes == null) {
    		attributes = new Attributes();
    	}
    	attributes.onCreate();
	}

    @PreUpdate
	private void onUpdate() {
		attributes.onUpdate();
	}
}
