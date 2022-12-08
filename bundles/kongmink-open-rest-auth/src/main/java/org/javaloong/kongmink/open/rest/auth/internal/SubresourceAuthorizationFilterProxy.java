package org.javaloong.kongmink.open.rest.auth.internal;

import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.authz.annotation.*;
import org.apache.shiro.web.jaxrs.AnnotationAuthorizationFilter;

import javax.ws.rs.ForbiddenException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

public class SubresourceAuthorizationFilterProxy implements ContainerRequestFilter {

    private static final List<Class<? extends Annotation>> shiroAnnotations = Collections.unmodifiableList(Arrays.asList(
            RequiresPermissions.class,
            RequiresRoles.class,
            RequiresAuthentication.class,
            RequiresUser.class,
            RequiresGuest.class));

    private static final Map<Method, ContainerRequestFilter> authorizationFilters = new HashMap<>();
    private static final Object lockObject = new Object();

    @Context
    ResourceInfo resourceInfo;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        // Subresource has two or more matching resources
        if (requestContext.getUriInfo().getMatchedResources().size() > 1) {
            ContainerRequestFilter authorizationFilter = getAuthorizationFilter(resourceInfo);
            if (authorizationFilter != null) {
                try {
                    authorizationFilter.filter(requestContext);
                } catch (UnauthorizedException e) {
                    throw new ForbiddenException(e);
                }
            }
        }
    }

    private static ContainerRequestFilter getAuthorizationFilter(ResourceInfo resourceInfo) {
        ContainerRequestFilter authorizationFilter = authorizationFilters.get(resourceInfo.getResourceMethod());
        if (authorizationFilter == null) {
            synchronized (lockObject) {
                if (authorizationFilter == null) {
                    List<Annotation> authzSpecs = getAuthzSpecs(resourceInfo);
                    if (!authzSpecs.isEmpty()) {
                        authorizationFilter = new AnnotationAuthorizationFilter(authzSpecs);
                    } else {
                        authorizationFilter = EmptyRequestFilter.Instance;
                    }
                    authorizationFilters.put(resourceInfo.getResourceMethod(), authorizationFilter);
                }
            }
        }
        return authorizationFilter;
    }

    private static List<Annotation> getAuthzSpecs(ResourceInfo resourceInfo) {
        List<Annotation> authzSpecs = new ArrayList<>();
        for (Class<? extends Annotation> annotationClass : shiroAnnotations) {
            // XXX What is the performance of getAnnotation vs getAnnotations?
            Annotation classAuthzSpec = resourceInfo.getResourceClass().getAnnotation(annotationClass);
            Annotation methodAuthzSpec = resourceInfo.getResourceMethod().getAnnotation(annotationClass);

            if (classAuthzSpec != null) authzSpecs.add(classAuthzSpec);
            if (methodAuthzSpec != null) authzSpecs.add(methodAuthzSpec);
        }
        return authzSpecs;
    }

    static class EmptyRequestFilter implements ContainerRequestFilter {

        static final EmptyRequestFilter Instance = new EmptyRequestFilter();

        @Override
        public void filter(ContainerRequestContext requestContext) throws IOException {

        }
    }
}
