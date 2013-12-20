/*
 * Created on 2006-03-29
 */
package pl.com.dbs.reports.support.web.context;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.ContextLoaderListener;

/**
 * TODO
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
public class SpringSingletonContextLoaderListener extends ContextLoaderListener {
    protected ContextLoader createContextLoader() {
        return new SpringSingletonContextLoader();
    }
}
