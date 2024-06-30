package com.szaumoor.rumple.model.interfaces;

/**
 * Basic interface for entities. Also holds the ID and user ID fields common to all entities for reference.
 */
public interface Entity {
    String ID_FIELD = "_id";
    String USER_ID_FIELD = "user_id";
}
