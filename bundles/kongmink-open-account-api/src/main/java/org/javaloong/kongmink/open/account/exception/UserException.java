package org.javaloong.kongmink.open.account.exception;

import org.javaloong.kongmink.open.common.exception.ErrorCodeException;

public class UserException extends ErrorCodeException {

    public static final String USERNAME_EXISTS = "UsernameExists";
    public static final String EMAIL_EXISTS = "EmailExists";
    public static final String PASSWORD_NOT_MATCH = "PasswordNotMatch";

    public static UserException usernameExistsException() {
        return new UserException(USERNAME_EXISTS);
    }

    public static UserException emailExistsException() {
        return new UserException(EMAIL_EXISTS);
    }

    public static UserException passwordNotMatchException() {
        return new UserException(PASSWORD_NOT_MATCH);
    }

    public UserException(String errorCode) {
        super(errorCode);
    }

    public UserException(String message, String errorCode) {
        super(message, errorCode);
    }

    public UserException(String message, String errorCode, Object... args) {
        super(message, errorCode, args);
    }

    public UserException(String message, Throwable cause, String errorCode, Object... args) {
        super(message, cause, errorCode, args);
    }
}
