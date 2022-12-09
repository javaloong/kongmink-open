package org.javaloong.kongmink.open.rest.portal.internal.resource;

import org.javaloong.kongmink.open.common.model.*;
import org.javaloong.kongmink.open.rest.RESTConstants;
import org.javaloong.kongmink.open.service.ApiService;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.jaxrs.whiteboard.JaxrsWhiteboardConstants;
import org.osgi.service.jaxrs.whiteboard.propertytypes.JSONRequired;
import org.osgi.service.jaxrs.whiteboard.propertytypes.JaxrsApplicationSelect;
import org.osgi.service.jaxrs.whiteboard.propertytypes.JaxrsName;
import org.osgi.service.jaxrs.whiteboard.propertytypes.JaxrsResource;

import javax.ws.rs.*;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;
import java.util.Optional;

@Component(service = ApiResource.class)
@JaxrsResource
@JaxrsName(ApiResource.RESOURCE_NAME)
@JaxrsApplicationSelect("(" + JaxrsWhiteboardConstants.JAX_RS_NAME + "=" + RESTConstants.JAX_RS_NAME + ")")
@JSONRequired
@Path("/apis")
@Produces(MediaType.APPLICATION_JSON)
public class ApiResource extends AbstractResource {

    static final String RESOURCE_NAME = "api";

    @Reference
    ApiService apiService;

    @GET
    @Path("/categories")
    public Response getCategories() {
        Collection<Category> result = apiService.getCategories();
        return Response.ok(result).build();
    }

    @GET
    @Path("/{id}/metrics")
    public Response getMetrics(@PathParam("id") String id) {
        ApiMetrics result = apiService.getMetrics(id);
        return Response.ok(result).build();
    }

    @GET
    @Path("/{id}/pages/{pageId}")
    public Response getApiPage(@HeaderParam(HttpHeaders.ACCEPT_LANGUAGE) String acceptLang,
                               @PathParam("id") String id,
                               @PathParam("pageId") String pageId) {
        ApiPage result = apiService.getPage(id, pageId, acceptLang);
        return Response.ok(result).build();
    }

    @GET
    @Path("/{id}/pages")
    public Response getApiPages(@HeaderParam(HttpHeaders.ACCEPT_LANGUAGE) String acceptLang,
                                @PathParam("id") String id,
                                @QueryParam("parent") String parent,
                                @DefaultValue("1") @QueryParam("page") int page,
                                @DefaultValue("10") @QueryParam("size") int size) {
        Page<ApiPage> result = apiService.getPages(id, acceptLang, parent, page, size);
        return Response.ok(result).build();
    }

    @GET
    @Path("/{id}/plans")
    public Response getPlans(@PathParam("id") String id,
                             @DefaultValue("1") @QueryParam("page") int page,
                             @DefaultValue("10") @QueryParam("size") int size) {
        Page<Plan> result = apiService.getPlans(id, page, size);
        return Response.ok(result).build();
    }

    @GET
    @Path("/{id}")
    public Response getApi(@PathParam("id") String id) {
        Optional<Api> result = apiService.findById(id);
        return result.map(api -> Response.ok(api).build())
                .orElseGet(() -> Response.status(Response.Status.NOT_FOUND).build());
    }

    @GET
    public Response getApis(@QueryParam("category") String category,
                            @DefaultValue("1") @QueryParam("page") int page,
                            @DefaultValue("10") @QueryParam("size") int size) {
        Page<Api> result = apiService.findAll(category, page, size);
        return Response.ok(result).build();
    }

    @POST
    @Path("/_search")
    public Response searchApis(@QueryParam("q") String query,
                               @DefaultValue("1") @QueryParam("page") int page,
                               @DefaultValue("10") @QueryParam("size") int size) {
        Page<Api> result = apiService.search(query, page, size);
        return Response.ok(result).build();
    }
}
