/*
 * Created on 2006-03-29
 */
package pl.com.dbs.reports.support.web;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.ContextLoaderListener;


/**
 * @author krzysztof.kaziura@gmail.com
 *
 */
public class CSpringSingletonContextLoaderListener extends ContextLoaderListener {
    protected ContextLoader createContextLoader() {
        return new CSpringSingletonContextLoader();
    }
}
