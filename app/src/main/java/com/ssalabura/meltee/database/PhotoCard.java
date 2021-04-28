package com.ssalabura.meltee.database;

import android.graphics.Bitmap;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class PhotoCard {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String sender;
    public String receiver;
    public long timestamp;
    public byte[] photo;
    public String message;

    @Ignore // helpful to store in AddPhotoFragment
    public Bitmap bitmap;

    @Ignore
    public PhotoCard() {

    }

    public PhotoCard(String sender, long timestamp, byte[] photo, String message) {
        this.sender = sender;
        this.timestamp = timestamp;
        this.photo = photo;
        this.message = message;
    }

    public PhotoCard(RealmPhotoCard realmPhotoCard) {
        this.sender = realmPhotoCard.sender;
        this.receiver = realmPhotoCard.receiver;
        this.timestamp = realmPhotoCard._id;
        this.photo = realmPhotoCard.photo;
        this.message = realmPhotoCard.message;
    }
}
