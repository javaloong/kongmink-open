package org.javaloong.kongmink.open.liquibase.extender;

public class LiquibaseExtenderException extends Exception {

    private static final long serialVersionUID = 1L;

    public LiquibaseExtenderException(String message) {
        super(message);
    }

    public LiquibaseExtenderException(String message, Throwable e) {
        super(message, e);
    }
}
