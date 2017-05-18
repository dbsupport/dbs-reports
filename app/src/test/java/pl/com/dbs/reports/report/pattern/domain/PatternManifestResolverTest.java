package pl.com.dbs.reports.report.pattern.domain;

import com.google.common.collect.Lists;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import pl.com.dbs.reports.api.report.pattern.PatternFactory;
import pl.com.dbs.reports.api.report.pattern.PatternFactoryNotFoundException;
import pl.com.dbs.reports.report.domain.ReportFactoryDefault;

import java.util.List;

import static org.junit.Assert.assertNotNull;

/**
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2017
 */
public class PatternManifestResolverTest {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void should_find_factory_by_canonical_name() throws PatternFactoryNotFoundException {
		List<? extends PatternFactory> factories = Lists.newArrayList(new DummyFactory(), new PatternFactoryDefault());
		PatternManifestResolver resolver = new PatternManifestResolver((List<PatternFactory>)factories);

		PatternFactory factory = resolver.resolveFactory("pl.com.dbs.reports.report.pattern.domain.PatternFactoryDefault");
		assertNotNull(factory);
	}

	@Test
	public void should_find_factory_by_simple_name() throws PatternFactoryNotFoundException {
		List<? extends PatternFactory> factories = Lists.newArrayList(new DummyFactory(), new PatternFactoryDefault());
		PatternManifestResolver resolver = new PatternManifestResolver((List<PatternFactory>)factories);

		PatternFactory factory = resolver.resolveFactory("DummyFactory");
		assertNotNull(factory);
	}

	@Test
	public void should_not_find_factory() throws PatternFactoryNotFoundException {
		thrown.expect(PatternFactoryNotFoundException.class);
		List<? extends PatternFactory> factories = Lists.newArrayList(new DummyFactory(), new PatternFactoryDefault());
		PatternManifestResolver resolver = new PatternManifestResolver((List<PatternFactory>)factories);

		PatternFactory factory = resolver.resolveFactory("SomeOtherFactory");
	}
}
