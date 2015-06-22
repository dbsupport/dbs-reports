/**
 * 
 */
package pl.com.dbs.reports.profile.dao;

import com.google.common.base.Strings;
import org.apache.commons.lang.Validate;
import org.springframework.stereotype.Repository;
import pl.com.dbs.reports.access.domain.Access;
import pl.com.dbs.reports.access.domain.Access_;
import pl.com.dbs.reports.profile.domain.Profile;
import pl.com.dbs.reports.profile.domain.ProfileGroup;
import pl.com.dbs.reports.profile.domain.ProfileGroup_;
import pl.com.dbs.reports.profile.domain.Profile_;
import pl.com.dbs.reports.support.db.dao.ADao;
import pl.com.dbs.reports.support.db.dao.ContextDao;
import pl.com.dbs.reports.support.db.dao.IContextDao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.util.List;

/**
 * Profile group CRUD.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2013
 */
@Repository
public class ProfileGroupDao extends ADao<ProfileGroup, Long> {

	@PersistenceContext
	private EntityManager em;	
	
	@Override
	public EntityManager getEntityManager() {
		return em;
	}
	
	@Override
	public ProfileGroup find(Long id) {
		Validate.notNull(id, "Id is no more!");
		IContextDao<ProfileGroup> context = new ContextDao<ProfileGroup>(em, ProfileGroup.class);

        CriteriaBuilder builder = context.getBuilder();
        Root<ProfileGroup> root = context.getRoot();
        Predicate p = builder.conjunction();

        p = builder.and(p, builder.equal(root.get(ProfileGroup_.id), id));

        context.getCriteria().where(p);
        return executeQuerySingle(context);
	}

    public ProfileGroup findByName(String name) {
        Validate.isTrue(!Strings.isNullOrEmpty(name), "Name is no more!");
        IContextDao<ProfileGroup> context = new ContextDao<ProfileGroup>(em, ProfileGroup.class);

        CriteriaBuilder builder = context.getBuilder();
        Root<ProfileGroup> root = context.getRoot();
        Predicate p = builder.conjunction();

        p = builder.and(p, builder.equal(builder.upper(root.<String>get(ProfileGroup_.name)), name.toUpperCase()));

	    context.getCriteria().where(p);
        return executeQuerySingle(context);
    }

	public List<ProfileGroup> find(ProfileGroupsFilter filter) {
		Validate.notNull(filter, "Filter is no more!");
		IContextDao<ProfileGroup> context = new ContextDao<ProfileGroup>(em, ProfileGroup.class, filter);

        CriteriaBuilder builder = context.getBuilder();
        Root<ProfileGroup> root = context.getRoot();
        Predicate p = builder.conjunction();

	    if (filter.getName()!=null) {
	    	p = builder.and(p, builder.like(builder.upper(root.<String>get(ProfileGroup_.name)), "%" + filter.getName().toUpperCase() + "%"));
	    }
        if (filter.getGidn()!=null) {
            p = builder.and(p, builder.equal(root.get(ProfileGroup_.id), filter.getGidn()).not());
        }
        if (filter.getPid()!=null) {
            ListJoin<ProfileGroup, Profile> profiles = root.join(ProfileGroup_.profiles);
            p = builder.and(p, profiles.get(Profile_.id).in(filter.getPid()));
        }
        if (filter.hasAccesses()) {
            Join<ProfileGroup, Access> accesses =  root.join(ProfileGroup_.accesses, JoinType.LEFT);
            p = builder.and(p, accesses.get(Access_.id).in(filter.getAccesses()));
        }
        if (filter.hasGroups()) {
            p = builder.and(p, root.get(ProfileGroup_.id).in(filter.getGroups()));
        }


        context.getCriteria().where(p);
	    return executeQuery(context);
	}

	
}
