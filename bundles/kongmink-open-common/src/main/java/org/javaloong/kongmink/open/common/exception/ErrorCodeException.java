package org.javaloong.kongmink.open.common.exception;

public class ErrorCodeException extends RuntimeException {

    private final String errorCode;
    private final Object[] args;

    public ErrorCodeException(String errorCode) {
        this(errorCode, errorCode);
    }

    public ErrorCodeException(String message, String errorCode) {
        this(message, null, errorCode);
    }

    public ErrorCodeException(String errorCode, Object... args) {
        this(errorCode, errorCode, args);
    }

    public ErrorCodeException(String message, Throwable cause, String errorCode){
        this(errorCode, cause, message, (Object[]) null);
    }

    public ErrorCodeException(String message, String errorCode, Object... args) {
        super(message);
        this.errorCode = errorCode;
        this.args = args;
    }

    public ErrorCodeException(String message, Throwable cause, String errorCode, Object... args) {
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

