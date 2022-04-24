package org.javaloong.kongmink.open.apim.gravitee.internal.resource;

import org.javaloong.kongmink.open.apim.gravitee.internal.model.PageEntity;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

public interface ApiPageResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    PageEntity getPageByApiIdAndPageId(
            @HeaderParam("Accept-Language") String acceptLang,
            @QueryParam("include") List<String> include);

    @GET
    @Path("content")
    @Produces(MediaType.TEXT_PLAIN)
    Response getPageContentByApiIdAndPageId();
}
