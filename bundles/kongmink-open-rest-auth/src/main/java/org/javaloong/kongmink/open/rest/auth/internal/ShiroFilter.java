package org.javaloong.kongmink.open.rest.auth.internal;

import org.apache.shiro.util.LifecycleUtils;
import org.apache.shiro.web.env.IniWebEnvironment;
import org.apache.shiro.web.env.WebEnvironment;
import org.apache.shiro.web.filter.mgt.FilterChainResolver;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.javaloong.kongmink.open.rest.RESTConstants;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ServiceScope;
import org.osgi.service.http.whiteboard.HttpWhiteboardConstants;
import org.osgi.service.http.whiteboard.propertytypes.HttpWhiteboardContextSelect;
import org.osgi.service.http.whiteboard.propertytypes.HttpWhiteboardFilterPattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Filter;

import static java.lang.Thread.currentThread;

@Component(service = Filter.class, scope = ServiceScope.PROTOTYPE,
        configurationPid = {ShiroConfiguration.SHIRO_CONFIGURATION_PID})
@HttpWhiteboardContextSelect("(" + HttpWhiteboardConstants.HTTP_WHITEBOARD_CONTEXT_NAME + "=" + ShiroFilter.CONTEXT_NAME + ")")
@HttpWhiteboardFilterPattern("/*")
public class ShiroFilter extends AbstractShiroFilter {

    private static final Logger LOG = LoggerFactory.getLogger(ShiroFilter.class);

    public static final String CONTEXT_NAME = "context.for" + RESTConstants.JAX_RS_NAME;

    private WebEnvironment env;

    private ShiroConfiguration shiroConfig;

    @Activate
    protected void activate(ShiroConfiguration shiroConfig) {
        this.shiroConfig = shiroConfig;
    }

    @Override
    public void init() {
        LOG.info("Initializing Shiro environment");
        env = createWebEnvironment();
        setSecurityManager(env.getWebSecurityManager());

        FilterChainResolver resolver = env.getFilterChainResolver();
        if (resolver != null) {
            setFilterChainResolver(resolver);
        }
    }

    private WebEnvironment createWebEnvironment() {
        final ClassLoader ldr = currentThread().getContextClassLoader();
        currentThread().setContextClassLoader(this.getClass().getClassLoader());
        try {
            IniWebEnvironment environment = new ShiroIniWebEnvironment(shiroConfig.shiro_ini_file());
            environment.setServletContext(getServletContext());
            environment.init();
            return environment;
        } finally {
            currentThread().setContextClassLoader(ldr);
        }
    }

    @Override
    public void destroy() {
        LOG.info("Cleaning up Shiro Environment");
        LifecycleUtils.destroy(env);
    }
}
