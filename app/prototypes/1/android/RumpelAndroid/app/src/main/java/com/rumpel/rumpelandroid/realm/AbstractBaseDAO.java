package com.rumpel.rumpelandroid.realm;

import android.content.Context;

import com.szaumoor.rumple.model.interfaces.AbstractEntity;

public abstract class AbstractBaseDAO<T extends AbstractEntity<Object>> extends AbstractDAO implements BaseDAO<T>{
    protected AbstractBaseDAO(final Context context, final String collName) {
        super(context, collName);
    }
}
