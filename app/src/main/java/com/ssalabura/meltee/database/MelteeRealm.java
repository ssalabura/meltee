package com.ssalabura.meltee.database;

import android.content.Context;

import com.ssalabura.meltee.MainActivity;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.Sort;
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
            app.loginAsync(credentials, result -> {
                System.out.println("Successfully logged in!");
                config = new SyncConfiguration.Builder(
                        result.get(),"Meltee")
                        .allowQueriesOnUiThread(true)
                        .allowWritesOnUiThread(true)
                        .build();
            });
        }
    }

    public static Realm getInstance() {
        while(config == null) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return Realm.getInstance(config);
    }

    public static void insertPhoto(PhotoCard photoCard) {
        getInstance().executeTransaction(transaction -> {
            transaction.insert(photoCard);
        });
    }

    public static List<PhotoCard> getPhotos() {
        return getInstance().copyFromRealm(getInstance().where(PhotoCard.class)
                .equalTo("receiver", MainActivity.username)
                .sort("_id", Sort.DESCENDING)
                .findAll());
    }
}
