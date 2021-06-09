package com.ssalabura.meltee.database;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.ssalabura.meltee.util.BitmapTools;

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

    public static void setConfig(User newUser, String newUsername) {
        user = newUser;
        username = newUsername;
    }

    public static void startListener(Context context) {
        getInstance(username).addChangeListener(
                realm -> RealmNotificationManager.reactToChanges(context)
        );
        Log.i("Meltee", "Started realm listener.");
    }
    
    public static void logOut() {
        user.logOutAsync(result -> {
            if(result.isSuccess()) {
                Log.i("Meltee", "Successfully logged out.");
            } else {
                Log.e("Meltee", "Logout failed.", result.getError());
            }
        });
    }

    private static SyncConfiguration getConfig(String partitionValue) {
        SyncConfiguration.Builder builder = new SyncConfiguration.Builder(
                user, partitionValue)
                .allowQueriesOnUiThread(true)
                .allowWritesOnUiThread(true);
        return builder.build();
    }

    public static Realm getInstance(String partitionValue) {
        Log.d("Meltee", "Getting instance of realm: " + partitionValue);
        return Realm.getInstance(getConfig(partitionValue));
    }

    public static void insertPhoto(PhotoCard photoCard) {
        photoCard.timestamp = System.currentTimeMillis();
        for(String receiver : photoCard.receivers) {
            photoCard._id = username + "|" + receiver + "|" + photoCard.timestamp;
            photoCard.partition_key = receiver;
            Realm instance = getInstance(receiver);
            instance.executeTransaction(transaction -> {
                transaction.insert(photoCard);
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
                instance.where(PhotoCard.class)
                        .greaterThan("timestamp", System.currentTimeMillis()-24*60*60*1000)
                        .findAll().sort("timestamp", Sort.DESCENDING));
        instance.close();
        return photoCardList;
    }

    public static void insertFriend(String friendName) {
        Realm instance = getInstance(username);
        Friend newFriend = new Friend(friendName, username);
        newFriend.lastPhotoTimestamp = 0;
        try {
            instance.executeTransaction(transaction -> {
                transaction.insert(newFriend);
            });
        } catch(Exception e) {
            Log.e("Meltee", "Failed inserting friend: " + e.getMessage());
        }
        instance.close();
    }

    public static List<Friend> getFriends() {
        Realm instance = getInstance(username);
        List<Friend> friendList = instance.copyFromRealm(
                instance.where(Friend.class).findAll().sort("lastPhotoTimestamp", Sort.DESCENDING));
        instance.close();

        String[] usernames = friendList.stream().map(friend -> friend.username).toArray(String[]::new);

        instance = getInstance("public");
        List<Profile> friendProfiles = instance.copyFromRealm(
                instance.where(Profile.class).in("_id", usernames).findAll());
        for(Friend friend : friendList) {
            for(Profile profile : friendProfiles) {
                if(friend.username.equals(profile._id)) {
                    friend.profilePicture = BitmapTools.fromByteArray(profile.photo);
                }
            }
        }
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

    public static Bitmap getProfilePicture() {
        Realm instance = getInstance("public");
        Profile realmProfile = instance.where(Profile.class).equalTo("_id", username).findFirst();
        if(realmProfile == null) {
            instance.close();
            return null;
        }
        Bitmap bitmap = BitmapTools.fromByteArray(instance.copyFromRealm(realmProfile).photo);
        instance.close();
        return bitmap;
    }

    public static void updateProfile(Bitmap profilePicture) {
        Realm instance = getInstance("public");
        Profile profile = new Profile(username);
        profile.photo = BitmapTools.toByteArray(profilePicture);
        instance.executeTransaction(transaction -> {
            transaction.insertOrUpdate(profile);
        });
        instance.close();
    }
}
