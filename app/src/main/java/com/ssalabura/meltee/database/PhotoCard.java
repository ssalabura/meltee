package com.ssalabura.meltee.database;

import android.graphics.Bitmap;

import java.util.List;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class PhotoCard extends RealmObject {
    @PrimaryKey
    public String _id;
    @Required
    public String partition_key;
    public String sender;
    public long timestamp;
    public byte[] photo;
    public String message;
    public String location;

    @Ignore
    public List<String> receivers;
    @Ignore
    public Bitmap bitmap;

    public PhotoCard() { }
}
