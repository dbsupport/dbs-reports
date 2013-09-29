/**
 * 
 */
package pl.com.dbs.reports.support.db.domain;

import java.io.Serializable;

/**
 * TODO
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
public interface IEntity extends Serializable {
	String ID = "id";
	String CREATE_DATE = "_cdate";
	String CREATE_GSID = "_cgsid";
	String EDIT_DATE = "_edate";
	String EDIT_GSID = "_egsid";
	String STAMP = "_stamp";
	
}
