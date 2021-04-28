package com.ssalabura.meltee.database;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class RealmPhotoCard extends RealmObject {
    @PrimaryKey
    public long _id; //timestamp
    public String sender;
    public String receiver;
    public byte[] photo;
    public String message;

    public RealmPhotoCard() {

    }

    public RealmPhotoCard(PhotoCard photoCard) {
        this._id = photoCard.timestamp;
        this.sender = photoCard.sender;
        this.receiver = photoCard.receiver;
        this.photo = photoCard.photo;
        this.message = photoCard.message;
    }
}
