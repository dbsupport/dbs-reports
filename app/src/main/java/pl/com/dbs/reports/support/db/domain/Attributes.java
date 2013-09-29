package pl.com.dbs.reports.support.db.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * Pola techniczne dla kazdej encji.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
@Embeddable
/*package*/ class Attributes {
	private static final Log logger = LogFactory.getLog(Attributes.class);
	
	private static final String UNDEFINED = "undefined";
	
	@Column(name = IEntity.CREATE_DATE)
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;

	@Column(name = IEntity.EDIT_DATE)
	@Temporal(TemporalType.TIMESTAMP)
	private Date editDate;

	@Column(name = IEntity.CREATE_GSID)
	private String createGsid;

	@Column(name = IEntity.EDIT_GSID)
	private String editGsid;
	
	/*package*/ Attributes() {/* JPA */}
	
	/*package*/ void onCreate() {
		Date now = new Date();
        createDate = now;
		editDate = now;
		String gsid = getGsid();
		createGsid = gsid;
		editGsid = gsid;
	}
	
	/*package*/ void onUpdate() {
		editDate = new Date();
		editGsid = getGsid();
	}
	
	private String getGsid() {
		//FIXME:
		String gsid = UNDEFINED;
		if (gsid != null) return gsid;
		else logger.warn("Sesja uzytkownika pusta [brak gsid]. Nie mozna prawidlowo wypelnic pola ["
	                    + "idSesjaMod] przed zapisem obiektu. Pole wypelniono wartoscia '" + UNDEFINED + "'");
			return UNDEFINED;
	}
}
