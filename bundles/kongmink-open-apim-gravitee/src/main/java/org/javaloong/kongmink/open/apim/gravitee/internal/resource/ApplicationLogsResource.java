package org.javaloong.kongmink.open.apim.gravitee.internal.resource;

import org.javaloong.kongmink.open.apim.gravitee.internal.model.LogEntity;
import org.javaloong.kongmink.open.apim.gravitee.internal.resource.param.LogsParam;
import org.javaloong.kongmink.open.apim.gravitee.internal.resource.param.PaginationParam;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

public interface ApplicationLogsResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    DataResponse<LogEntity> applicationLogs(
            @BeanParam PaginationParam paginationParam,
            @BeanParam LogsParam logsParam);

    @GET
    @Path("/{logId}")
    @Produces(MediaType.APPLICATION_JSON)
    LogEntity applicationLog(
            @PathParam("logId") String logId,
            @QueryParam("timestamp") Long timestamp);
}
