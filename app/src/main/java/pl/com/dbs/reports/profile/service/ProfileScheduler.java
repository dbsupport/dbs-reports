/**
 * 
 */
package pl.com.dbs.reports.profile.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import pl.com.dbs.reports.access.domain.Access;
import pl.com.dbs.reports.api.profile.ClientProfile;
import pl.com.dbs.reports.api.profile.ClientProfileAuthority;
import pl.com.dbs.reports.api.profile.ClientProfileService;
import pl.com.dbs.reports.authority.dao.AuthorityDao;
import pl.com.dbs.reports.authority.domain.Authority;
import pl.com.dbs.reports.parameter.service.ParameterService;
import pl.com.dbs.reports.profile.domain.ClientProfilesFilter;
import pl.com.dbs.reports.profile.domain.Profile;
import pl.com.dbs.reports.profile.domain.ProfileException;
import pl.com.dbs.reports.profile.domain.ProfileGlobalCreation;
import pl.com.dbs.reports.support.encoding.EncodingContext;
import pl.com.dbs.reports.support.encoding.EncodingService;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

/**
 * Tasks to synchronize profiles between CLIENT db and local db.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2013
 */
@Service("profile.scheduler")
public class ProfileScheduler {
	private static final Logger logger = LoggerFactory.getLogger(ProfileScheduler.class);
	@Autowired(required=false) private ClientProfileService clientProfileService;
	@Autowired private ProfileService profileService;
	@Autowired private AuthorityDao authorityDao;
	@Autowired private ParameterService parameterService;
	@Autowired private EncodingService encodingService;
	
	/**
	 * Temp. For tests.
	 */
	public List<ClientProfile> findClientProfiles(ClientProfilesFilter filter) {
		parameterService.edit("client.db.encoding", filter.getInEncoding()!=null?filter.getInEncoding().getBytes():null, null);
		parameterService.edit("local.db.encoding", filter.getOutEncoding()!=null?filter.getOutEncoding().getBytes():null, null);
		
		try {
			return encode(clientProfileService.find(filter));
		} catch (Exception e) {
			logger.error("Error reading profile.", e);
			return new ArrayList<ClientProfile>();
		}
	}
	
	@Scheduled(cron="0 0 7 * * ?")
	public void synchronize() {
		if (clientProfileService==null) return;
		logger.debug("Profiles synchronization..");
		
		ClientProfileFilterDefault filter = new ClientProfileFilterDefault(50);
		List<ClientProfile> profiles;
		do {
			profiles = encode(clientProfileService.find(filter));
			logger.debug("Found "+profiles.size()+" profiles!");
			
			for (ClientProfile clientprofile : profiles) {
				synchronize(clientprofile);
				filter.next(clientprofile.getId());
			}
		} while (profiles!=null&&!profiles.isEmpty());
	}
	
	private List<ClientProfile> encode(List<ClientProfile> profiles) {
		final EncodingContext context = encodingService.getEncodingContext();
		return Lists.transform(profiles, new Function<ClientProfile, ClientProfile>() {
			@Override
			public ClientProfile apply(final ClientProfile input) {
				return new ClientProfile() {
					@Override
					public String getId() {
						return input.getId();
					}
					@Override
					public String getFirstName() {
						return encodingService.encode(input.getFirstName(), context);
					}
					@Override
					public String getLastName() {
						return encodingService.encode(input.getLastName(), context);
					}
					@Override
					public String getDescription() {
						return encodingService.encode(input.getDescription(), context);
					}
					@Override
					public String getLogin() {
						return input.getLogin();
					}
					@Override
					public String getProfile() {
						return input.getProfile();
					}
					@Override
					public List<ClientProfileAuthority> getAuthorities() {
						return input.getAuthorities();
					}
				};
			}});		
		
	}
	
	private void synchronize(final ClientProfile clientprofile) {
		logger.debug("Synchronizing: "+clientprofile.getLogin());
		
		Profile profile = profileService.findByUuid(clientprofile.getId());
		if (profile!=null&&profile.isActive()) {
			//..update description..
			profileService.modify(profile.getId(), clientprofile);
			return;
		}

		if (profile==null) {
			//..create new one..
			create(clientprofile);
		}
	}
	

		
	private void create(final ClientProfile clientprofile) {
		logger.info("Profile "+clientprofile.getLogin()+" NOT found locally. Creating one..");

		try {
			//..default add user authority..
			final Authority authority = authorityDao.findByName(Authority.USER);
			
			
			profileService.add(new ProfileGlobalCreation() {
				@Override
				public String getLogin() {
					return clientprofile.getLogin();
				}
	
				@Override
				public String getPasswd() {
					return null;
				}
	
				@Override
				public String getFirstName() {
					return clientprofile.getFirstName();
				}
	
				@Override
				public String getLastName() {
					return clientprofile.getLastName();
				}
	
				@Override
				public String getEmail() {
					return null;
				}
	
				@Override
				public String getPhone() {
					return null;
				}
	
				@Override
				public Address getAddress() {
					return null;
				}
	
				@Override
				public File getPhoto() {
					return null;
				}
	
				@Override
				public boolean isAccepted() {
					return false;
				}
	
				@Override
				public List<Authority> getAuthorities() {
					return authority!=null?Arrays.asList(authority):new ArrayList<Authority>();
				}
	
				@Override
				public List<Access> getAccesses() {
					return null;
				}

				@Override
				public String getUuid() {
					return clientprofile.getId();
				}

				@Override
				public List<ClientProfileAuthority> getClientAuthorities() {
					return clientprofile.getAuthorities();
				}

				@Override
				public String getDescription() {
					return clientprofile.getDescription();
				}
				
			});
		} catch (ProfileException e) {
			logger.error("Error synchronizing profile"+clientprofile.getId()+" Details: "+e.getMessage());
		}
		
	}
		
}
