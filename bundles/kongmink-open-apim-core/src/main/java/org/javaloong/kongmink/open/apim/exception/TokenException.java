package org.javaloong.kongmink.open.apim.exception;

import org.javaloong.kongmink.open.common.exception.ErrorCodeException;

public class TokenException extends ErrorCodeException {

    public static final String TOKEN_NOT_EXISTS = "TokenNotExits";
    public static final String INVALID_TOKEN = "InvalidToken";

    public static TokenException tokenNotExits() {
        return new TokenException(TOKEN_NOT_EXISTS);
    }

    public static TokenException invalidToken(Throwable cause) {
        return new TokenException(cause.getMessage(), cause, INVALID_TOKEN);
    }

    public TokenException(String errorCode) {
        super(errorCode);
    }

    public TokenException(String message, Throwable cause, String errorCode) {
        super(message, cause, errorCode);
    }
}
