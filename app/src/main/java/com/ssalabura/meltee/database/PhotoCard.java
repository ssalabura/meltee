package com.ssalabura.meltee.database;

import android.graphics.Bitmap;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

public class PhotoCard extends RealmObject {
    @PrimaryKey
    public long _id; //timestamp
    public String sender;
    public String receiver;
    public byte[] photo;
    public String message;

    @Ignore
    public Bitmap bitmap;

    public PhotoCard() {

    }
}
