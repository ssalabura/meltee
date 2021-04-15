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
    public byte[] photo;
    public String message;

    @Ignore // helpful to store in AddPhotoFragment
    public Bitmap bitmap;

    @Ignore
    public PhotoCard() {

    }

    public PhotoCard(String sender, byte[] photo, String message) {
        this.sender = sender;
        this.photo = photo;
        this.message = message;
    }
}
