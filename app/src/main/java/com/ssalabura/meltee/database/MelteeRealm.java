package com.ssalabura.meltee.database;

import java.util.List;

import io.realm.Realm;
import io.realm.Sort;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.User;
import io.realm.mongodb.sync.SyncConfiguration;

public class MelteeRealm {
    private static App app;
    private static SyncConfiguration config;

    public static synchronized App getApp() {
        if(app == null) {
            app = new App(new AppConfiguration.Builder("meltee-nbigl").build());
        }
        return app;
    }

    public static void makeConfig(User user) {
        config = new SyncConfiguration.Builder(
                user,"Meltee")
                .allowQueriesOnUiThread(true)
                .allowWritesOnUiThread(true)
                .build();
    }

    public static Realm getInstance() {
        return Realm.getInstance(config);
    }

    public static void insertPhoto(PhotoCard photoCard) {
        Realm instance = getInstance();
        instance.executeTransaction(transaction -> {
            transaction.insertOrUpdate(photoCard);
        });
        instance.close();
    }

    public static List<PhotoCard> getPhotos() {
        Realm instance = getInstance();
        List<PhotoCard> realmList = instance.where(PhotoCard.class)
                .sort("_id", Sort.DESCENDING)
                .findAll();
        List<PhotoCard> photoCardList = instance.copyFromRealm(realmList);
        instance.close();
        return photoCardList;
    }
}
