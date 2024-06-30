package com.szaumoor.rumple.db.utils;

/**
 * Enum that represents various outcomes in database interactions
 */
public enum Outcome {
    ERROR,
    NOT_FOUND,
    UNIQUE_EXISTS,
    SUCCESS,
    NULL,
    TIMEOUT,
    TRANSACTION_FAILED,
    CANNOT_DELETE_IN_USE,
}
