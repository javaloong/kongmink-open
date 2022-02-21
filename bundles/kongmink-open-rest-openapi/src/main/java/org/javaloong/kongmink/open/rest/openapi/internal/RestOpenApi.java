package org.javaloong.kongmink.open.rest.openapi.internal;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.javaloong.kongmink.open.rest.RESTConstants;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.jaxrs.whiteboard.JaxrsWhiteboardConstants;
import org.osgi.service.jaxrs.whiteboard.propertytypes.JaxrsApplicationSelect;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@ObjectClassDefinition(name = "Open Api Jaxrs Configuration")
@interface OpenApiJaxrsConfiguration {

    String description() default "Service REST API";

    String title() default "My Service";

    String contact() default "me@me.com";
}

@Component(service = OpenAPI.class)
@Designate(ocd = OpenApiJaxrsConfiguration.class)
@JaxrsApplicationSelect("(" + JaxrsWhiteboardConstants.JAX_RS_NAME + "=" + RESTConstants.JAX_RS_NAME + ")")
public class RestOpenApi extends OpenAPI {

    @Activate
    public RestOpenApi(OpenApiJaxrsConfiguration openApiJaxrsConfiguration) {
        super();

        info(new Info()
                .title(openApiJaxrsConfiguration.title())
                .description(openApiJaxrsConfiguration.description())
                .contact(new Contact()
                        .email(openApiJaxrsConfiguration.contact())));

        registerComponents(securityDefinitions()).ifPresent(this::components);
    }

    private Map<String, SecurityScheme> securityDefinitions() {
        return Collections.singletonMap(RESTConstants.SECURITY_BEARER_AUTH,
                new SecurityScheme()
                        .name(RESTConstants.SECURITY_BEARER_AUTH)
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT"));
    }

    private static Optional<Components> registerComponents(Map<String, SecurityScheme> securityDefinitions) {
        final Components components = new Components();

        boolean hasComponents = false;
        if (securityDefinitions != null && !securityDefinitions.isEmpty()) {
            securityDefinitions.forEach(components::addSecuritySchemes);
            hasComponents = true;
        }

        return hasComponents ? Optional.of(components) : Optional.empty();
    }
}
