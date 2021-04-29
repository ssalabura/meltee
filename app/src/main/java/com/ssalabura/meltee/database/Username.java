package com.ssalabura.meltee.database;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Username extends RealmObject {
    @PrimaryKey
    public String _id;
    @Required
    public String partition_key;

    public Username() {

    }

    public Username(String username) {
        this._id = username;
        this.partition_key = "Meltee";
    }
}
