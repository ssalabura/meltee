package com.ssalabura.meltee.database;

import android.graphics.Bitmap;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Friend extends RealmObject {
    @PrimaryKey
    public String _id;
    @Required
    public String partition_key;
    public String username;
    public long lastPhotoTimestamp;

    @Ignore
    public Bitmap profilePicture;

    public Friend() { }

    public Friend(String username, String partition_key) {
        this._id = partition_key + "|" + username;
        this.username = username;
        this.partition_key = partition_key;
    }
}
