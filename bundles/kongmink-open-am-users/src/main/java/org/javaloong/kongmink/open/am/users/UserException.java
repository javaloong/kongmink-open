package org.javaloong.kongmink.open.am.users;

public class UserException extends RuntimeException {

    public static final String USERNAME_EXISTS = "UsernameExists";
    public static final String EMAIL_EXISTS = "emailExists";
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

    private final String errorCode;
    private final Object[] args;

    public UserException(String errorCode) {
        this(errorCode, errorCode);
    }

    public UserException(String errorCode, String message) {
        this(errorCode, message, null);
    }

    public UserException(String errorCode, String message, Object[] args) {
        super(message);
        this.errorCode = errorCode;
        this.args = args;
    }

    public UserException(String errorCode, Throwable cause, String message, Object[] args) {
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
