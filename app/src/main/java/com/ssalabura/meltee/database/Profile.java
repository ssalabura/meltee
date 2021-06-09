package com.ssalabura.meltee.database;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Profile extends RealmObject {
    @PrimaryKey
    public String _id;
    @Required
    public String partition_key;
    public byte[] photo;

    public Profile() { }

    public Profile(String username) {
        _id = username;
        partition_key = "public";
    }
}
