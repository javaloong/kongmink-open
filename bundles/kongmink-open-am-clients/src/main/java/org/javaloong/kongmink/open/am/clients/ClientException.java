package org.javaloong.kongmink.open.am.clients;

public class ClientException extends RuntimeException {

    public static final String CLIENT_EXISTS = "ClientExists";

    public static ClientException clientExistsException() {
        return new ClientException(CLIENT_EXISTS);
    }

    private final String errorCode;
    private final Object[] args;

    public ClientException(String errorCode) {
        this(errorCode, errorCode);
    }

    public ClientException(String errorCode, String message) {
        this(errorCode, message, null);
    }

    public ClientException(String errorCode, String message, Object[] args) {
        super(message);
        this.errorCode = errorCode;
        this.args = args;
    }

    public ClientException(String errorCode, Throwable cause, String message, Object[] args) {
        super(message, cause);
        this.errorCode = errorCode;
        this.args = args;
    }

    public String getErrorCode() {
        return errorCode;
    }


    public Object[] getArgs() {
        return args;
    }
}
