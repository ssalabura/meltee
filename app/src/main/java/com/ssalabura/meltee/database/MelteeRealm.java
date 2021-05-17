package com.ssalabura.meltee.database;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.Sort;
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
    
    public static void logOut() {
        user.logOutAsync(result -> {
            if(result.isSuccess()) {
                System.out.println("Successfully logged out.");
            } else {
                System.out.println("Logout failed: " + result.getError().getErrorMessage());
            }
        });
    }

    public static void setConfig(User newUser, String newUsername) {
        user = newUser;
        username = newUsername;
    }

    private static SyncConfiguration getConfig(String partitionValue) {
        SyncConfiguration.Builder builder = new SyncConfiguration.Builder(
                user,partitionValue)
                .allowQueriesOnUiThread(true)
                .allowWritesOnUiThread(true);
        return builder.build();
    }

    public static Realm getInstance(String partitionValue) {
        return Realm.getInstance(getConfig(partitionValue));
    }

    public static void insertPhoto(PhotoCard photoCard) {
        photoCard.timestamp = System.currentTimeMillis();
        for(String receiver : photoCard.receivers) {
            photoCard._id = username + "|" + receiver + "|" + photoCard.timestamp;
            photoCard.partition_key = receiver;
            Realm instance = getInstance(receiver);
            instance.executeTransaction(transaction -> {
                transaction.insertOrUpdate(photoCard);
            });
            instance.close();
        }

        List<Friend> updatedFriends = new ArrayList<>();
        for(String receiver : photoCard.receivers) {
            Friend friend = new Friend(receiver, username);
            friend.lastPhotoTimestamp = photoCard.timestamp;
            updatedFriends.add(friend);
        }
        Realm instance = getInstance(username);
        instance.executeTransaction(transaction -> {
            transaction.insertOrUpdate(updatedFriends);
        });
        instance.close();
    }

    public static List<PhotoCard> getPhotos() {
        Realm instance = getInstance(username);
        List<PhotoCard> photoCardList = instance.copyFromRealm(
                instance.where(PhotoCard.class).findAll().sort("timestamp", Sort.DESCENDING));
        instance.close();
        return photoCardList;
    }

    public static void insertFriend(String friendName) {
        Realm instance = getInstance(username);
        Friend newFriend = new Friend(friendName, username);
        newFriend.lastPhotoTimestamp = 0;
        instance.executeTransaction(transaction -> {
            transaction.insertOrUpdate(newFriend);
        });
        instance.close();
    }

    public static List<Friend> getFriends() {
        Realm instance = getInstance(username);
        List<Friend> friendList = instance.copyFromRealm(
                instance.where(Friend.class).findAll().sort("lastPhotoTimestamp", Sort.DESCENDING));
        instance.close();
        return friendList;
    }

    public static void removeFriend(String friendName) {
        Realm instance = getInstance(username);
        for(Friend friend : instance.where(Friend.class).findAll()) {
            if(friend.username.equals(friendName)) {
                instance.executeTransaction(transaction -> {
                    friend.deleteFromRealm();
                });
            }
        }
        instance.close();
    }
}
