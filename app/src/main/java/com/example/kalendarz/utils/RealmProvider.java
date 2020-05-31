package com.example.kalendarz.utils;

import com.example.kalendarz.R;
import io.realm.*;

/**
 * Helper Class helping to always return Realm with same configuration
 */
public  class RealmProvider {
    private final static RealmConfiguration config = new RealmConfiguration
            .Builder()
            .schemaVersion(R.integer.SCHEMA_VERSION)
            .deleteRealmIfMigrationNeeded()
            .build();

    private RealmProvider() {

    }

    /**
     * Gets realm.
     *
     * @return the realm
     */
    public static Realm getRealm() {
        return Realm.getInstance(config);
    }

}
