/**
 * 
 */
package pl.com.dbs.reports.activedirectory.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.com.dbs.reports.activedirectory.dao.ActiveDirectoryFilter;
import pl.com.dbs.reports.activedirectory.dao.ActiveDirectoryProfile;
import pl.com.dbs.reports.api.activedirectory.ClientActiveDirectoryService;

/**
 * AD services.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2014
 */
@Service("active.directory.service")
public class ActiveDirectoryService {
	private static final Logger logger = Logger.getLogger(ActiveDirectoryService.class);
	@Autowired private ClientActiveDirectoryService clientActiveDirectoryService;
	
	public List<ActiveDirectoryProfile> find(ActiveDirectoryFilter filter) {
		return null;
	}
	
	/**
	 * Brand new profile.
	 */
	public void update(ActiveDirectoryFilter filter) {
		logger.info("Updating AD profiles..");
		
	}
	
}
