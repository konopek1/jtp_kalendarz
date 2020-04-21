package com.example.kalendarz.utils;

import com.example.kalendarz.R;
import io.realm.*;

public  class RealmProvider {
    private final static RealmConfiguration config = new RealmConfiguration
            .Builder()
            .schemaVersion(R.integer.SCHEMA_VERSION)
            .deleteRealmIfMigrationNeeded()
            .build();

    private RealmProvider() {

    }

    public static Realm getRealm() {
        return Realm.getInstance(config);
    }

}
