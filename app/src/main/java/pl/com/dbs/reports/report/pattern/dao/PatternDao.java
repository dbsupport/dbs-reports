/**
 * 
 */
package pl.com.dbs.reports.report.pattern.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import pl.com.dbs.reports.profile.domain.Profile;
import pl.com.dbs.reports.profile.domain.Profile_;
import pl.com.dbs.reports.report.pattern.domain.ReportPattern;
import pl.com.dbs.reports.report.pattern.domain.ReportPattern_;
import pl.com.dbs.reports.support.db.dao.ADao;
import pl.com.dbs.reports.support.db.dao.ContextDao;
import pl.com.dbs.reports.support.db.dao.IContextDao;


/**
 * Pattern CRUD.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
@Repository
public class PatternDao extends ADao<ReportPattern, Long> {
	@PersistenceContext
	private EntityManager em;

	@Override
	public EntityManager getEntityManager() {
		return em;
	}
	
	public ReportPattern findSingle(PatternFilter filter) {
		List<ReportPattern> patterns = find(filter);
		return patterns!=null&&!patterns.isEmpty()?patterns.get(0):null;
	}
	
	public List<ReportPattern> find(final PatternFilter filter) {
		IContextDao<ReportPattern> c = new ContextDao<ReportPattern>(em, ReportPattern.class, filter);
	    
		Predicate p = c.getBuilder().conjunction();
	    p = c.getBuilder().and(p, c.getBuilder().equal(c.getRoot().get(ReportPattern_.active), 1));
	    
	    if (!filter.getAccesses().isEmpty()) {
	    	p = c.getBuilder().and(p, c.getRoot().get(ReportPattern_.accesses).in(filter.getAccesses()));
	    } else {
	    	p = c.getBuilder().and(p, c.getRoot().get(ReportPattern_.accesses).isNull());
	    }
	    if (!StringUtils.isBlank(filter.getName())) {
	    	Predicate ex1 = c.getBuilder().like(c.getBuilder().upper(c.getRoot().<String>get(ReportPattern_.name)), "%"+filter.getName().toUpperCase()+"%");
	    	Predicate ex2 = c.getBuilder().like(c.getBuilder().upper(c.getRoot().<String>get(ReportPattern_.version)), "%"+filter.getName().toUpperCase()+"%");
	    	Predicate ex3 = c.getBuilder().like(c.getBuilder().upper(c.getRoot().<String>get(ReportPattern_.author)), "%"+filter.getName().toUpperCase()+"%");
	    	Predicate ex4 = c.getBuilder().like(c.getBuilder().upper(c.getRoot().<String>get(ReportPattern_.factory)), "%"+filter.getName().toUpperCase()+"%");
	    	
	    	Join<ReportPattern, Profile> a = c.getRoot().join(ReportPattern_.creator, JoinType.LEFT);
	    	Predicate ex5 = c.getBuilder().like(c.getBuilder().upper(a.<String>get(Profile_.firstname)), "%"+filter.getName().toUpperCase()+"%");
	    	Predicate ex6 = c.getBuilder().like(c.getBuilder().upper(a.<String>get(Profile_.lastname)), "%"+filter.getName().toUpperCase()+"%");
	    	Predicate ex7 = c.getBuilder().like(c.getBuilder().upper(a.<String>get(Profile_.description)), "%"+filter.getName().toUpperCase()+"%");
	    	Predicate ex8 = c.getBuilder().like(c.getBuilder().upper(a.<String>get(Profile_.login)), "%"+filter.getName().toUpperCase()+"%");
	    	p = c.getBuilder().or(ex1, ex2, ex3, ex4, ex5, ex6, ex7, ex8);	    	
	    }
//	    if (!StringUtils.isBlank(filter.getVersion())) {
//	    	p = c.getBuilder().and(p, c.getBuilder().like(c.getBuilder().upper(c.getRoot().<String>get("version")), "%"+filter.getVersion().toUpperCase()+"%"));
//	    }
//	    if (!StringUtils.isBlank(filter.getFactory())) {
//	    	p = c.getBuilder().and(p, c.getBuilder().like(c.getBuilder().upper(c.getRoot().<String>get("factory")), "%"+filter.getFactory().toUpperCase()+"%"));
//	    }
	    if (filter.getId()!=null) {
	    	p = c.getBuilder().and(p, c.getBuilder().equal(c.getRoot().get(ReportPattern_.id), filter.getId()));
	    }
	    if (filter.getProfileId()!=null) {
	    	p = c.getBuilder().and(p, c.getBuilder().equal(c.getRoot().get(ReportPattern_.creator).get(Profile_.id), filter.getProfileId()));
	    }
	    	    
	    
	    c.getCriteria().where(p);
	    
		return executeQuery(c);
	}
	
	/**
	 * active/name/version/factory..
	 */
	public List<ReportPattern> findExactMatch(final PatternFilter filter) {
		IContextDao<ReportPattern> c = new ContextDao<ReportPattern>(em, ReportPattern.class, filter);
	    
		Predicate p = c.getBuilder().conjunction();
	    p = c.getBuilder().and(p, c.getBuilder().equal(c.getRoot().get(ReportPattern_.active), 1));
	    
	    if (!StringUtils.isBlank(filter.getName())) {
	    	p = c.getBuilder().and(p, c.getBuilder().equal(c.getRoot().get(ReportPattern_.name), filter.getName()));
	    }
	    if (!StringUtils.isBlank(filter.getVersion())) {
	    	p = c.getBuilder().and(p, c.getBuilder().equal(c.getRoot().get(ReportPattern_.version), filter.getVersion()));
	    }
	    if (!StringUtils.isBlank(filter.getFactory())) {
	    	p = c.getBuilder().and(p, c.getBuilder().equal(c.getRoot().get(ReportPattern_.factory), filter.getFactory()));
	    }
	    
	    c.getCriteria().where(p);
	    
		return executeQuery(c);
	}	
}
