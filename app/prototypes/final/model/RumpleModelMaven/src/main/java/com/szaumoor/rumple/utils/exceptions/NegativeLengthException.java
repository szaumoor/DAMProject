package com.szaumoor.rumple.utils.exceptions;

/**
 * Barebone custom class to provide a meaningfully named exception for some runtime exceptions
 */
public final class NegativeLengthException extends RuntimeException {
    public NegativeLengthException() {
    }

    public NegativeLengthException(final String message) {
        super(message);
    }

    public NegativeLengthException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public NegativeLengthException(final Throwable cause) {
        super(cause);
    }

    public NegativeLengthException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
