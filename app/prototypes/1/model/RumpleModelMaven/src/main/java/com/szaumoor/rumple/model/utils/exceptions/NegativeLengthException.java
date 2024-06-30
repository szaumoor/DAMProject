package com.szaumoor.rumple.model.utils.exceptions;

public class NegativeLengthException extends RuntimeException {
    public NegativeLengthException() {
    }

    public NegativeLengthException(String message) {
        super(message);
    }

    public NegativeLengthException(String message, Throwable cause) {
        super(message, cause);
    }

    public NegativeLengthException(Throwable cause) {
        super(cause);
    }

    public NegativeLengthException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
