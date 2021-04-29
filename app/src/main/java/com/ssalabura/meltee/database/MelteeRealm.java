package com.ssalabura.meltee.database;

import com.ssalabura.meltee.MainActivity;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

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
        List<PhotoCard> realmList = instance.where(PhotoCard.class).findAll();
        List<PhotoCard> photoCardList = new ArrayList<>();
        for(PhotoCard photoCard : realmList) {
            if(photoCard.receivers.where().equalTo("_id", MainActivity.username).findFirst() != null) {
                photoCardList.add(instance.copyFromRealm(photoCard));
            }
        }
        photoCardList.sort((o1, o2) -> Long.compare(o2._id, o1._id));
        instance.close();
        return photoCardList;
    }
}
