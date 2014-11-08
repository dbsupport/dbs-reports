package pl.com.dbs.reports.support.filter;

import java.io.Serializable;

import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Pager implementation.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2013
 */
public class Pager implements Serializable{
	private static final long serialVersionUID = 1L;
	private final transient Logger log = LoggerFactory.getLogger(getClass());
	public static final int NO_LIMIT = -1;

    private int pageSize = NO_LIMIT;
    private int dataSize = 0;
    private int dataCursor = 0;
    
	public Pager() {}

	public Pager(int size) {
		this.pageSize = size;
	}

	/**
	 * @return Calkowity rozmiar danych (0 - jesli brak)
	 */
	public int getDataSize() {
		return dataSize;
	}
	
	/**
	 * Ustawia calkowity rozmiar danych,
	 * koryguje ustawienie kursora 
	 */
	public void setDataSize(int size) {
		// koryguj kursor
		if (size == 0) {
			this.dataCursor = 0;
		} else if (this.dataCursor == 0) {
			this.dataCursor = 1;
		} else if (this.dataCursor > size) {
			this.dataCursor = size;
		}
		
		this.dataSize = size > 0?size:0;
	}
	
	/**
	 * Maksymalny rozmiar danych na stronie lub -1 jesli bez limitu
	 */
	public int getPageSize() {
		return pageSize;
	}
	
	public void setPageSize(int size) {
		this.pageSize = size <= 0?NO_LIMIT:size;
	}
	
	public void setNoLimitPageSize() {
		this.pageSize = NO_LIMIT;
	}
	

	
	/**
	 * Aktualna pozycja wskaznika danych w zakresie <1; daneRozmiar>,
	 * 			lub 0 jesli brak danych
	 */
	int getDataCursor() {
		return dataCursor;
	}
	
    /**
     * Ustawia kursor na wskazanej pozycji <1; daneRozmiar>
     */
    void setDataCursor(int cursor) {
		Validate.isTrue(cursor > 0);

		if (dataSize == 0) {
			this.dataCursor = 0;
			if (log.isDebugEnabled()) {
				log.debug("Brak danych, ustawiam kursor na 0");
			}
    	} else if (cursor > dataSize) {
			this.dataCursor = dataSize;
			if (log.isDebugEnabled()) {
				log.debug("Kursor > rozmiar danych, ustawiam na rozmiar danych (" + dataSize + ")");
			}
    	} else {
    		this.dataCursor = cursor;
    	}
    }
    
    /**
     * Index na poczatek danych dla aktualnej strony, liczony od 1
     * 			lub zero - jesli brak danych
     */
    public int getDataStart() {
    	return getDataSize() == 0?0:(getPage() - 1) * getPageSize() + 1;
    }

    /**
     * Index na koniec danych dla aktualnej strony, liczony od 1
     * 			lub zero - jesli brak danych
     */
    public int getDataEnd() {
    	if (getDataSize() == 0) {
    		return 0;
    	}
		if (pageSize == NO_LIMIT) {
    		return getDataSize();
		}
    	
		return Math.min(getPage() * getPageSize(), getDataSize());
    }

    /**
     * Ustawia kursor na poczatek strony o podanym numerze
     */
    public void setPage(int page) {
        setDataCursor(pageSize == NO_LIMIT || page <= 0?1:(page - 1) * pageSize + 1);
    }
    
    /**
     * Aktualny numer strony, liczony od 1, 0 - brak danych
     */
    public int getPage() {
    	if (dataSize == 0) {
    		return 0;
    	}
    	
    	if (pageSize == NO_LIMIT) {
    		return 1;
    	}
    	
        return (dataCursor / pageSize) + (dataCursor % pageSize == 0? 0: 1);
    }
    
    /**
     * Maksymalny numer strony, liczony od 1, 0 - brak danych
     */
    public int getMaxPage() {
    	if (dataSize == 0) {
    		return 0;
    	}
    	
    	if (pageSize == NO_LIMIT) {
    		return 1;
    	}
    	
    	return (dataSize / pageSize) + (dataSize % pageSize == 0? 0: 1);
    }
    
    /**
     * czy potrzebny do stronicowania
     */
    public boolean isOn() {
    	return dataSize > 0&& pageSize != NO_LIMIT;
    }

}
