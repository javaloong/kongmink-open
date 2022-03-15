package org.javaloong.kongmink.open.am.exception;

import org.javaloong.kongmink.open.common.exception.ErrorCodeException;

public class ClientException extends ErrorCodeException {

    public static final String CLIENT_EXISTS = "ClientExists";

    public static ClientException clientExistsException() {
        return new ClientException(CLIENT_EXISTS);
    }

    public ClientException(String errorCode) {
        super(errorCode);
    }

    public ClientException(String message, String errorCode) {
        super(message, errorCode);
    }

    public ClientException(String message, String errorCode, Object... args) {
        super(message, errorCode, args);
    }

    public ClientException(String message, Throwable cause, String errorCode, Object... args) {
        super(message, cause, errorCode, args);
    }
}
