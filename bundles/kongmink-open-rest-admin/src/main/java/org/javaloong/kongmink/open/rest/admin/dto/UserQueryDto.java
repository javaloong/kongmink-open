package org.javaloong.kongmink.open.rest.admin.dto;

import org.javaloong.kongmink.open.common.model.user.query.UserQuery;

import javax.ws.rs.QueryParam;

public class UserQueryDto {

    public enum UserQueryField {
        ID, USERNAME
    }

    @QueryParam("from")
    private long from;
    @QueryParam(("to"))
    private long to;
    @QueryParam("query")
    private String query;
    @QueryParam("field")
    private UserQueryField field;

    public long getFrom() {
        return from;
    }

    public void setFrom(long from) {
        this.from = from;
    }

    public long getTo() {
        return to;
    }

    public void setTo(long to) {
        this.to = to;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public UserQueryField getField() {
        return field;
    }

    public void setField(UserQueryField field) {
        this.field = field;
    }

    public UserQuery toUserQuery() {
        UserQuery userQuery = new UserQuery();
        userQuery.setFrom(from);
        userQuery.setTo(to);
        userQuery.setQuery(query);
        userQuery.setField(toFieldString(field));
        return userQuery;
    }

    private String toFieldString(UserQueryField field) {
        return field != null ? field.toString().toLowerCase() : null;
    }
}
