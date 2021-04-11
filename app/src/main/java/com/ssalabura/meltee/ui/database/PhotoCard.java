package com.ssalabura.meltee.ui.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class PhotoCard {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String sender;
    public byte[] photo;
    public String text;

    public PhotoCard(String sender, byte[] photo, String text) {
        this.sender = sender;
        this.photo = photo;
        this.text = text;
    }
}
