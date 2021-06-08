package com.example.rpgitemshop.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.rpgitemshop.CharacterLog;
import com.example.rpgitemshop.ItemLog;

import java.util.List;

@Dao
public interface CharacterLogDAO {
    @Insert
    void insert(CharacterLog... characterLogs);

    @Update
    void update(CharacterLog... characterLogs);

    @Delete
    void delete (CharacterLog characterLog);

    @Query("SELECT * FROM " + AppDatabase.CHARACTER_TABLE + " ORDER BY mCharacterName")
    List<CharacterLog> getCharacterLogs();
}
