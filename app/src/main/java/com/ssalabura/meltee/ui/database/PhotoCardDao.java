package com.ssalabura.meltee.ui.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface PhotoCardDao {
    @Query("SELECT * FROM photocard ORDER BY id DESC")
    List<PhotoCard> getAll();

    @Insert
    void insert(PhotoCard photoCard);
}
