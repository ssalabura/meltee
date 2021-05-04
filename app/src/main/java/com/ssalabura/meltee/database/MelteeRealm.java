package com.ssalabura.meltee.database;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.User;
import io.realm.mongodb.sync.SyncConfiguration;

public class MelteeRealm {
    private static App app;
    private static User user;
    private static String username;

    public static synchronized App getApp() {
        if(app == null) {
            app = new App(new AppConfiguration.Builder("meltee-nbigl").build());
        }
        return app;
    }

    public static void setConfig(User newUser, String newUsername) {
        user = newUser;
        username = newUsername;
    }

    private static SyncConfiguration getConfig(String partitionValue) {
        return new SyncConfiguration.Builder(
                user,partitionValue)
                .allowQueriesOnUiThread(true)
                .allowWritesOnUiThread(true)
                .build();
    }

    public static Realm getInstance(String partitionValue) {
        return Realm.getInstance(getConfig(partitionValue));
    }

    public static void insertPhoto(PhotoCard photoCard) {
        for(String receiver : photoCard.receivers) {
            photoCard._id = System.currentTimeMillis();
            photoCard.partition_key = receiver;
            Realm instance = getInstance(receiver);
            instance.executeTransaction(transaction -> {
                transaction.insertOrUpdate(photoCard);
            });
            instance.close();
            // temporary fix for different ids
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static List<PhotoCard> getPhotos() {
        Realm instance = getInstance(username);
        List<PhotoCard> photoCardList = instance.copyFromRealm(instance.where(PhotoCard.class).findAll());
        photoCardList.sort((o1, o2) -> Long.compare(o2._id, o1._id));
        instance.close();
        return photoCardList;
    }

    public static void insertFriend(String friendName) {
        Realm instance = getInstance(username);
        instance.executeTransaction(transaction -> {
            transaction.insertOrUpdate(new Friend(friendName, username));
        });
        instance.close();
    }

    public static List<String> getFriends() {
        Realm instance = getInstance(username);
        List<Friend> realmFriends = instance.where(Friend.class).findAll();
        List<String> output = new ArrayList<>();
        for(Friend friend : realmFriends) {
            output.add(friend._id);
        }
        instance.close();
        return output;
    }

    public static void removeFriend(String friendName) {
        Realm instance = getInstance(username);
        List<Friend> realmFriends = instance.where(Friend.class).findAll();
        for(Friend friend : realmFriends) {
            if(friend._id.equals(friendName)) {
                instance.executeTransaction(transaction -> {
                    friend.deleteFromRealm();
                });
            }
        }
        instance.close();
    }
}
