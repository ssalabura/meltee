package com.ssalabura.meltee.database;

import android.content.Context;

import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.sync.SyncConfiguration;

public class MelteeRealm {
    private static SyncConfiguration config;

    public static synchronized void initialize(Context context) {
        if(config == null) {
            Realm.init(context);

            App app = new App(new AppConfiguration.Builder("meltee-nbigl").build());

            Credentials credentials = Credentials.anonymous();
            app.login(credentials);

            String partitionValue = "Meltee";
            config = new SyncConfiguration.Builder(
                    app.currentUser(),
                    partitionValue)
                    .allowQueriesOnUiThread(true)
                    .allowWritesOnUiThread(true)
                    .build();
        }
    }

    public static Realm getInstance() {
        return Realm.getInstance(config);
    }

    public static void insertPhoto(RealmPhotoCard photoCard) {
        getInstance().executeTransaction(transaction -> {
            transaction.insert(photoCard);
        });
    }
}
