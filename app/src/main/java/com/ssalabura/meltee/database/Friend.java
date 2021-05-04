package com.ssalabura.meltee.database;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Friend extends RealmObject {
    @PrimaryKey
    public long _id;
    public String username;
    @Required
    public String partition_key;

    public Friend() {

    }

    public Friend(String username, String partition_key) {
        this.username = username;
        this.partition_key = partition_key;
    }
}
