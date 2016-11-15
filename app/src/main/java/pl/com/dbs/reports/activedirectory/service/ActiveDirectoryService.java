/**
 * 
 */
package pl.com.dbs.reports.activedirectory.service;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.com.dbs.reports.activedirectory.dao.ActiveDirectoryFilter;
import pl.com.dbs.reports.activedirectory.dao.ActiveDirectoryProfile;
import pl.com.dbs.reports.api.activedirectory.ClientActiveDirectoryProfile;
import pl.com.dbs.reports.api.activedirectory.ClientActiveDirectoryProfileFilter;
import pl.com.dbs.reports.api.activedirectory.ClientActiveDirectoryProfileUpdateContext;
import pl.com.dbs.reports.api.activedirectory.ClientActiveDirectoryService;
import pl.com.dbs.reports.support.encoding.EncodingContext;
import pl.com.dbs.reports.support.encoding.EncodingService;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

/**
 * AD services.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2014
 */
@Service("active.directory.service")
public class ActiveDirectoryService {
	private static final Logger logger = LoggerFactory.getLogger(ActiveDirectoryService.class);
	@Autowired(required=false) private ClientActiveDirectoryService clientActiveDirectoryService;
	@Autowired private EncodingService encodingService;

	/**
	 * Find AD profiles from client DB.
	 */
	public List<ActiveDirectoryProfile> find(ActiveDirectoryFilter filter) {
		ClientActiveDirectoryProfileFilter cfilter = filter.convert();
		List<ClientActiveDirectoryProfile> profiles = clientActiveDirectoryService.find(cfilter);
		filter.update(cfilter);
		final EncodingContext econtext = encodingService.getEncodingContext();
		
		return Lists.transform(profiles, new Function<ClientActiveDirectoryProfile, ActiveDirectoryProfile>() {
			@Override
			public ActiveDirectoryProfile apply(ClientActiveDirectoryProfile input) {
				return new ActiveDirectoryProfile(input)
					.encodeFirstName(encodingService.encode(input.getFirstName(), econtext))
					.encodeLastName(encodingService.encode(input.getLastName(), econtext))
					.encodeLocationName(encodingService.encode(input.getLocationName(), econtext))
					.encodeUnitName(encodingService.encode(input.getUnitName(), econtext));
			}
		});
	}
	
	/**
	 * Update AD profiles.
	 */
	public void update(final List<String> id, final Date date) {
		logger.info("Updating AD profiles..");
		clientActiveDirectoryService.update(new ClientActiveDirectoryProfileUpdateContext() {
			@Override
			public List<String> getProfilesNumbers() {
				return id;
			}
			
			@Override
			public Date getDismissalDate() {
				return date;
			}
		});
	}
	
}
