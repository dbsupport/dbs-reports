/*
 * Created on 2006-03-29
 */
package pl.com.dbs.reports.support.web;

import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.access.BeanFactoryLocator;
import org.springframework.beans.factory.access.BeanFactoryReference;
import org.springframework.beans.factory.access.SingletonBeanFactoryLocator;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoader;

/**
 * Ladowacz konktekstu springa, ktory uzywa SingletonBeanFactoryLocator zamiast
 * ContextSingletonBeanFactoryLocator. 
 * 
 * @author krzysztof.kaziura@gmail.com
 */
public class CSpringSingletonContextLoader extends ContextLoader {
    private final Log logger = LogFactory.getLog(CSpringSingletonContextLoader.class);
    private BeanFactoryReference parentFactoryRef;

    protected ApplicationContext loadParentContext(ServletContext servletContext) throws BeansException {
        String locatorFactorySelector = servletContext.getInitParameter(LOCATOR_FACTORY_SELECTOR_PARAM);
        String parentContextKey = servletContext.getInitParameter(LOCATOR_FACTORY_KEY_PARAM);

        if (parentContextKey != null) {
            BeanFactoryLocator locator = (locatorFactorySelector != null) ?
                SingletonBeanFactoryLocator.getInstance(locatorFactorySelector) :
                    SingletonBeanFactoryLocator.getInstance();
    
            if (logger.isInfoEnabled()) {
                logger.info("Getting parent context definition: using parent context key of '" +
                        parentContextKey + "' with BeanFactoryLocator");
            }
    
            this.parentFactoryRef = locator.useBeanFactory(parentContextKey);
            return (ApplicationContext) this.parentFactoryRef.getFactory();
        } else {
            return null;
        }
    }

    public void closeWebApplicationContext(ServletContext servletContext) {
        try {
            super.closeWebApplicationContext(servletContext);
        } finally {
            if (this.parentFactoryRef != null) {
                this.parentFactoryRef.release();
            }
        }
    }
}
