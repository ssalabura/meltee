package com.ssalabura.meltee.database;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class RealmPhotoCard extends RealmObject {
    @PrimaryKey
    public int _id;
    public String sender;
    public String receiver;
    public long timestamp;
    public byte[] photo;
    public String message;

    public RealmPhotoCard() {

    }

    public RealmPhotoCard(PhotoCard photoCard) {
        this.sender = photoCard.sender;
        this.receiver = "admin";
        this.timestamp = photoCard.timestamp;
        this.photo = photoCard.photo;
        this.message = photoCard.message;
    }
}
