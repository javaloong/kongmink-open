package org.javaloong.kongmink.open.rest.core.internal.validation;

import org.apache.cxf.feature.Feature;
import org.apache.cxf.jaxrs.validation.JAXRSBeanValidationFeature;
import org.apache.cxf.validation.BeanValidationProvider;
import org.hibernate.validator.HibernateValidator;
import org.javaloong.kongmink.open.rest.RESTConstants;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.jaxrs.whiteboard.JaxrsWhiteboardConstants;
import org.osgi.service.jaxrs.whiteboard.propertytypes.JaxrsApplicationSelect;
import org.osgi.service.jaxrs.whiteboard.propertytypes.JaxrsExtension;

import javax.script.ScriptEngineFactory;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;

import static java.lang.Thread.currentThread;

@Component(service = Feature.class, immediate = true)
@JaxrsExtension
@JaxrsApplicationSelect("(" + JaxrsWhiteboardConstants.JAX_RS_NAME + "=" + RESTConstants.JAX_RS_NAME + ")")
public class BeanValidationFeature extends JAXRSBeanValidationFeature {

    @Activate
    public void activate() {
        final ClassLoader ldr = currentThread().getContextClassLoader();
        currentThread().setContextClassLoader(HibernateValidator.class.getClassLoader());
        try {
            ValidatorFactory validatorFactory = Validation.byProvider(HibernateValidator.class)
                    .configure()
                    .scriptEvaluatorFactory(new MultiClassLoaderScriptEvaluatorFactory(ScriptEngineFactory.class.getClassLoader()))
                    .buildValidatorFactory();
            BeanValidationProvider validationProvider = new BeanValidationProvider(validatorFactory);
            this.setProvider(validationProvider);
        } finally {
            currentThread().setContextClassLoader(ldr);
        }
    }
}
