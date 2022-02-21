package org.javaloong.kongmink.open.rest.openapi.internal;

import io.swagger.v3.oas.annotations.Operation;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

public class TestOpenApiResource {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/operation")
    @Operation(description = "operation")
    public String operation() {
        return "operation";
    }
}
