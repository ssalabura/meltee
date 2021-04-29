package com.ssalabura.meltee.database;

import android.graphics.Bitmap;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class PhotoCard extends RealmObject {
    @PrimaryKey
    public long _id; //timestamp
    @Required
    public String partition_key;
    public String sender;
    public RealmList<Username> receivers;
    public byte[] photo;
    public String message;

    @Ignore
    public Bitmap bitmap;

    public PhotoCard() {

    }
}
